import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class AnalyticsDashboard {

    // page → total visits
    HashMap<String, Integer> pageViews = new HashMap<>();

    // page → unique users
    HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // source → count
    HashMap<String, Integer> trafficSource = new HashMap<>();

    // Process event
    public void processEvent(PageEvent event) {

        // Count page views
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // Unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Traffic source count
        trafficSource.put(event.source,
                trafficSource.getOrDefault(event.source, 0) + 1);
    }

    // Get top 10 pages
    public void getTopPages() {

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        // Sort by visits (descending)
        list.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Top Pages:");

        int count = 0;
        for (Map.Entry<String, Integer> entry : list) {
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((count + 1) + ". " + url +
                    " - " + views + " views (" + unique + " unique)");

            count++;
            if (count == 10) break;
        }
    }

    // Traffic source stats
    public void getTrafficSources() {

        int total = 0;
        for (int val : trafficSource.values()) {
            total += val;
        }

        System.out.println("\nTraffic Sources:");

        for (String src : trafficSource.keySet()) {
            int count = trafficSource.get(src);
            double percent = (count * 100.0) / total;

            System.out.println(src + ": " + percent + "%");
        }
    }

    // Dashboard (simulate every 5 sec)
    public void getDashboard() {
        getTopPages();
        getTrafficSources();
    }

    public static void main(String[] args) {

        AnalyticsDashboard ad = new AnalyticsDashboard();

        // Simulated events
        ad.processEvent(new PageEvent("/article/breaking-news", "user1", "google"));
        ad.processEvent(new PageEvent("/article/breaking-news", "user2", "facebook"));
        ad.processEvent(new PageEvent("/sports/championship", "user3", "direct"));
        ad.processEvent(new PageEvent("/article/breaking-news", "user1", "google"));
        ad.processEvent(new PageEvent("/sports/championship", "user4", "google"));

        // Show dashboard
        ad.getDashboard();
    }
}