package com.nonsense.controller;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.NonsenseGenerator;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin // permette richieste da GitHub Pages
@RequestMapping("/api")
public class NonsenseController {

    @PostMapping("/analyze")
    public Map<String, Object> analyzeSentence(@RequestBody Map<String, String> payload) {
        String sentence = payload.get("sentence");
        int count = Integer.parseInt(payload.getOrDefault("count", "1"));
        Map<String, Object> response = new HashMap<>();

        try {
            SentenceAnalyzer analyzer = new SentenceAnalyzer();
            List<String> generated = NonsenseGenerator.generateSentences(
                analyzer.extractNouns(sentence),
                analyzer.extractVerbs(sentence),
                analyzer.extractAdjectives(sentence),
                count
            );
            response.put("sentences", generated);
        } catch (IOException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}
