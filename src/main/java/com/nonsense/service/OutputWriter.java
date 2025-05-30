package com.nonsense.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {

    private static final String FILE_PATH = "src/main/output.txt"; // file nella root del progetto

    // Metodo statico che scrive una singola frase nel file
    public static void writeSentence(String sentence) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(sentence);
            writer.newLine(); // va a capo dopo ogni frase
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }
}

