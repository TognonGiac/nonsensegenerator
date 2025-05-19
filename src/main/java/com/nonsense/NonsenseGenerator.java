package com.nonsense;

import java.util.*;

public class NonsenseGenerator {

    private static final List<String> NOUNS_EXTRA = List.of("dragon", "pizza", "toaster", "mountain", "penguin");
    private static final List<String> VERBS_EXTRA = List.of("jumps", "dances", "sings", "crashes", "melts");
    private static final List<String> ADJECTIVES_EXTRA = List.of("weird", "purple", "noisy", "giant", "soft");

    private static final List<String> TEMPLATES = List.of(
        "The [noun] [verb] the [adjective] [noun] in a [adjective] [noun].",
        "A [adjective] [noun] [verb] over the [adjective] [noun].",
        "In a [adjective] [noun], the [noun] [verb] the [adjective] [noun]."
    );

    public static List<String> generateSentences(List<String> nouns, List<String> verbs, List<String> adjectives, int count) {
        Random rand = new Random();
        List<String> allNouns = new ArrayList<>(nouns);
        List<String> allVerbs = new ArrayList<>(verbs);
        List<String> allAdjectives = new ArrayList<>(adjectives);

        allNouns.addAll(NOUNS_EXTRA);
        allVerbs.addAll(VERBS_EXTRA);
        allAdjectives.addAll(ADJECTIVES_EXTRA);

        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String template = TEMPLATES.get(rand.nextInt(TEMPLATES.size()));
            String sentence = template
                    .replaceFirst("\\[noun\\]", pickRandom(allNouns, rand))
                    .replaceFirst("\\[verb\\]", pickRandom(allVerbs, rand))
                    .replaceFirst("\\[adjective\\]", pickRandom(allAdjectives, rand))
                    .replaceFirst("\\[noun\\]", pickRandom(allNouns, rand))
                    .replaceFirst("\\[adjective\\]", pickRandom(allAdjectives, rand))
                    .replaceFirst("\\[noun\\]", pickRandom(allNouns, rand));
            results.add(sentence);
        }
        return results;
    }

    private static String pickRandom(List<String> list, Random rand) {
        return list.get(rand.nextInt(list.size()));
    }
}
