package com.nonsense;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Carica .env e imposta variabile per Google
        Dotenv dotenv = Dotenv.load();
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", dotenv.get("GOOGLE_APPLICATION_CREDENTIALS"));

        try {
            SentenceAnalyzer analyzer = new SentenceAnalyzer();
            //String input = "The quick brown fox jumps over the lazy dog.";

            //Map<String, List<String>> result = analyzer.analyzeSyntax(input);

            System.out.println("Nouns: " + result.get("nouns"));
            System.out.println("Verbs: " + result.get("verbs"));
            System.out.println("Adjectives: " + result.get("adjectives"));

            analyzer.close();
        } catch (IOException e) {
            System.err.println("Errore durante l'analisi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
