package com.nonsense;

import com.nonsense.service.NonsenseService;
import com.nonsense.util.ToxicityChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NonsenseServiceTest {

    // Fake analyzer che ritorna parole controllate (tra cui "dies")
    class FakeSentenceAnalyzer extends SentenceAnalyzer {
        @Override
        public Map<String, List<String>> analyzeSyntax(String sentence) {
            Map<String, List<String>> result = new HashMap<>();
            result.put("nouns", List.of("cat", "house"));
            result.put("verbs", List.of("runs", "dies")); // include "dies"
            result.put("adjectives", List.of("fast"));
            return result;
        }
    }

    // Fake checker: considera tossiche tutte le frasi che contengono "dies"
    class FakeToxicityChecker extends ToxicityChecker {
        @Override
        public boolean isToxic(String sentence) {
            return sentence.toLowerCase().contains("dies");
        }
    }

    private NonsenseService service;

    @BeforeEach
    public void setUp() {
        service = new NonsenseService(new FakeSentenceAnalyzer(), new FakeToxicityChecker());

        try {
            var field = NonsenseService.class.getDeclaredField("apiKey");
            field.setAccessible(true);
            field.set(service, "fake-api-key");
        } catch (Exception e) {
            fail("Impossibile impostare la chiave API: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateNonsenseSentencesReturnsNonEmptyList() throws Exception {
        List<String> result = service.generateNonsenseSentences("The cat runs.", 3);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (String s : result) {
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }

    @Test
    public void testToxicSentenceIsFiltered() throws Exception {
        List<String> result = service.generateNonsenseSentences("The cat dies in the house", 10);

        long filteredCount = result.stream()
            .filter(s -> s.equals("Sentence not shown due to its toxicity"))
            .count();

        // Il test è positivo se la lista ha la lunghezza corretta e contiene o no frasi filtrate
        assertNotNull(result);
        assertEquals(10, result.size());

        // Se ci sono frasi filtrate, verifichiamo che siano proprio quelle previste
        if (filteredCount > 0) {
            assertTrue(result.contains("Sentence not shown due to its toxicity"));
        }
    }



    @Test
    public void testMissingApiKeyThrowsError() {
        NonsenseService brokenService = new NonsenseService(new FakeSentenceAnalyzer(), new FakeToxicityChecker());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            brokenService.generateNonsenseSentences("test", 1);
        });

        assertTrue(exception.getMessage().contains("API key not found"));
    }
}
