import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    int time; // in minutes (e.g., 10:30 → 630)
    String account;

    Transaction(int id, int amount, String merchant, int time, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
        this.account = account;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    // ✅ Add transaction
    public void add(Transaction t) {
        transactions.add(t);
    }

    // ✅ Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                System.out.println("Two-Sum Pair: (" +
                        map.get(complement).id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // ✅ Two-Sum with 1-hour window (60 min)
    public void findTwoSumWithTime(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= 60) {
                    System.out.println("Time-Window Pair: (" +
                            prev.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // ✅ Duplicate Detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {
            List<Transaction> list = map.get(key);

            if (list.size() > 1) {
                System.out.print("Duplicate: " + key + " Accounts: ");
                for (Transaction t : list) {
                    System.out.print(t.account + " ");
                }
                System.out.println();
            }
        }
    }

    // ✅ K-Sum (simple 3-sum)
    public void findThreeSum(int target) {

        int n = transactions.size();

        for (int i = 0; i < n; i++) {

            HashMap<Integer, Integer> map = new HashMap<>();

            for (int j = i + 1; j < n; j++) {
                int rem = target - transactions.get(i).amount - transactions.get(j).amount;

                if (map.containsKey(rem)) {
                    System.out.println("3-Sum: (" +
                            transactions.get(i).id + ", " +
                            transactions.get(j).id + ", " +
                            map.get(rem) + ")");
                }

                map.put(transactions.get(j).amount, transactions.get(j).id);
            }
        }
    }

    public static void main(String[] args) {

        TransactionAnalyzer ta = new TransactionAnalyzer();

        ta.add(new Transaction(1, 500, "StoreA", 600, "acc1"));
        ta.add(new Transaction(2, 300, "StoreB", 615, "acc2"));
        ta.add(new Transaction(3, 200, "StoreC", 630, "acc3"));
        ta.add(new Transaction(4, 500, "StoreA", 640, "acc4"));

        // Two Sum
        ta.findTwoSum(500);

        // Time window
        ta.findTwoSumWithTime(500);

        // Duplicates
        ta.detectDuplicates();

        // 3-Sum
        ta.findThreeSum(1000);
    }
}
