package org.example.authenticationwithverification.dto;

import java.util.List;

public class GeminiRequest {
    private final List<Content> contents;
    private final GenerationConfig generationConfig;

    public GeminiRequest(String prompt) {
        Part part = new Part(prompt);
        Content content = new Content(List.of(part));
        this.contents = List.of(content);
        this.generationConfig = new GenerationConfig(0.1f);
    }
    public List<Content> getContents() { return contents; }
    public GenerationConfig getGenerationConfig() { return generationConfig; }
}

class Content {
    private final List<Part> parts;
    public Content(List<Part> parts) { this.parts = parts; }
    public List<Part> getParts() { return parts; }
}

class Part {
    private final String text;
    public Part(String text) { this.text = text; }
    public String getText() { return text; }
}

class GenerationConfig {
    private final float temperature;

    public GenerationConfig(float temperature) {
        this.temperature = temperature;
    }
    public float getTemperature() {
        return temperature;
    }
}
