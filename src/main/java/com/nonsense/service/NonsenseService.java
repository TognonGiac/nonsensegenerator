package com.nonsense.service;

import com.nonsense.SentenceAnalyzer;

import java.io.IOException;
import java.util.*;

public class NonsenseService {

    private static final List<String> templates = List.of(
        "Il %s %s il %s.",
        "Un %s molto %s può %s.",
        "Perché il %s ha deciso di %s il %s?"
    );

    private static final Set<String> globalNouns = new HashSet<>();
    private static final Set<String> globalVerbs = new HashSet<>();
    private static final Set<String> globalAdjectives = new HashSet<>();

    public List<String> generateNonsenseSentences(String sentence, int count) throws IOException {
        SentenceAnalyzer analyzer = new SentenceAnalyzer();
        Map<String, List<String>> parts = analyzer.analyzeSyntax(sentence);
        analyzer.close();

        List<String> nouns = parts.get("nouns");
        List<String> verbs = parts.get("verbs");
        List<String> adjectives = parts.get("adjectives");

        // Aggiungi le nuove parole ai dizionari globali
        if (nouns != null) globalNouns.addAll(nouns);
        if (verbs != null) globalVerbs.addAll(verbs);
        if (adjectives != null) globalAdjectives.addAll(adjectives);

        List<String> generated = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            String template = templates.get(rand.nextInt(templates.size()));
            String phrase = String.format(
                template,
                getRandomOrDefault(nouns, "gatto"),
                getRandomOrDefault(verbs, "mangia"),
                getRandomOrDefault(adjectives, "rosso")
            );
            generated.add(phrase);
        }

        return generated;
    }

    private String getRandomOrDefault(List<String> list, String fallback) {
        if (list == null || list.isEmpty()) return fallback;
        return list.get(new Random().nextInt(list.size()));
    }

    // Opzionale: getter per dizionari
    public static Set<String> getGlobalNouns() {
        return globalNouns;
    }

    public static Set<String> getGlobalVerbs() {
        return globalVerbs;
    }

    public static Set<String> getGlobalAdjectives() {
        return globalAdjectives;
    }
}
