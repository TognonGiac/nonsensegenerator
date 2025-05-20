package com.nonsense.service;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.NonsenseGenerator;

import java.io.IOException;
import java.util.*;

public class NonsenseService {

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

        if (nouns != null) globalNouns.addAll(nouns);
        if (verbs != null) globalVerbs.addAll(verbs);
        if (adjectives != null) globalAdjectives.addAll(adjectives);

        return NonsenseGenerator.generateSentences(nouns, verbs, adjectives, count);
    }

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
