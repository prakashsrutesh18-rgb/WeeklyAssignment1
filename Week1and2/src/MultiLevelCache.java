import java.util.*;

class Video {
    String id;
    String data;

    Video(String id, String data) {
        this.id = id;
        this.data = data;
    }
}

public class MultiLevelCache {

    // L1 Cache (small, fast)
    LinkedHashMap<String, Video> L1;

    // L2 Cache (bigger)
    LinkedHashMap<String, Video> L2;

    // L3 Database
    HashMap<String, Video> L3 = new HashMap<>();

    int L1_CAP = 3;
    int L2_CAP = 5;

    int L1_hits = 0, L2_hits = 0, L3_hits = 0;

    public MultiLevelCache() {

        // LRU for L1
        L1 = new LinkedHashMap<String, Video>(L1_CAP, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Video> e) {
                return size() > L1_CAP;
            }
        };

        // LRU for L2
        L2 = new LinkedHashMap<String, Video>(L2_CAP, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Video> e) {
                return size() > L2_CAP;
            }
        };
    }

    // Get video
    public void getVideo(String id) {

        // L1
        if (L1.containsKey(id)) {
            L1_hits++;
            System.out.println("L1 HIT → " + id);
            return;
        }

        // L2
        if (L2.containsKey(id)) {
            L2_hits++;
            System.out.println("L2 HIT → " + id + " → Promoted to L1");

            Video v = L2.get(id);
            L1.put(id, v); // promote
            return;
        }

        // L3
        if (L3.containsKey(id)) {
            L3_hits++;
            System.out.println("L3 HIT → " + id + " → Added to L2");

            Video v = L3.get(id);
            L2.put(id, v);
            return;
        }

        System.out.println("Video not found!");
    }

    // Add video to DB
    public void addToDB(String id, String data) {
        L3.put(id, new Video(id, data));
    }

    // Stats
    public void getStats() {

        int total = L1_hits + L2_hits + L3_hits;

        System.out.println("\nCache Stats:");
        System.out.println("L1 Hits: " + L1_hits);
        System.out.println("L2 Hits: " + L2_hits);
        System.out.println("L3 Hits: " + L3_hits);

        if (total > 0) {
            System.out.println("Overall Hit Rate: " +
                    ((total * 100.0) / total) + "%");
        }
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        // Add videos to DB
        cache.addToDB("video1", "data1");
        cache.addToDB("video2", "data2");
        cache.addToDB("video3", "data3");

        // Access videos
        cache.getVideo("video1"); // L3 → L2
        cache.getVideo("video1"); // L2 → L1
        cache.getVideo("video1"); // L1

        cache.getVideo("video2"); // L3 → L2
        cache.getVideo("video3"); // L3 → L2

        // Stats
        cache.getStats();
    }
}