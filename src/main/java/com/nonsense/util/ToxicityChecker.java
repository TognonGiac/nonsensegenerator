package com.nonsense.util;

import com.google.cloud.language.v1beta2.*;

import java.io.IOException;

public class ToxicityChecker {
    public static boolean isToxic(String text) throws IOException {
        try (ModerationServiceClient client = ModerationServiceClient.create()) {
            TextModerationRequest request = TextModerationRequest.newBuilder()
                    .setText(text)
                    .setType(ModerationType.MODERATION_TYPE_UNSPECIFIED)
                    .build();

            TextModerationResponse response = client.moderateText(request);

            return response.getModerationCategoriesList().stream()
                    .anyMatch(cat -> cat.getConfidence() > 0.7);
        }
    }
}
