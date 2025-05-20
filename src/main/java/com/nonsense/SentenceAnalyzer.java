package com.nonsense;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SentenceAnalyzer {

    private final String apiKey;

    public SentenceAnalyzer(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key non valida");
        }
        this.apiKey = apiKey;
    }

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

        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        InputStream is = connection.getInputStream();
        String json;
        try (Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            json = scanner.hasNext() ? scanner.next() : "";
        }

        JSONObject response = new JSONObject(json);
        JSONArray tokens = response.getJSONArray("tokens");

        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

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

    public void close() {
        // Nessuna risorsa da chiudere nel caso REST
    }
}

