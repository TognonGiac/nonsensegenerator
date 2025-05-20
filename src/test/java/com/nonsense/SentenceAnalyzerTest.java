package com.nonsense;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SentenceAnalyzerTest {

    @Value("${google.api.key}")
    private String apiKey;

    @Test
    public void testAnalyzeSyntax() throws Exception {
        SentenceAnalyzer analyzer = new SentenceAnalyzer(apiKey);
        String sentence = "The dog eats the delicious food.";

        Map<String, List<String>> result = analyzer.analyzeSyntax(sentence);

        assertNotNull(result);
        assertTrue(result.get("nouns").contains("dog"));
        assertTrue(result.get("verbs").contains("eats"));
        assertTrue(result.get("adjectives").contains("delicious"));

        analyzer.close();
    }
}
