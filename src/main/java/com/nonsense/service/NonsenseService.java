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

@Service
public class NonsenseService {

    private static final Set<String> globalNouns = new HashSet<>();
    private static final Set<String> globalVerbs = new HashSet<>();
    private static final Set<String> globalAdjectives = new HashSet<>();
    private static final Set<String> globalProperNouns = new HashSet<>();

    private static final Path NOUNS_FILE = Paths.get("src/main/resources/data/nouns.txt");
    private static final Path VERBS_FILE = Paths.get("src/main/resources/data/verbs.txt");
    private static final Path ADJECTIVES_FILE = Paths.get("src/main/resources/data/adjectives.txt");
    private static final Path PROPER_NOUNS_FILE = Paths.get("src/main/resources/data/proper_nouns.txt");

    @Value("${google.api.key}")
    private String apiKey;
    
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
        List<String> properNouns = parts.get("properNouns");

        if (nouns != null) globalNouns.addAll(nouns);
        if (verbs != null) globalVerbs.addAll(verbs);
        if (adjectives != null) globalAdjectives.addAll(adjectives);
        if (properNouns != null) globalProperNouns.addAll(properNouns);

        updateWordFile(NOUNS_FILE, nouns);
        updateWordFile(VERBS_FILE, verbs);
        updateWordFile(ADJECTIVES_FILE, adjectives);
        updateWordFile(PROPER_NOUNS_FILE, properNouns);

        List<String> generated = NonsenseGenerator.generateSentences(nouns, verbs, adjectives, properNouns, count);

        List<String> filtered = new ArrayList<>();
        for (String sentenceOut : generated) {
            try {
                if (ToxicityChecker.isToxic(sentenceOut, apiKey)) {
                    filtered.add("Phrase not shown due to its toxicity");
                } else {
                    filtered.add(sentenceOut);
                }
            } catch (Exception e) {
                filtered.add("Error while checking toxicity");
            }
        }

        return filtered;
    }

    private void updateWordFile(Path filePath, List<String> newWords) throws IOException {
        if (newWords == null || newWords.isEmpty()) return;

        Set<String> existing = new HashSet<>();

        if (Files.exists(filePath)) {
            existing.addAll(Files.readAllLines(filePath).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet()));
        }

        // aggiungi solo parole nuove
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String word : newWords) {
                if (!existing.contains(word)) {
                    writer.write(word);
                    writer.newLine();
                }
            }
        }
    }

    public static Set<String> getGlobalNouns() { return globalNouns; }
    public static Set<String> getGlobalVerbs() { return globalVerbs; }
    public static Set<String> getGlobalAdjectives() { return globalAdjectives; }
    public static Set<String> getGlobalProperNouns() { return globalProperNouns; }

}
