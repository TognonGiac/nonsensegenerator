package com.nonsense;

import org.junit.jupiter.api.Test;

import com.nonsense.util.ToxicityChecker;

import static org.junit.jupiter.api.Assertions.*;

// Test base con checker finto per evitare chiamate reali all'API
public class ToxicityCheckerTest {

    // Fake checker: simula la risposta dell'API
    class FakeToxicityChecker extends ToxicityChecker {
        @Override
        public boolean isToxic(String sentence) {
            // Simuliamo una frase tossica
            if (sentence.equalsIgnoreCase("You are stupid.")) {
                return true;
            }
            return false; // Tutto il resto è considerato non tossico
        }
    }

    @Test
    public void testFraseNonTossica() throws Exception {
        ToxicityChecker checker = new FakeToxicityChecker();
        boolean result = checker.isToxic("Have a nice day!");

        assertFalse(result); // La frase è educata → non tossica
    }

    @Test
    public void testFraseTossica() throws Exception {
        ToxicityChecker checker = new FakeToxicityChecker();
        boolean result = checker.isToxic("You are stupid.");

        assertTrue(result); // Frase offensiva → tossica
    }

    @Test
    public void testFraseVuota() throws Exception {
        ToxicityChecker checker = new FakeToxicityChecker();
        boolean result = checker.isToxic("");

        assertFalse(result); // Non c'è nulla da analizzare → considerata non tossica
    }

    @Test
    public void testSoloSimboli() throws Exception {
        ToxicityChecker checker = new FakeToxicityChecker();
        boolean result = checker.isToxic("!!! ???");

        assertFalse(result); // Solo simboli → considerata non tossica
    }
}
