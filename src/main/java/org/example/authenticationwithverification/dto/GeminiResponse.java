package org.example.authenticationwithverification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {
    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;

    public String getResponseText() {
        if (candidates != null && !candidates.isEmpty() && candidates.get(0).getContent() != null) {
            return candidates.get(0).getContent().getParts().get(0).getText();
        }
        return null;
    }

    public boolean isBlocked() {
        if (promptFeedback != null && "SAFETY".equals(promptFeedback.getBlockReason())) {
            return true;
        }
        if (candidates != null && !candidates.isEmpty() && "SAFETY".equals(candidates.get(0).getFinishReason())) {
            return true;
        }
        return false;
    }

    public List<Candidate> getCandidates() { return candidates; }
    public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
    public PromptFeedback getPromptFeedback() { return promptFeedback; }
    public void setPromptFeedback(PromptFeedback promptFeedback) { this.promptFeedback = promptFeedback; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Candidate {
    private Content content;
    private String finishReason;
    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }
    public String getFinishReason() { return finishReason; }
    public void setFinishReason(String finishReason) { this.finishReason = finishReason; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class PromptFeedback {
    private String blockReason;
    public String getBlockReason() { return blockReason; }
    public void setBlockReason(String blockReason) { this.blockReason = blockReason; }
}
