import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class NonsenseService {

    private static final Set<String> globalNouns = new HashSet<>();
    private static final Set<String> globalVerbs = new HashSet<>();
    private static final Set<String> globalAdjectives = new HashSet<>();

    private static final Path NOUNS_FILE = Paths.get("src/main/resources/data/nouns.txt");
    private static final Path VERBS_FILE = Paths.get("src/main/resources/data/verbs.txt");
    private static final Path ADJECTIVES_FILE = Paths.get("src/main/resources/data/adjectives.txt");

    public List<String> generateNonsenseSentences(String sentence, int count) throws IOException {
        SentenceAnalyzer analyzer = new SentenceAnalyzer();
        Map<String, List<String>> parts = analyzer.analyzeSyntax(sentence);
        analyzer.close();

        List<String> nouns = parts.get("nouns");
        List<String> verbs = parts.get("verbs");
        List<String> adjectives = parts.get("adjectives");

        // aggiorna dizionari globali
        if (nouns != null) globalNouns.addAll(nouns);
        if (verbs != null) globalVerbs.addAll(verbs);
        if (adjectives != null) globalAdjectives.addAll(adjectives);

        // salva su file (merge ed evita duplicati)
        updateWordFile(NOUNS_FILE, nouns);
        updateWordFile(VERBS_FILE, verbs);
        updateWordFile(ADJECTIVES_FILE, adjectives);

        return NonsenseGenerator.generateSentences(nouns, verbs, adjectives, count);
    }

    private void updateWordFile(Path filePath, List<String> newWords) throws IOException {
        if (newWords == null || newWords.isEmpty()) return;

        Set<String> existing = new HashSet<>();

        if (Files.exists(filePath)) {
            existing.addAll(Files.readAllLines(filePath).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet()));
        }

        // aggiungi solo parole nuove
        boolean updated = false;
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String word : newWords) {
                if (!existing.contains(word)) {
                    writer.write(word);
                    writer.newLine();
                    updated = true;
                }
            }
        }

        if (updated) {
            System.out.println("File aggiornato: " + filePath.getFileName());
        }
    }

    // opzionali
    public static Set<String> getGlobalNouns() { return globalNouns; }
    public static Set<String> getGlobalVerbs() { return globalVerbs; }
    public static Set<String> getGlobalAdjectives() { return globalAdjectives; }
}
