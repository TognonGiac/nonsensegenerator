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

/**
* Controller REST principale per la gestione delle richieste relative al generatore di frasi nonsense.
* Espone endpoint per generare e salvare frasi.
*/
@RestController
@CrossOrigin(origins = "*")// permette richieste da GitHub Pages
@RequestMapping("/api")
public class NonsenseController {

    private final NonsenseService service;

    /**
    * Costruttore del controller
    * 
    * @param service istanza di {@link NonsenseService} per la generazione delle frasi
    */
    public NonsenseController(NonsenseService service) {
        this.service = service;
    }

    /**
    * Endpoint POST per generare delle frasi nonsense partendo da una frase ottenuta in input dall'utente.
    *
    * @param payload -> oggetto JSON che contiene:
    *                     - "sentence" : frase di input
    *                     - "count" : numero di frasi che l'utente vuole generare (min 1 - max 10)
    * @return una lista di frasi generate o un messaggio di errore
    * @throws Exception se durante la generazione di una frase nonsense si verifica un errore
    */
    @PostMapping("/generate")
    public Map<String, Object> generateSentences(@RequestBody Map<String, Object> payload) throws Exception {
        String sentence = (String) payload.get("sentence");
        Number countNumber = (Number) payload.get("count");
        int count = countNumber.intValue();

        Map<String, Object> response = new HashMap<>();

        if (sentence == null || sentence.trim().isEmpty()) {
            response.put("error", "Sentence not valid.");
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

    /**
    * Endpoint POST per salvare le frasi nonsense generate su un file di testo locale.
    *
    * @param request oggetto {@link SaveRequest} contiene la lista di frasi generate da salvare.
    * @return una risposta HTTP con un messaggio che indica se il salvataggio è avvenuto con successo o se è avvenuto un errore.
    */
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

            return ResponseEntity.ok("Sentence saved on file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while saving the file.");
        }
    }
}
