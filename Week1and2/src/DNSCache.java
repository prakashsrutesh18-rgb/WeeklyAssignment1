import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCache {

    // LRU Cache using LinkedHashMap
    LinkedHashMap<String, DNSEntry> cache;
    int capacity = 3;

    int hits = 0;
    int misses = 0;

    public DNSCache() {
        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > capacity;
            }
        };
    }

    // Simulated upstream DNS query
    public String queryUpstream(String domain) {
        return "172.217.14." + new Random().nextInt(255);
    }

    // Resolve domain
    public String resolve(String domain) {

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain); // remove expired
            }
        }

        // Cache MISS
        misses++;
        String ip = queryUpstream(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 5); // TTL = 5 sec
        cache.put(domain, newEntry);

        return "Cache MISS → " + ip;
    }

    // Stats
    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits * 100.0 / total);

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCache dns = new DNSCache();

        // First request → MISS
        System.out.println(dns.resolve("google.com"));

        // Second request → HIT
        System.out.println(dns.resolve("google.com"));

        // Wait for expiry
        Thread.sleep(6000);

        // After TTL → EXPIRED → MISS again
        System.out.println(dns.resolve("google.com"));

        // Stats
        dns.getCacheStats();
    }
}