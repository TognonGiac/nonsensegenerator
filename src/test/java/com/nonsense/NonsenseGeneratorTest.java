package com.nonsense;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Non utilizza Mockito: tutti i dati sono controllati direttamente nel test.
class NonsenseGeneratorTest {

    // Dizionari fittizi per il test
    static List<String> testNouns = List.of("dog", "cat", "tree");
    static List<String> testVerbs = List.of("eats", "jumps", "runs");
    static List<String> testAdjectives = List.of("happy", "sad", "angry");
    static List<String> testTemplates = List.of(
        "The [noun] [verb] the [adjective] [noun] and [sentence]"
    );

    @BeforeAll
    static void setupTemplates() throws Exception {
        // Ottiene il campo "templates" dalla classe NonsenseGenerator (che è privato e statico)
        Field templatesField = NonsenseGenerator.class.getDeclaredField("templates");
        templatesField.setAccessible(true); // Permette di accedere anche se il campo è privato

        // Prende la lista interna e la svuota
        List<String> templatesList = (List<String>) templatesField.get(null);
        templatesList.clear();

        // Aggiunge i template finti di test (definiti sopra)
        templatesList.addAll(testTemplates);
    }

    @Test
    void testGenerateSentences_returnsCorrectNumber() {
        // Genera 3 frasi usando solo i dizionari di test
        List<String> result = NonsenseGenerator.generateSentences(
            List.copyOf(testNouns),
            List.copyOf(testVerbs),
            List.copyOf(testAdjectives),
            3
        );
        
        // Verifica che la lista contenga esattamente 3 frasi
        assertEquals(3, result.size(), "3 sentences should be generated");
    }

    @Test
    void testGenerateSentences_containsExpectedWords() {
        // Genera 5 frasi usando i dizionari di test
        List<String> result = NonsenseGenerator.generateSentences(testNouns, testVerbs, testAdjectives, 5);

        // Unisce tutte le frasi in una singola stringa per cercare più facilmente
        String allSentences = String.join(" ", result);

        // Verifica che almeno un sostantivo di test sia presente
        assertTrue(allSentences.contains("dog") || allSentences.contains("cat") || allSentences.contains("tree"));

        // Verifica che almeno un verbo di test sia presente
        assertTrue(allSentences.contains("eats") || allSentences.contains("jumps") || allSentences.contains("runs"));

        // Verifica che almeno un aggettivo di test sia presente
        assertTrue(allSentences.contains("happy") || allSentences.contains("sad") || allSentences.contains("angry"));
    }

    @Test
    void testGenerateSentences_withNullInputsReturnsEmptyList() {
        // Prova a generare frasi passando null come input (nessuna parola utente)
        List<String> result = NonsenseGenerator.generateSentences(null, null, null, 1);

        // Verifica che il risultato sia una lista vuota, cioè nessuna frase generata
        assertTrue(result.isEmpty(), "With null input no sentences should be generated");
    }

}
