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

    static {
        try {
            loadList("/data/nouns.txt", nouns);
            loadList("/data/verbs.txt", verbs);
            loadList("/data/adjectives.txt", adjectives);
            loadList("/data/sentence_structures.txt", templates);
        } catch (IOException e) {
            System.err.println("Errore nel caricamento dei file di parole/template: " + e.getMessage());
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
        return template
                .replaceFirst("\\[noun\\]", pickRandom(nouns))
                .replaceFirst("\\[verb\\]", pickRandom(verbs))
                .replaceFirst("\\[adjective\\]", pickRandom(adjectives))
                .replaceFirst("\\[noun\\]", pickRandom(nouns))
                .replaceFirst("\\[adjective\\]", pickRandom(adjectives))
                .replaceFirst("\\[noun\\]", pickRandom(nouns));
    }

    private static String pickRandom(List<String> list) {
        return list.get(rand.nextInt(list.size()));
    }
}
