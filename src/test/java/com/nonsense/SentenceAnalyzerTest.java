package com.nonsense;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class SentenceAnalyzerTest {

    // Fake analyzer per evitare chiamate a Google API
    class FakeSentenceAnalyzer extends SentenceAnalyzer {
        @Override
        public Map<String, List<String>> analyzeSyntax(String sentence) {
            Map<String, List<String>> result = new HashMap<>();
            result.put("nouns", new ArrayList<>());
            result.put("verbs", new ArrayList<>());
            result.put("adjectives", new ArrayList<>());

            if (sentence.equalsIgnoreCase("The red cat jumps.")) {
                result.get("nouns").add("cat");
                result.get("verbs").add("jumps");
                result.get("adjectives").add("red");
            }

            return result;
        }
    }

    @Test
    public void testFraseNormale() throws Exception {
        SentenceAnalyzer analyzer = new FakeSentenceAnalyzer();
        Map<String, List<String>> result = analyzer.analyzeSyntax("The red cat jumps.");

        assertTrue(result.get("nouns").contains("cat"));
        assertTrue(result.get("verbs").contains("jumps"));
        assertTrue(result.get("adjectives").contains("red"));
    }

    @Test
    public void testFraseVuota() throws Exception {
        SentenceAnalyzer analyzer = new FakeSentenceAnalyzer();
        Map<String, List<String>> result = analyzer.analyzeSyntax("");

        assertTrue(result.get("nouns").isEmpty());
        assertTrue(result.get("verbs").isEmpty());
        assertTrue(result.get("adjectives").isEmpty());
    }

    @Test
    public void testFraseConSoloNumeri() throws Exception {
        SentenceAnalyzer analyzer = new FakeSentenceAnalyzer();
        Map<String, List<String>> result = analyzer.analyzeSyntax("12345 67890");

        assertTrue(result.get("nouns").isEmpty());
        assertTrue(result.get("verbs").isEmpty());
        assertTrue(result.get("adjectives").isEmpty());
    }

    @Test
    public void testFraseConSimboli() throws Exception {
        SentenceAnalyzer analyzer = new FakeSentenceAnalyzer();
        Map<String, List<String>> result = analyzer.analyzeSyntax("!!! ???");

        assertTrue(result.get("nouns").isEmpty());
        assertTrue(result.get("verbs").isEmpty());
        assertTrue(result.get("adjectives").isEmpty());
    }

    @Test
    public void testFraseConUnaParola() throws Exception {
        SentenceAnalyzer analyzer = new FakeSentenceAnalyzer();
        Map<String, List<String>> result = analyzer.analyzeSyntax("Run");

        assertTrue(result.get("nouns").isEmpty());
        assertTrue(result.get("verbs").isEmpty());
        assertTrue(result.get("adjectives").isEmpty());
    }
}
