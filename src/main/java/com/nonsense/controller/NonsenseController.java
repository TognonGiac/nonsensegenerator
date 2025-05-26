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
* <p>
* Questo Controller espone endpoint RESTful per:
* <ul>
*    <li>Generare frasi nonsense basate sull'input dato dall'utente</li>
*    <li>Salvare la lista delle frasi su file</li>
*    <li>Controllare la tossicità della frase</li>
* </ul>
* </p>
*/
@RestController
@CrossOrigin(origins = "*")// permette richieste da GitHub Pages
@RequestMapping("/api")
public class NonsenseController {

    /** Servizio per la generazione di frasi nonsense. */
    private final NonsenseService service;

    /**
    * Costruttore del controller
    * 
    * @param service istanza del servizio per la generazione delle frasi nonsense
    */
    public NonsenseController(NonsenseService service) {
        this.service = service;
    }

    /**
    * Endpoint POST per generare delle frasi nonsense partendo da una frase ottenuta in input dall'utente.
    *
    *<p>
    * Questo endpoint: 
    * permette di ricevere una frase in input dall'utente, analizza la frase sintatticmante, e genera
    * un numero di frasi nonsense decide dall'utente utilizzando le parole estratte.
    * </p>
    
    * @param payload     mappa JSON che contiene i parametri della richiesta:
    *                    <ul> 
    *                        <li>{@code "sentence"} (String) : frase in input da analizzare</li>
    *                        <li>{@code "count"} (Number) : numero di frasi che l'utente vuole generare (min 1 - max 10)</li>
    *                    </ul>
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
    * <p>
    * Questo endpoint permette all'utente di salvare le frasi generate all'interno di un file di testo.
    *
    * @param request oggetto {@link SaveRequest} contiene la lista di frasi generate da salvare.
    * @return {@link ResponseEntity} : una risposta HTTP con un messaggio che indica se il salvataggio è avvenuto con successo o se si è presentato un errore.
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
