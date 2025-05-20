package com.nonsense.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ToxicityChecker {

    public static boolean isToxic(String text, String apiKey) throws Exception {
        String endpoint = "https://language.googleapis.com/v1beta2/documents:moderateText?key=" + apiKey;

        String body = """
        {
          "document": {
            "type": "PLAIN_TEXT",
            "content": "%s"
          }
        }
        """.formatted(text.replace("\"", "\\\""));

        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        try (InputStream is = connection.getInputStream();
             Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            String json = scanner.hasNext() ? scanner.next() : "";
            return json.contains("\"confidence\":") && !json.contains("\"confidence\": 0.0");
        }
    }
}
