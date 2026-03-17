import java.util.*;

class TokenBucket {
    int tokens;
    int maxTokens;
    long lastRefillTime;

    public TokenBucket(int maxTokens) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens every hour
    public void refill() {
        long now = System.currentTimeMillis();

        long elapsed = now - lastRefillTime;

        // 1 hour = 3600000 ms
        if (elapsed >= 3600000) {
            tokens = maxTokens;
            lastRefillTime = now;
        }
    }

    // Try to consume token
    public boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    public int getRemainingTokens() {
        refill();
        return tokens;
    }
}

public class RateLimiter {

    // clientId → TokenBucket
    HashMap<String, TokenBucket> clients = new HashMap<>();

    int LIMIT = 5; // (use 1000 in real case)

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry later)";
        }
    }

    public void getRateLimitStatus(String clientId) {
        if (!clients.containsKey(clientId)) {
            System.out.println("No data for client");
            return;
        }

        TokenBucket bucket = clients.get(clientId);

        int used = LIMIT - bucket.getRemainingTokens();

        System.out.println("Used: " + used +
                ", Limit: " + LIMIT +
                ", Remaining: " + bucket.getRemainingTokens());
    }

    public static void main(String[] args) {

        RateLimiter rl = new RateLimiter();

        String client = "abc123";

        // Simulate requests
        for (int i = 1; i <= 7; i++) {
            System.out.println(rl.checkRateLimit(client));
        }

        // Status
        rl.getRateLimitStatus(client);
    }
}