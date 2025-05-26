package com.nonsense.service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.NonsenseGenerator;
import com.nonsense.util.ToxicityChecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
* Service per la generazione di frasi nonsense.
* Analizza le frasi ottenute dall'utente in input, aggiorna i vocabolari con le nuove parole ottenute,
* filtra le frasi tossiche e mostra in output le nuove frasi nonsense generate.
*/
@Service
public class NonsenseService {

    private static final Set<String> globalNouns = new HashSet<>();
    private static final Set<String> globalVerbs = new HashSet<>();
    private static final Set<String> globalAdjectives = new HashSet<>();

    private static final Path NOUNS_FILE = Paths.get("src/main/resources/data/nouns.txt");
    private static final Path VERBS_FILE = Paths.get("src/main/resources/data/verbs.txt");
    private static final Path ADJECTIVES_FILE = Paths.get("src/main/resources/data/adjectives.txt");

    /**
    * Chiave API di Google NLP, letta da application.properties
    */
    @Value("${google.api.key}")
    private String apiKey;

    /** 
    * Genera frasi nonsense a partire da una frase di input ottenuta dall'utente.
    * Analizza le frasi in input, aggiorna i vocabolari e filtra le frasi tossiche.
    *
    * @param sentence : frase in input da cui estrarre le parole
    * @param count : numero di frasi nonsense che l'utente vuole generare
    * @return lista di frasi generate 
    * @throws Exception se l'analisi sintattica non va a buon fine o se manca la chiave API 
    */
    public List<String> generateNonsenseSentences(String sentence, int count) throws Exception {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("API key not found: Make sure you have set google.api.key in application.properties");
        }
        
        SentenceAnalyzer analyzer = new SentenceAnalyzer(apiKey);
        Map<String, List<String>> parts = analyzer.analyzeSyntax(sentence);
        analyzer.close();

        List<String> nouns = parts.get("nouns");
        List<String> verbs = parts.get("verbs");
        List<String> adjectives = parts.get("adjectives");

        if (nouns != null) globalNouns.addAll(nouns);
        if (verbs != null) globalVerbs.addAll(verbs);
        if (adjectives != null) globalAdjectives.addAll(adjectives);

        updateWordFile(NOUNS_FILE, nouns);
        updateWordFile(VERBS_FILE, verbs);
        updateWordFile(ADJECTIVES_FILE, adjectives);

        List<String> generated = NonsenseGenerator.generateSentences(nouns, verbs, adjectives, count);

        List<String> filtered = new ArrayList<>();
        for (String sentenceOut : generated) {
            try {
                if (ToxicityChecker.isToxic(sentenceOut, apiKey)) {
                    filtered.add("Sentence not shown due to its toxicity");
                } else {
                    filtered.add(sentenceOut);
                }
            } catch (Exception e) {
                filtered.add("Error while checking toxicity");
            }
        }

        return filtered;
    }

    /**
    * Aggiorna un file con le nuove parole, assicurandosi di non avere duplicati
    * 
    * @param filePath : persorso del dile da aggiornare
    * @param newWords : lista di nuove parole da aggiungere al file
    * @throws IOExcpetion se il file non può essere letto o scritto
    */
    private void updateWordFile(Path filePath, List<String> newWords) throws IOException {
        if (newWords == null || newWords.isEmpty()) return;

        Set<String> existing = new HashSet<>();

        if (Files.exists(filePath)) {
            existing.addAll(Files.readAllLines(filePath).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet()));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String word : newWords) {
                if (!existing.contains(word)) {
                    writer.write(word);
                    writer.newLine();
                }
            }
        }
    }

    /**
    * Restituisce l'insieme dei nomi raccolti finora.
    *
    * @return insieme di nomi
    */
    public static Set<String> getGlobalNouns() { return globalNouns; }

    /**
    * Restituisce l'insieme dei verbi raccolti finora.
    *
    * @return insieme dei verbi
    */
    public static Set<String> getGlobalVerbs() { return globalVerbs; }

    /**
    * Restituisce l'insieme degli aggettivi raccolti finora.
    *
    * @return insieme degli aggettivi 
    */
    public static Set<String> getGlobalAdjectives() { return globalAdjectives; }
}
