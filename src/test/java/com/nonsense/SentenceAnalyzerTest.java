package com.nonsense;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SentenceAnalyzerTest {

    @Test
    public void testAnalyzeSyntax() throws IOException {
        SentenceAnalyzer analyzer = new SentenceAnalyzer();
        String sentence = "The dog eats the delicious food.";

        Map<String, List<String>> result = analyzer.analyzeSyntax(sentence);

        assertTrue(result.get("nouns").contains("dog"));
        assertTrue(result.get("verbs").contains("eats"));
        assertTrue(result.get("adjectives").contains("delicious"));

        analyzer.close();
    }
}
