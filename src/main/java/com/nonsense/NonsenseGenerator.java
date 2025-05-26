package com.nonsense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
* Generatore di frasi nonsense basato su template e dizionari di parole.
*
* <p>
* Questa classe genera frasi nonsense partendo da una frase presa in input da un utente.
* Avviene un analisi sintattica della frase e si aggiungono le parole in dizionari divisi per tipo (nomi, verbi e aggettivi).
* Vengono generate frasi casuali usando parole presenti nella frase in input, aggiungendone altre.
* </p>
*/
public class NonsenseGenerator {

    /** Dizionario dei sostantivi caricato dal file di risorse {@code /data/nouns.txt}
    * <p>
    * Questa lista contiene tutti i sostantivi utilizzabili per la generazione di nuove frasi nonsense.
    * </p>
    */
    private static final List<String> nouns = new ArrayList<>();
   
    /** Dizionario dei verbi caricato dal file di risorse {@code /data/verbs.txt}
    * <p>
    * Questa lista contiene tutti i verbi utilizzabili per la generazione di nuove frasi nonsense.
    * </p>
    */
    private static final List<String> verbs = new ArrayList<>();
    
    /** Dizionario dei sostantivi caricato dal file di risorse {@code /data/adjectives.txt}
    * <p>
    * Questa lista contiene tutti gli aggettivi utilizzabili per la generazione di nuove frasi nonsense.
    * </p>
    */
    private static final List<String> adjectives = new ArrayList<>();
    
    /** Template delle strutture delle frasi caricati dal file {@code /data/sentence_structures.txt}
    * <p>
    * Ogni template è una stringa che definisce la struttura di una frase utilizzando
    * placeholder come {@code [noun]}, {@code [verb]} e {@code [adjective]} che vengono 
    * sostituiti durante la generazione della frase.
    * </p>
    */
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

    /**Costruttore privato per impedire l'istanziazione della classe di utility.*/
    private NonsenseGenerator(){
    }
    
    /** 
    * Blocco di inizializzazione statica per il caricamento dei dizionari e template.
    * <p>
    * Questo blocco viene eseguito automaticamente al primo accesso alla classe
    * e si occupa di caricare tutti i file di risorse necessari per il funzionamento
    * del programma di generazione frasi nonsense. 
    * In caso di errore durante il caricamento, stampa un messaggio
    * di errore.
    * </p>
    *
    * <p> I file caricati, in sequenza, sono: </p>
    * <ol>
    *    <li>Dizionario dei sostantivi</li>
    *    <li>Dizionario dei verbi</li>
    *    <li>Dizionario degli aggettivi</li>
    *    <li>Template delle strutture delle frasi</li>
    * </ol>
    */
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
    * Carica il contenuto di un file di risorse in una lista di stringhe.
    *
    * <p>
    * Questo metodo legge un file di testo dalla directory delle risorse, processando ogni riga come un elemento separato.
    * Le righe vuote o quelle che contengono solo spazi vengono scartate,
    * mentre gli spazi iniziali e finali vengono rimossi dalle righe valide.
    * </p>
    *
    * @param path      percorso del file di risorse relativo al classpath
    * @param target    lista di destinazione in cui inserire le righe lette
    * @throws IOException se si verifica un errore durante la lettura del file
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
    * Genera una lista di frasi nonsense basate sulle parole fornite e sulla quantità specificata.
    *
    * <p>
    * Metodo princpiale della classe che controlla il processo di generazione delle frasi nonsense. 
    * Aggiunge alle parole di base quelle prese dalla frase ottenuta in input, per poi utilizzare i template per creare delle frasi nonsense.
    * </p>
    *
    * @param userNouns            insieme di nomi forniti dall'utente (può essere null)
    * @param userVerbs            insieme di verbi forniti dall'utente (può essere null) 
    * @param userAdjectives       insieme di aggettivi forniti dall'utente (può essere null)
    * @param count                numero di frasi da generare
    * @return lista contenente {@code count} frasi generate casualmente
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
    * Sostituisce i placeholder [noun], [verb], [adjective], [sentence] all'interno di un template
    * con parole scelte casualmente dall'insieme di parole fornite.
    *
    * <p> I placeholder sono: </p>
    * <ul>
    *    <li>{@code [noun]} - che viene sostituito con un sostantivo casuale</li>
    *    <li>{@code [verb]} - che viene sostituito con un verbo casuale</li>
    *    <li>{@code [adjective]} - che viene sostituito con un aggettivo casuale</li>
    *    <li>{@code [sentence]} - che viene sostituito con una mini-frase generata automaticamnte</li>
    * </ul>
    *
    * @param template     stringa template che contiene i placeholder
    * @param nouns        insieme di nomi da pescare casualmente
    * @param verbs        insieme di verbi da pescare casualmente
    * @param adjective    insieme di aggettivi da pescare casualmente
    * @return frase completa con i placeholder sostituiti da parole casuali 
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
    * @param list    lista di parametri da cui selezionare l'elemento. Non può essere {@code null} o vuota
    * @return elemento scelto casualmente dalla lista
    */
    private static String pickRandom(List<String> list) {
        return list.get(rand.nextInt(list.size()));
    }

    /**
    * Genera una mini-frase composta da "The" + aggettivo + sostantivo + verbo.
    * <p> Il metodo implementa un sistema che esclude verbi presenti nella lista proibita per evitare forme grammaticali scorrette. </p>
    *
    * @param nouns        lista dei sostantivi. Non può essere {@code null} o vuota
    * @param verbs        lista dei verbi. Non può essere {@code null} o vuota
    * @param adjective    lista degli aggettivi. Non può essere {@code null} o vuota 
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
