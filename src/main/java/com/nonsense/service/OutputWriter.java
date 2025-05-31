package com.nonsense.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe OutputWriter si occupa della scrittura su file delle frasi generate
 * dal sistema. Permette di abilitare o disabilitare la scrittura tramite una
 * variabile statica {@code ENABLED}, utile per i test.
 */
public class OutputWriter {

    /**
     * Percorso del file di output. Il file si trova nella directory src/main.
     */
    private static final String FILE_PATH = "src/main/output.txt";

    /**
     * Flag pubblico per abilitare o disabilitare la scrittura del file.
     * Può essere settato a false durante i test automatici.
     */
    public static boolean ENABLED = true;

    /**
     * Scrive una singola frase nel file specificato da {@code FILE_PATH}.
     * Ogni frase viene scritta su una nuova riga.
     *
     * @param sentence la frase da scrivere nel file
     */
    public static void writeSentence(String sentence) {
        if (!ENABLED) return; // non scrive nulla se disabilitato
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(sentence);
            writer.newLine(); // aggiunge un ritorno a capo dopo ogni frase
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }
}

