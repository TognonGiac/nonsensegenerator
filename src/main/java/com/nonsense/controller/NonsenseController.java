package com.nonsense.controller;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.service.NonsenseService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin // permette richieste da GitHub Pages
@RequestMapping("/api")
public class NonsenseController {

    private final NonsenseService service = new NonsenseService();

    @PostMapping("/generate")
    public Map<String, Object> generateSentences(@RequestBody Map<String, Object> payload) {
        String sentence = (String) payload.get("sentence");
        int count = (int) payload.get("count");

        Map<String, Object> response = new HashMap<>();

        try {
            List<String> generated = service.generateNonsenseSentences(sentence, count);
            response.put("sentences", generated);
        } catch (IOException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}
