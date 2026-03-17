import java.util.*;

public class UserNameChecker {

    HashMap<String, Integer> usernameMap = new HashMap<>();
    HashMap<String, Integer> attemptCount = new HashMap<>();

    public boolean checkAvailability(String username) {
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);
        return !usernameMap.containsKey(username);
    }

    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String alt = username.replace("_", ".");
        if (!usernameMap.containsKey(alt)) {
            suggestions.add(alt);
        }

        return suggestions;
    }

    public String getMostAttempted() {
        String maxUser = "";
        int maxCount = 0;

        for (String user : attemptCount.keySet()) {
            if (attemptCount.get(user) > maxCount) {
                maxCount = attemptCount.get(user);
                maxUser = user;
            }
        }
        return maxUser;
    }

    public static void main(String[] args) {

        UserNameChecker uc = new UserNameChecker();

        // Existing users
        uc.registerUser("john_doe", 1);
        uc.registerUser("admin", 2);

        // Check availability
        System.out.println("john_doe → " + uc.checkAvailability("john_doe"));
        System.out.println("jane_smith → " + uc.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("Suggestions for john_doe → " + uc.suggestAlternatives("john_doe"));

        // Simulate multiple attempts
        uc.checkAvailability("admin");
        uc.checkAvailability("admin");
        uc.checkAvailability("admin");

        // Most attempted
        System.out.println("Most Attempted → " + uc.getMostAttempted());
    }
}