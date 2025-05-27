package com.nonsense;

import org.junit.jupiter.api.Test;

import java.util.List;

import com.nonsense.dto.SaveRequest;

import static org.junit.jupiter.api.Assertions.*;

public class SaveRequestTest {

    @Test
    public void testSetAndGetPhrases() {
        SaveRequest request = new SaveRequest();

        List<String> inputPhrases = List.of("Frase uno", "Frase due", "Frase tre");
        request.setPhrases(inputPhrases);

        List<String> result = request.getPhrases();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Frase uno", result.get(0));
        assertEquals("Frase tre", result.get(2));
    }

    @Test
    public void testDefaultConstructorInitialState() {
        SaveRequest request = new SaveRequest();

        // Dopo il costruttore vuoto, phrases dovrebbe essere null
        assertNull(request.getPhrases());
    }
}
