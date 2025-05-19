package com.nonsense.controller;

import com.nonsense.SentenceAnalyzer;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class NonsenseController {

    @PostMapping("/generate")
    public Map<String, Object> generate(@RequestBody Map<String, Object> payload) {
        String sentence = (String) payload.get("sentence");
        int count = (int) payload.getOrDefault("count", 1);

        Map<String, Object> response = new HashMap<>();
        List<String> results = new ArrayList<>();

        try {
            SentenceAnalyzer analyzer = new SentenceAnalyzer();

            for (int i = 0; i < count; i++) {
                String generated = analyzer.generateSentence(sentence);
                results.add(generated);
            }

            response.put("sentences", results);
        } catch (IOException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}
