package org.example.authenticationwithverification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class GeminiVisionRequest {
    private final List<VisionContent> contents;
    private final List<SafetySetting> safetySettings;

    public GeminiVisionRequest(String prompt, String base64Image, String mimeType) {
        VisionPart textPart = new VisionPart(prompt, null);
        VisionPart imagePart = new VisionPart(null, new VisionInlineData(mimeType, base64Image));
        VisionContent content = new VisionContent(List.of(textPart, imagePart));
        this.contents = List.of(content);

        this.safetySettings = List.of(
                new SafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_ONLY_HIGH"),
                new SafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_ONLY_HIGH"),
                new SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_ONLY_HIGH"),
                new SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_ONLY_HIGH")
        );
    }

    public List<VisionContent> getContents() { return contents; }
    public List<SafetySetting> getSafetySettings() { return safetySettings; }
}

class SafetySetting {
    private final String category;
    private final String threshold;

    public SafetySetting(String category, String threshold) {
        this.category = category;
        this.threshold = threshold;
    }

    public String getCategory() { return category; }
    public String getThreshold() { return threshold; }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class VisionPart {
    private String text;
    private VisionInlineData inlineData;

    public VisionPart(String text, VisionInlineData inlineData) {
        this.text = text;
        this.inlineData = inlineData;
    }
    public String getText() { return text; }
    public VisionInlineData getInlineData() { return inlineData; }
}

class VisionInlineData {
    private final String mimeType;
    private final String data;

    public VisionInlineData(String mimeType, String data) {
        this.mimeType = mimeType;
        this.data = data;
    }
    public String getMimeType() { return mimeType; }
    public String getData() { return data; }
}

class VisionContent {
    private final List<VisionPart> parts;
    public VisionContent(List<VisionPart> parts) { this.parts = parts; }
    public List<VisionPart> getParts() { return parts; }
}

