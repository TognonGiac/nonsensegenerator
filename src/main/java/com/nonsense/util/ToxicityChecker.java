package com.nonsense.util;

import com.google.cloud.language.v1.*;
import java.io.IOException;

public class ToxicityChecker {
    public static boolean isToxic(String text) throws IOException {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            ModerateTextRequest request = ModerateTextRequest.newBuilder()
                    .setContent(text)
                    .setMimeType("text/plain")
                    .build();

            ModerateTextResponse response = language.moderateText(request);

            return response.getModerationCategoriesList().stream()
                    .anyMatch(cat -> cat.getConfidence() > 0.7);
        }
    }
}
