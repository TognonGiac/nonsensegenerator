package com.nonsense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
* Classe che genera frasi nonsense.
* Data una frasi presa in input da un utente, carica dizionari di nomi, verbi e aggettivi.
* Genera frasi casuali usando parole presenti nella frase in input, aggiungendone altre.
*/
public class NonsenseGenerator {

    /** Liste contententi parole e template caricati dai file di risorse */
    private static final List<String> nouns = new ArrayList<>();
    private static final List<String> verbs = new ArrayList<>();
    private static final List<String> adjectives = new ArrayList<>();
    private static final List<String> templates = new ArrayList<>();
    
    /** Generatore di numeri casuali per selezioni random */
    private static final Random rand = new Random();

    /** Insieme di verbi proibiti nelle mini-frasi generate internamente,
    * così da evitare delle strutture grammaticali non desiderate. 
    */
    private static final Set<String> forbiddenMiniSentenceVerbs = Set.of(
        "is", "are", "was", "were", "has", "have", "had",
        "can", "could", "shall", "should", "will", "would",
        "does", "do", "did",
        "likes", "loves", "hates", "wants", "needs", "sees",
        "thinks", "believes", "knows", "hears", "feels",
        "tries", "hopes", "helps", "seems", "appears", "becomes"
    );

    //blocco statico che carica le liste di parole e template all'avvio della classe
    static {
        try {
            loadList("/data/nouns.txt", nouns);
            loadList("/data/verbs.txt", verbs);
            loadList("/data/adjectives.txt", adjectives);
            loadList("/data/sentence_structures.txt", templates);
        } catch (IOException e) {
            System.err.println("Error loading word/template files: " + e.getMessage());
        }
    }

    /**
    * Carica da un file di risorse una lista di stringhe (una per riga) e le aggiunge alla lista target.
    * 
    * @param path : percorso del file di risorse
    * @param target : lista di destinazione in cui inserire le righe lette
    * @throws IOException se si verifica un errore di lettura
    */
    private static void loadList(String path, List<String> target) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(NonsenseGenerator.class.getResourceAsStream(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) target.add(line.trim());
            }
        }
    }
    
    /**
    * Genera una lista di frasi nonsense basate sulle parole e sulla quantità specificata.
    * Aggiunge le parole di base a quelle prese dalla frase ottenuta in input.
    * 
    * @param userNouns : insieme di nomi forniti dall'utente (può essere null)
    * @param userVerbs : insieme di verbi forniti dall'utente (può essere null) 
    * @param userAdjectives : insieme di aggettivi forniti dall'utente (può essere null)
    * @param count : numero di frasi da generare
    * @return lista di frasi generate casualmente
    */
    public static List<String> generateSentences(
        List<String> userNouns,
        List<String> userVerbs,
        List<String> userAdjectives,
        int count
    ) {
        // Crea copie delle liste complete (dizionari base)
        List<String> allNouns = new ArrayList<>(nouns);
        List<String> allVerbs = new ArrayList<>(verbs);
        List<String> allAdjectives = new ArrayList<>(adjectives);

        // Aggiungi parole dall’utente
        if (userNouns != null) allNouns.addAll(userNouns);
        if (userVerbs != null) allVerbs.addAll(userVerbs);
        if (userAdjectives != null) allAdjectives.addAll(userAdjectives);

        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String template = templates.get(rand.nextInt(templates.size()));
            String sentence = fillTemplate(template, allNouns, allVerbs, allAdjectives);
            results.add(sentence);
        }

        return results;
    }

    /**
    * Riemepie un template sostituendo i placeholder [noun], [verb], [adjective], [sentence]
    * con parole scelte casualmente dall'insieme di parole fornite.
    * 
    * @param template : stringa template che contiene i placeholder
    * @param nouns : insieme di nomi da cui pescare casualmente
    * @param verbs : insieme di verbi da cui perscare casualmente
    * @param adjective : insieme di aggettivi da cui perscare casualmente
    * @return frase generata random 
    */
    private static String fillTemplate(String template, List<String> nouns, List<String> verbs, List<String> adjectives) {
        String sentence = template;
        sentence = sentence.replaceFirst("\\[noun\\]", pickRandom(nouns));
        sentence = sentence.replaceFirst("\\[verb\\]", pickRandom(verbs));
        sentence = sentence.replaceFirst("\\[adjective\\]", pickRandom(adjectives));
        sentence = sentence.replaceFirst("\\[noun\\]", pickRandom(nouns));
        sentence = sentence.replaceFirst("\\[adjective\\]", pickRandom(adjectives));
        sentence = sentence.replaceFirst("\\[noun\\]", pickRandom(nouns));
        
        // nuova gestione per [sentence], genera mini-frasi
        if (sentence.contains("[sentence]")) {
            sentence = sentence.replace("[sentence]", generateMiniSentence(nouns, verbs, adjectives));
        }
    
        return sentence;
    }

    /**
    * Restituisce un elemento casuale dalla lista fornita.
    *
    * @param list : lista di elementi
    * @return elemento scelto casualmente
    */
    private static String pickRandom(List<String> list) {
        return list.get(rand.nextInt(list.size()));
    }

    /**
    * Genera una mini frase composta da "The" + aggettivo + sostantivo + verbo.
    * Esclude verbi presenti nella lista proibita per evitare forme grammaticali scorrette.
    *
    * @param nouns : insieme di sostantivi
    * @param verbs : insieme di verbi 
    * @param adjective : insieme di aggettivi
    * @return mini-frase generata casualmente
    */
    private static String generateMiniSentence(List<String> nouns, List<String> verbs, List<String> adjectives) {
        String verb;
        do {
            verb = pickRandom(verbs);
        } while (forbiddenMiniSentenceVerbs.contains(verb));

        return "the " + pickRandom(adjectives) + " " + pickRandom(nouns) + " " + verb;
    }
}
