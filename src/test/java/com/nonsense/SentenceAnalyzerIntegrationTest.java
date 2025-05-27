package com.nonsense;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SentenceAnalyzerIntegrationTest {

    @Autowired
    private SentenceAnalyzer analyzer;

    @Test
    public void testAnalyzeSyntax_realGoogleApi() throws Exception {
        String sentence = "The dog runs fast.";
        Map<String, List<String>> result = analyzer.analyzeSyntax(sentence);

        assertNotNull(result);
        assertTrue(result.get("nouns").contains("dog"));
        assertTrue(result.get("verbs").contains("runs"));
        assertTrue(result.get("adjectives").contains("fast"));
    }
}
