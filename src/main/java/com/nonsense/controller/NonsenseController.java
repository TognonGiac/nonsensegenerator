package com.nonsense.controller;

import com.nonsense.SentenceAnalyzer;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class NonsenseController {

    @PostMapping("/analyze")
    public Map<String, Object> analyzeSentence(@RequestBody Map<String, String> payload) {
        String sentence = payload.get("sentence");
        Map<String, Object> response = new HashMap<>();

        try {
            SentenceAnalyzer analyzer = new SentenceAnalyzer();
            response.put("nouns", analyzer.extractNouns(sentence));
            response.put("verbs", analyzer.extractVerbs(sentence));
            response.put("adjectives", analyzer.extractAdjectives(sentence));
        } catch (IOException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}
