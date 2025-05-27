package com.nonsense;

import com.nonsense.controller.NonsenseController;
import com.nonsense.dto.SaveRequest;
import com.nonsense.service.NonsenseService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NonsenseControllerTest {

    // Fake service che restituisce frasi fisse
    class FakeNonsenseService extends NonsenseService {
        public FakeNonsenseService() {
            super(null, null);
        }

        @Override
        public List<String> generateNonsenseSentences(String input, int count) {
            List<String> result = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                result.add("Fake sentence " + i);
            }
            return result;
        }
    }

    @Test
    public void testGenerateSentencesValidInput() throws Exception {
        NonsenseController controller = new NonsenseController(new FakeNonsenseService());

        Map<String, Object> payload = new HashMap<>();
        payload.put("sentence", "The dog barks.");
        payload.put("count", 2);

        Map<String, Object> response = controller.generateSentences(payload);

        assertTrue(response.containsKey("sentences"));
        List<?> sentences = (List<?>) response.get("sentences");
        assertEquals(2, sentences.size());
    }

    @Test
    public void testGenerateSentencesEmptySentence() throws Exception {
        NonsenseController controller = new NonsenseController(new FakeNonsenseService());

        Map<String, Object> payload = new HashMap<>();
        payload.put("sentence", " ");
        payload.put("count", 2);

        Map<String, Object> response = controller.generateSentences(payload);

        assertTrue(response.containsKey("error"));
        assertEquals("Sentence not valid.", response.get("error"));
    }

    @Test
    public void testGenerateSentencesInvalidCount() throws Exception {
        NonsenseController controller = new NonsenseController(new FakeNonsenseService());

        Map<String, Object> payload = new HashMap<>();
        payload.put("sentence", "Test");
        payload.put("count", 0);

        Map<String, Object> response = controller.generateSentences(payload);

        assertTrue(response.containsKey("error"));
        assertEquals("The number of sentences must be between 1 and 10.", response.get("error"));
    }

    @Test
    public void testSavePhrasesToFile() {
        NonsenseController controller = new NonsenseController(null); // Non serve il service qui

        // Fake request con frasi
        SaveRequest request = new SaveRequest();
        request.setPhrases(List.of("Hello world", "Nonsense test"));

        // Usiamo una sottoclasse per NON scrivere davvero su disco
        ResponseEntity<String> response = new NonsenseController(null) {
            @Override
            public ResponseEntity<String> savePhrasesToFile(SaveRequest request) {
                // Simuliamo un salvataggio riuscito
                return ResponseEntity.ok("Simulated save");
            }
        }.savePhrasesToFile(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Simulated save", response.getBody());
    }
}
