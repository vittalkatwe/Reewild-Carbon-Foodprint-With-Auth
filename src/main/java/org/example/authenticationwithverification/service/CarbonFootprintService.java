package org.example.authenticationwithverification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authenticationwithverification.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CarbonFootprintService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.url}")
    private String apiUrl;

    @Value("${google.api.vision.url}")
    private String visionApiUrl;


    public CarbonFootprintResponse calculateCarbonFootprintFromImage(MultipartFile imageFile, long servings) throws IOException {
        String dishName = identifyDishFromImage(imageFile);

        if (dishName == null || dishName.equalsIgnoreCase("Unknown") || !isDishRelevantAndEdible(dishName)) {
            throw new IllegalArgumentException("The image does not appear to contain an edible food item. Please upload a relevant image.");
        }

        System.out.println("Validation passed for dish: " + dishName);
        return calculateCarbonFootprint(dishName, servings);
    }

    private boolean isDishRelevantAndEdible(String dishName) {
        String prompt = "Is '" + dishName + "' a type of food, an edible dish, or an edible ingredient? Respond with only the word 'Yes' or 'No'.";

        String fullApiUrl = apiUrl + "?key=" + apiKey;
        GeminiRequest request = new GeminiRequest(prompt);

        try {
            GeminiResponse geminiResponse = restTemplate.postForObject(fullApiUrl, request, GeminiResponse.class);

            if (geminiResponse != null && geminiResponse.getResponseText() != null) {
                String responseText = geminiResponse.getResponseText().trim();
                return responseText.equalsIgnoreCase("Yes");
            }
        } catch (Exception e) {
            System.err.println("Relevance check API call failed: " + e.getMessage());
        }

        return false;
    }

    private String identifyDishFromImage(MultipartFile imageFile) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
        String mimeType = imageFile.getContentType();
        String prompt = "Identify the dish in this image. Respond with only the common name of the dish, for example 'Margherita Pizza' or 'Chicken Biryani'. If you cannot identify a dish, respond with 'Unknown'.";

        GeminiVisionRequest request = new GeminiVisionRequest(prompt, base64Image, mimeType);

        String fullApiUrl = visionApiUrl + "?key=" + apiKey;
        GeminiResponse geminiResponse = restTemplate.postForObject(fullApiUrl, request, GeminiResponse.class);

        if (geminiResponse == null || geminiResponse.isBlocked()) {
            System.err.println("Image identification was blocked due to safety concerns.");
            return null;
        }

        if (geminiResponse.getResponseText() != null) {
            return geminiResponse.getResponseText().trim();
        }
        return null;
    }


    private final Map<String, CarbonFootprintResponse> footprintCache = new ConcurrentHashMap<>();

    public CarbonFootprintResponse calculateCarbonFootprint(String dish, long servings) {

        final String cacheKey = dish.trim().toLowerCase();
        CarbonFootprintResponse singleServingResponse = footprintCache.get(cacheKey);

        if (singleServingResponse == null) {
            System.out.println("CACHE MISS for dish: " + dish);

            String prompt = createPromptForDish(dish);
            String fullApiUrl = apiUrl + "?key=" + apiKey;
            GeminiRequest request = new GeminiRequest(prompt);
            GeminiResponse geminiResponse = restTemplate.postForObject(fullApiUrl, request, GeminiResponse.class);

            if (geminiResponse == null || geminiResponse.getResponseText() == null) {
                return null;
            }

            singleServingResponse = parseAiResponse(geminiResponse.getResponseText());

            if (isResponseValid(singleServingResponse)) {
                footprintCache.put(cacheKey, singleServingResponse);
            } else {
                System.err.println("AI response for '" + dish + "' was invalid. Not caching.");
                return null;
            }
        } else {
            System.out.println("CACHE HIT for dish: " + dish);
        }

        return adjustForServings(singleServingResponse, servings);
    }

    private String createPromptForDish(String dish) {
        return "You are a food data analyst. Your task is to calculate the carbon and water footprint for a single serving of a dish. " +
                "Your response MUST be only a single, minified JSON object and nothing else. " +

                "For the `estimated_carbon_kg`, use conservative, average global values from reputable life cycle assessment (LCA) databases. " +
                "The calculation should focus on 'cradle-to-farm-gate' emissions. Do NOT include emissions from packaging, retail, or transport. " +
                "Do NOT use overestimated values. " +

                "For the `waterPrint`, calculate the **blue water footprint** in liters. The blue water footprint represents the volume of surface and groundwater consumed (e.g., from irrigation). " +
                "This calculation should also be 'cradle-to-farm-gate'. " +
                "Do NOT include green water (rainwater) or grey water (polluted water) in this value. Using only the blue water footprint will ensure the value is precise and not overestimated. " +

                "The JSON structure MUST be: {\"dish\":\"[Dish Name]\",\"estimated_carbon_kg\":0.0,\"ingredients\":[{\"name\":\"IngredientName\",\"carbon_kg\":0.0}],\"waterPrint\":0}. " +
                "For example, for the dish 'Boiled White Rice', a precise response using these rules would be: " +
                "{\"dish\":\"Boiled White Rice\",\"estimated_carbon_kg\":0.5,\"ingredients\":[{\"name\":\"White Rice (uncooked)\",\"carbon_kg\":0.5}],\"waterPrint\":250}. " +

                "Now, provide the data for the dish: '" + dish + "'.";
    }

    private boolean isResponseValid(CarbonFootprintResponse response) {
        if (response == null) return false;

        if (response.getEstimated_carbon_kg() < 0 || response.getWaterPrint() < 0) {
            System.err.println("Validation failed: Response contains negative values.");
            return false;
        }
        for (Ingredient ingredient : response.getIngredients()) {
            if (ingredient.getCarbon_kg() < 0) {
                System.err.println("Validation failed: Ingredient '" + ingredient.getName() + "' has a negative carbon value.");
                return false;
            }
        }

        double ingredientsSum = response.getIngredients().stream()
                .mapToDouble(Ingredient::getCarbon_kg)
                .sum();
        if (Math.abs(ingredientsSum - response.getEstimated_carbon_kg()) > (response.getEstimated_carbon_kg() * 0.1)) {
            System.err.println("Validation failed: Sum of ingredients (" + ingredientsSum + ") does not match total (" + response.getEstimated_carbon_kg() + ").");
            return false;
        }

        if (response.getEstimated_carbon_kg() > 20) {
            System.err.println("Validation failed: Estimated carbon is unrealistically high (>20kg).");
            return false;
        }

        return true;
    }


    private CarbonFootprintResponse parseAiResponse(String aiResponse) {
        String cleanedJson = aiResponse.replace("```json", "").replace("```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(cleanedJson, CarbonFootprintResponse.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing AI response JSON: " + e.getMessage());
            return null;
        }
    }


    private CarbonFootprintResponse adjustForServings(CarbonFootprintResponse singleServingResponse, long servings) {
        if (servings <= 1) {
            return singleServingResponse;
        }

        CarbonFootprintResponse multiServingResponse = new CarbonFootprintResponse();
        multiServingResponse.setDish(singleServingResponse.getDish());

        multiServingResponse.setEstimated_carbon_kg(singleServingResponse.getEstimated_carbon_kg() * servings);
        multiServingResponse.setWaterPrint(singleServingResponse.getWaterPrint() * servings);

        multiServingResponse.setIngredients(
                singleServingResponse.getIngredients().stream()
                        .map(ingredient -> new Ingredient(ingredient.getName(), ingredient.getCarbon_kg() * servings))
                        .collect(Collectors.toList())
        );

        return multiServingResponse;
    }
}
