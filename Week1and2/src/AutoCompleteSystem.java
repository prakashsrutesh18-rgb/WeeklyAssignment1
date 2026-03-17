import java.util.*;

public class AutoCompleteSystem {

    // query → frequency
    HashMap<String, Integer> queryFreq = new HashMap<>();

    // Add or update query frequency
    public void updateFrequency(String query) {
        queryFreq.put(query, queryFreq.getOrDefault(query, 0) + 1);
    }

    // Search suggestions
    public List<String> search(String prefix) {

        List<Map.Entry<String, Integer>> matches = new ArrayList<>();

        // Find matching queries
        for (Map.Entry<String, Integer> entry : queryFreq.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                matches.add(entry);
            }
        }

        // Sort by frequency (descending)
        matches.sort((a, b) -> b.getValue() - a.getValue());

        // Top 10 results
        List<String> result = new ArrayList<>();
        int count = 0;

        for (Map.Entry<String, Integer> entry : matches) {
            result.add(entry.getKey() + " (" + entry.getValue() + ")");
            count++;
            if (count == 10) break;
        }

        return result;
    }

    public static void main(String[] args) {

        AutoCompleteSystem ac = new AutoCompleteSystem();

        // Sample data
        ac.updateFrequency("java tutorial");
        ac.updateFrequency("javascript");
        ac.updateFrequency("java download");
        ac.updateFrequency("java tutorial");
        ac.updateFrequency("java tutorial");
        ac.updateFrequency("java 21 features");

        // Search prefix
        System.out.println("Suggestions for 'jav':");
        List<String> results = ac.search("jav");

        for (String res : results) {
            System.out.println(res);
        }

        // Update trending query
        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");

        System.out.println("\nAfter updating frequency:");
        System.out.println(ac.search("java"));
    }
}