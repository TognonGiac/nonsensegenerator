package com.nonsense.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ToxicityChecker {

    private static final double TOXICITY_THRESHOLD = 0.5;

    public static boolean isToxic(String text, String apiKey) throws Exception {
        String endpoint = "https://language.googleapis.com/v1beta2/documents:moderateText?key=" + apiKey;

        // Prepara il corpo JSON della richiesta
        String body = """
        {
          "document": {
            "type": "PLAIN_TEXT",
            "content": "%s"
          }
        }
        """.formatted(text.replace("\"", "\\\""));

        // Invia la richiesta HTTP POST
        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        // Leggi la risposta JSON
        try (InputStream is = connection.getInputStream();
             Scanner scanner = new Scanner(is).useDelimiter("\\A")) {

            String json = scanner.hasNext() ? scanner.next() : "";
            JSONObject response = new JSONObject(json);

            if (response.has("moderationCategories")) {
                JSONArray categories = response.getJSONArray("moderationCategories");
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject category = categories.getJSONObject(i);
                    double confidence = category.optDouble("confidence", 0.0);
                    if (confidence >= TOXICITY_THRESHOLD) {
                        return true; // Frase tossica
                    }
                }
            }

            return false; // Frase non tossica
        }
    }
}
