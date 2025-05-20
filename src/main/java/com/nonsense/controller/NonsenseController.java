package com.nonsense.controller;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.service.NonsenseService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")// permette richieste da GitHub Pages
@RequestMapping("/api")
public class NonsenseController {

    private final NonsenseService service;

    public NonsenseController(NonsenseService service) {
        this.service = service;
    }
    
    @PostMapping("/generate")
    public Map<String, Object> generateSentences(@RequestBody Map<String, Object> payload) throws Exception {
        String sentence = (String) payload.get("sentence");
        Number countNumber = (Number) payload.get("count");
        int count = countNumber.intValue();

        Map<String, Object> response = new HashMap<>();

        if (sentence == null || sentence.trim().isEmpty()) {
            response.put("error", "Frase non valida.");
            return response;
        }

        if (count < 1 || count > 10) {
            response.put("error", "Il numero di frasi deve essere tra 1 e 10.");
            return response;
        }

        try {
            List<String> generated = service.generateNonsenseSentences(sentence, count);

            // TODO: filtro tossicità

            response.put("sentences", generated);
        } catch (IOException e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
}
