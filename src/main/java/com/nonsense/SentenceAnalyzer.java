package com.nonsense;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
* Classe che analizza la sintassi di una frase in input con l'aiuto dell'API di Google atural Language.
* Estrae sostantivi, verbi e aggettivi.
*/
public class SentenceAnalyzer {

    /** Chiave API per accedere a Google Cloud Natural Language API*/
    private final String apiKey;

    /** 
    * Costruttore della classe SentenceAnalyzer
    *
    * @param apiKey : chiave API di Google, non può essere null o vuota.
    * @throws IllegalArgumentException se la chiave è null o vuota
    */
    public SentenceAnalyzer(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Invalid API key");
        }
        this.apiKey = apiKey;
    }

    /**
    * Utilizzando Google Natural Language API, analizza la sintassi di una frase 
    * classificando le parole contenute al suo interno in nomi, verbi e aggettivi.
    *
    * @param text : frase in input da analizzare
    * @return mappa contentente tre liste -> "nouns", "verbs" e "adjectives"
    * @throws Exception in caso di errore durante la richiesta http o parsing JSON
    */
    public Map<String, List<String>> analyzeSyntax(String text) throws Exception {
        String endpoint = "https://language.googleapis.com/v1/documents:analyzeSyntax?key=" + apiKey;

        String body = """
        {
          "document": {
            "type": "PLAIN_TEXT",
            "content": "%s"
          },
          "encodingType": "UTF8"
        }
        """.formatted(text.replace("\"", "\\\""));

        // Configura la connessione HTTP
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // Invia la richiesta con il contenuto JSON
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        // Legge la risposta JSON
        InputStream is = connection.getInputStream();
        String json;
        try (Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            json = scanner.hasNext() ? scanner.next() : "";
        }

        // Parsing della risposta JSON
        JSONObject response = new JSONObject(json);
        JSONArray tokens = response.getJSONArray("tokens");

        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        // Estrae le parole in base alla loro lista di appartenenza
        for (int i = 0; i < tokens.length(); i++) {
            JSONObject token = tokens.getJSONObject(i);
            String word = token.getJSONObject("text").getString("content");
            String tag = token.getJSONObject("partOfSpeech").getString("tag");

            switch (tag) {
                case "NOUN" -> nouns.add(word);
                case "VERB" -> verbs.add(word);
                case "ADJ"  -> adjectives.add(word);
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("nouns", nouns);
        result.put("verbs", verbs);
        result.put("adjectives", adjectives);
        return result;
    }

    /** Metodo close anche se in questo caso non ci sono risorse da chiudere */
    public void close() {
        // Nessuna risorsa da chiudere nel caso REST
    }
}
