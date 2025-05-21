package com.nonsense.controller;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.service.NonsenseService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nonsense.dto.SaveRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            response.put("error", "The number of sentences must be between 1 and 10.");
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

    @PostMapping("/save")
    public ResponseEntity<String> savePhrasesToFile(@RequestBody SaveRequest request) {
        try {
            Path outputDir = Paths.get("output");
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            String filename = "frasi_" + System.currentTimeMillis() + ".txt";
            Path outputFile = outputDir.resolve(filename);

            Files.write(outputFile, request.getPhrases());

            return ResponseEntity.ok("Frasi salvate nel file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel salvataggio");
        }
    }
}
