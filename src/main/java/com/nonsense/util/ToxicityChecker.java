package com.nonsense.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
* Utility class per il controllo della tossicità del testo utilizzando l'API Google Cloud Natural Language.
* <p>
* Questa classe fornisce dei metodi per analizzare il contenuto testuale e determianre se contiene
* linguaggio tossico utilizzando il servizio ModerateText di Google.
*/
public class ToxicityChecker {

    /** Costruttore privato per evitare l'istanziazione della classe utility.*/
    private ToxicityChecker(){
        //previene l'istanzazione
    }
    
    /**
     * Verifica se la frase è considerata tossica utilizzando l'API Google Cloud Natural Language.
     *
     * <p>
     * Il metodo invia una richiesta HTTP POST all'endpoint ModerateText di Google e analizza
     * la risposta per determinare se il contenuto della frase supera la soglia di tossicità.
     * Un testo è considerato tossico se almeno una delle categorie di moderazione
     * restituisce un livello di confidenza >= 0.5.
     * </p>
     * 
     * @param text          frase da analizzare
     * @param apiKey        chiave API Google valida per chiamare il servizio
     * @return true se la frase è considerata tossica con confidenza >= 0.5, false altrimenti
     * @throws Exception in caso di errori nella connessione o nella risposta dell'API
     */
    public static boolean isToxic(String text, String apiKey) throws Exception {
        String endpoint = "https://language.googleapis.com/v1beta2/documents:moderateText?key=" + apiKey;

        // Escape per evitare problemi con doppi apici
        String safeText = text.replace("\"", "\\\"");

        // Corpo della richiesta JSON
        String body = """
        {
          "document": {
            "type": "PLAIN_TEXT",
            "content": "%s"
          }
        }
        """.formatted(safeText);

        // Apertura della connessione HTTP POST
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // Invia il body JSON
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        int responseCode = connection.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300)
                ? connection.getInputStream()
                : connection.getErrorStream();

        // Lettura della risposta JSON
        try (Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            String json = scanner.hasNext() ? scanner.next() : "";

            JSONObject response = new JSONObject(json);
            if (!response.has("moderationCategories")) return false;

            JSONArray categories = response.getJSONArray("moderationCategories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
                double confidence = category.optDouble("confidence", 0.0);
                if (confidence >= 0.5) {
                    return true; // tossica
                }
            }

            return false; // non tossica
        }
    }
}
