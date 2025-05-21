package com.nonsense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NonsenseGenerator {

    private static final List<String> nouns = new ArrayList<>();
    private static final List<String> verbs = new ArrayList<>();
    private static final List<String> adjectives = new ArrayList<>();
    private static final List<String> templates = new ArrayList<>();
    private static final Random rand = new Random();

    private static final Set<String> forbiddenMiniSentenceVerbs = Set.of(
        "is", "are", "was", "were", "has", "have", "had",
        "can", "could", "shall", "should", "will", "would",
        "does", "do", "did",
        "likes", "loves", "hates", "wants", "needs", "sees",
        "thinks", "believes", "knows", "hears", "feels",
        "tries", "hopes", "helps", "seems", "appears", "becomes"
    );

    static {
        try {
            loadList("/data/nouns.txt", nouns);
            loadList("/data/verbs.txt", verbs);
            loadList("/data/adjectives.txt", adjectives);
            loadList("/data/sentence_structures.txt", templates);
        } catch (IOException e) {
            System.err.println("Error loading word/template files: " + e.getMessage());
        }
    }

    private static void loadList(String path, List<String> target) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(NonsenseGenerator.class.getResourceAsStream(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) target.add(line.trim());
            }
        }
    }

    public static List<String> generateSentences(
        List<String> userNouns,
        List<String> userVerbs,
        List<String> userAdjectives,
        int count
    ) {
        // Crea copie delle liste complete (dizionari base)
        List<String> allNouns = new ArrayList<>(nouns);
        List<String> allVerbs = new ArrayList<>(verbs);
        List<String> allAdjectives = new ArrayList<>(adjectives);

        // Aggiungi parole dall’utente
        if (userNouns != null) allNouns.addAll(userNouns);
        if (userVerbs != null) allVerbs.addAll(userVerbs);
        if (userAdjectives != null) allAdjectives.addAll(userAdjectives);

        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String template = templates.get(rand.nextInt(templates.size()));
            String sentence = fillTemplate(template, allNouns, allVerbs, allAdjectives);
            results.add(sentence);
        }

        return results;
    }

    private static String fillTemplate(String template, List<String> nouns, List<String> verbs, List<String> adjectives) {
        String sentence = template;
        sentence = sentence.replaceFirst("\\[noun\\]", pickRandom(nouns));
        sentence = sentence.replaceFirst("\\[verb\\]", pickRandom(verbs));
        sentence = sentence.replaceFirst("\\[adjective\\]", pickRandom(adjectives));
        sentence = sentence.replaceFirst("\\[noun\\]", pickRandom(nouns));
        sentence = sentence.replaceFirst("\\[adjective\\]", pickRandom(adjectives));
        sentence = sentence.replaceFirst("\\[noun\\]", pickRandom(nouns));
        
        // nuova gestione per [sentence]
        if (sentence.contains("[sentence]")) {
            sentence = sentence.replace("[sentence]", generateMiniSentence(nouns, verbs, adjectives));
        }
    
        return sentence;
    }

    private static String pickRandom(List<String> list) {
        return list.get(rand.nextInt(list.size()));
    }

    private static String generateMiniSentence(List<String> nouns, List<String> verbs, List<String> adjectives) {
        String verb;
        do {
            verb = pickRandom(verbs);
        } while (forbiddenMiniSentenceVerbs.contains(verb));

        return "the " + pickRandom(adjectives) + " " + pickRandom(nouns) + " " + verb;
    }
}
