package com.nonsense;

import com.nonsense.service.NonsenseService;
import com.nonsense.util.ToxicityChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
    "google.api.key=mock-key"
})
public class NonsenseServiceMockTest {

    @Autowired
    private NonsenseService service;

    @MockBean
    private SentenceAnalyzer analyzer;

    @MockBean
    private ToxicityChecker checker;

    @Test
    public void testGenerateNonsenseSentences_mockedAnalyzer() throws Exception {
        // Simulate syntax analysis result
        when(analyzer.analyzeSyntax("The cat sleeps")).thenReturn(
            of("nouns", of("cat"), "verbs", of("sleeps"), "adjectives", of())
        );

        // Simulate all sentences as non-toxic
        when(checker.isToxic(anyString())).thenReturn(false);

        List<String> sentences = service.generateNonsenseSentences("The cat sleeps", 2);

        assertNotNull(sentences);
        assertEquals(2, sentences.size());
        for (String s : sentences) {
            assertFalse(s.contains("toxic"));
        }
    }
} 
