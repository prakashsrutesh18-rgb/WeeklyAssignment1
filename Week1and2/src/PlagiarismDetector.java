import java.util.*;

public class PlagiarismDetector {

    // n-gram → set of document IDs
    HashMap<String, Set<String>> index = new HashMap<>();

    int N = 5; // 5-gram

    // Generate n-grams
    public List<String> generateNGrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.split(" ");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder gram = new StringBuilder();
            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }
            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document to index
    public void addDocument(String docId, String text) {
        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {
            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }
    }

    // Analyze document
    public void analyzeDocument(String docId, String text) {
        List<String> ngrams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            if (index.containsKey(gram)) {
                for (String otherDoc : index.get(gram)) {
                    if (!otherDoc.equals(docId)) {
                        matchCount.put(otherDoc,
                                matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Total n-grams: " + ngrams.size());

        for (String doc : matchCount.keySet()) {
            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matches with " + doc + ": " + matches);
            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED 🚨");
            } else if (similarity > 10) {
                System.out.println("Suspicious ⚠️");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector pd = new PlagiarismDetector();

        // Existing documents
        String doc1 = "this is a sample essay for plagiarism detection system using hashing technique";
        String doc2 = "this is a sample essay for plagiarism detection system using advanced hashing method";

        pd.addDocument("essay_089", doc1);
        pd.addDocument("essay_092", doc2);

        // New document
        String newDoc = "this is a sample essay for plagiarism detection system using hashing technique";

        pd.analyzeDocument("essay_123", newDoc);
    }
}
