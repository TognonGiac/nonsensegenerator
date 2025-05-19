import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.Document.Type;
import com.google.protobuf.*;

import java.io.IOException;
import java.util.*;
import io.github.cdimascio.dotenv.Dotenv;

public class SentenceAnalyzer {

    private final LanguageServiceClient language;

    public SentenceAnalyzer() throws IOException {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", dotenv.get("GOOGLE_APPLICATION_CREDENTIALS"));
        language = LanguageServiceClient.create();
    }

    public Map<String, List<String>> analyzeSyntax(String text) throws IOException {
        Document doc = Document.newBuilder()
                .setContent(text)
                .setType(Type.PLAIN_TEXT)
                .build();

        AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                .setDocument(doc)
                .setEncodingType(EncodingType.UTF8)
                .build();

        AnalyzeSyntaxResponse response = language.analyzeSyntax(request);

        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        for (Token token : response.getTokensList()) {
            String word = token.getText().getContent();
            PartOfSpeech.Tag tag = token.getPartOfSpeech().getTag();

            switch (tag) {
                case NOUN:
                    nouns.add(word);
                    break;
                case VERB:
                    verbs.add(word);
                    break;
                case ADJ:
                    adjectives.add(word);
                    break;
                default:
                    break;
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("nouns", nouns);
        result.put("verbs", verbs);
        result.put("adjectives", adjectives);
        return result;
    }

    public void close() {
        language.close();
    }
}
