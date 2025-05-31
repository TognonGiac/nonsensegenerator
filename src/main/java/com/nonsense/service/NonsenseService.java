package com.nonsense.service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

import com.nonsense.SentenceAnalyzer;
import com.nonsense.NonsenseGenerator;
import com.nonsense.util.ToxicityChecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
* Service Spring per la generazione di frasi nonsense basate sull'analisi sintattica dei testi in input.
* <p> Questo servizio implementa un sistema per: </p>
* <ul>
*    <li>Analizzare le frasi ottenute dall'utente in input</li>
*    <li>Estrarre e classificare parole (nouns, verbs, adjective)</li>
*    <li>Aggiornare i vocabolari con le nuove parole ottenute</li>
*    <li>Filtrare le frasi tossiche utilizzando l'API Google</li>
*    <li>Mostrare in output le nuove frasi nonsense generate</li>
*</ul>
*/
@Service
public class NonsenseService {

    /** 
    * La dipendenza da
    * {@link SentenceAnalyzer} e {@link ToxicityChecker} viene iniettata tramite il costruttore
    * esplicito sottostante. Spring utilizza il costruttore con parametri per creare l'istanza
    * del servizio.
    */
    private final SentenceAnalyzer analyzer;
    private final ToxicityChecker checker;

    public NonsenseService(SentenceAnalyzer analyzer, ToxicityChecker checker) {
        this.analyzer = analyzer;
        this.checker = checker;
    }

    
    /**
    * Vocabolario in memoria per i nomi, condiviso tra tutte le istanze.
    */
    private static final Set<String> globalNouns = new HashSet<>();

    /**
    * Vocabolario in memoria per i verbi, condiviso tra tutte le istanze.
    */
    private static final Set<String> globalVerbs = new HashSet<>();

    /**
    * Vocabolario in memoria per gli aggettivi, condiviso tra tutte le istanze.
    */
    private static final Set<String> globalAdjectives = new HashSet<>();

    /** Percorso del file persistente per l'archivio dei nomi*/
    private static final Path NOUNS_FILE = Paths.get("src/main/resources/data/nouns.txt");
    /** Percorso del file persistente per l'archivio dei verbi*/
    private static final Path VERBS_FILE = Paths.get("src/main/resources/data/verbs.txt");
    /** Percorso del file persistente per l'archivio degli aggettivi*/
    private static final Path ADJECTIVES_FILE = Paths.get("src/main/resources/data/adjectives.txt");

    /**
    * Chiave API di Google Cloud Natural Language.
    *<p>
    * Questa chiave deve essere configurata nel file {@code application.properties}
    * Necessaria per l'analisi sintattica e il controllo della tossicità.
    * </p>
    */
    @Value("${google.api.key}")
    private String apiKey;

    /**
    * Genera frasi nonsense a partire da una frase di input fornita dall'utente.
    * <p> Questo metodo implementa il processo di generazione di una frase nonsense: </p>
    * <ol>
    *    <li><strong>Validazione:</strong> Verifica la presenza della chiave API</li>
    *    <li><strong>Analisi sintattica:</strong> Estrae nomi, verbi e aggettivi dalla frase</li>
    *    <li><strong>Aggiornamento dizionari:</strong> Aggiunge le nuove parole ai set globali</li>
    *    <li><strong>Generazione:</strong> Genera frasi nonsense</li>
    *    <li><strong>Controllo tossicità:</strong> Verifica e filtra le eventuali frasi tossiche.
    *        Le frasi non tossiche vengono salvate su file tramite 
    *        {@link com.nonsense.service.OutputWriter#writeSentence(String)}</li>
    * </ol>
    *
    * @param sentence    frase in input da cui estrarre le componenti sintattiche
    * @param count       numero di frasi nonsense che l'utente vuole generare
    * @return lista di frasi generate 
    * @throws Exception se l'analisi sintattica non va a buon fine o se manca la chiave API 
    */
    public List<String> generateNonsenseSentences(String sentence, int count) throws Exception {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("API key not found: Make sure you have set google.api.key in application.properties");
        }
        
        Map<String, List<String>> parts = analyzer.analyzeSyntax(sentence);

        List<String> nouns = parts.get("nouns");
        List<String> verbs = parts.get("verbs");
        List<String> adjectives = parts.get("adjectives");

        if (nouns != null) globalNouns.addAll(nouns);
        if (verbs != null) globalVerbs.addAll(verbs);
        if (adjectives != null) globalAdjectives.addAll(adjectives);

        updateWordFile(NOUNS_FILE, nouns);
        updateWordFile(VERBS_FILE, verbs);
        updateWordFile(ADJECTIVES_FILE, adjectives);

        List<String> generated = NonsenseGenerator.generateSentences(nouns, verbs, adjectives, count);

        List<String> filtered = new ArrayList<>();
        for (String sentenceOut : generated) {
            try {
                if (checker.isToxic(sentenceOut)) {
                    filtered.add("Sentence not shown due to its toxicity");
                } else {
                    filtered.add(sentenceOut);
                    OutputWriter.writeSentence(sentenceOut);
                }
            } catch (Exception e) {
                filtered.add("Error while checking toxicity");
            }
        }

        return filtered;
    }

    /**
    * Aggiorna un file con le nuove parole, assicurandosi di non avere duplicati.
    * 
    * @param filePath    persorso del dile da aggiornare
    * @param newWords    lista di nuove parole da aggiungere al file
    * @throws IOExcpetion se il file non può essere letto o scritto
    */
    private void updateWordFile(Path filePath, List<String> newWords) throws IOException {
        if (newWords == null || newWords.isEmpty()) return;

        Set<String> existing = new HashSet<>();

        if (Files.exists(filePath)) {
            existing.addAll(Files.readAllLines(filePath).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet()));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String word : newWords) {
                if (!existing.contains(word)) {
                    writer.write(word);
                    writer.newLine();
                }
            }
        }
    }

    /**
    * Restituisce l'insieme dei nomi raccolti finora.
    *
    * <p> Questo vocabolario globale contiene tutti i nomi estratti dalle frasi
    * analizzate dall'avvio dell'applicazione. </p>
    *
    * @return    insieme di nomi
    */
    public static Set<String> getGlobalNouns() { return globalNouns; }

    /**
    * Restituisce l'insieme dei verbi raccolti finora.
    *
    * <p> Questo vocabolario globale contiene tutti i verbi estratti dalle frasi
    * analizzate dall'avvio dell'applicazione. </p>
    *
    * @return    insieme di verbi
    */
    public static Set<String> getGlobalVerbs() { return globalVerbs; }

    /**
    * Restituisce l'insieme degli aggettivi raccolti finora.
    *
    * <p> Questo vocabolario globale contiene tutti gli aggettivi estratti dalle frasi
    * analizzate dall'avvio dell'applicazione. </p>
    *
    * @return    insieme di aggettivi
    */
    public static Set<String> getGlobalAdjectives() { return globalAdjectives; }
}
