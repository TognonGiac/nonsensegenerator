package com.nonsense.dto;

import java.util.List;

public class SaveRequest {
    private List<String> phrases;

    public List<String> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }
}
