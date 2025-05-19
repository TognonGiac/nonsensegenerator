public class Main {
    public static void main(String[] args) throws IOException {
        SentenceAnalyzer analyzer = new SentenceAnalyzer();

        String input = "The quick brown fox jumps over the lazy dog.";
        Map<String, List<String>> result = analyzer.analyzeSyntax(input);

        System.out.println("Nouns: " + result.get("nouns"));
        System.out.println("Verbs: " + result.get("verbs"));
        System.out.println("Adjectives: " + result.get("adjectives"));

        analyzer.close();
    }
}
