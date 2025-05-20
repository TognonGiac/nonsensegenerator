package com.nonsense.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class ToxicityChecker {

    /**
     * Restituisce true se la frase è considerata tossica da Google ModerateText API
     */
    public static boolean isToxic(String text, String apiKey) throws Exception {
        String endpoint = "https://language.googleapis.com/v1beta2/documents:moderateText?key=" + apiKey;

        // Escape per evitare problemi con doppi apici
        String safeText = text.replace("\"", "\\\"");

        String body = """
        {
          "document": {
            "type": "PLAIN_TEXT",
            "content": "%s"
          }
        }
        """.formatted(safeText);

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
