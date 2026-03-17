import java.util.*;

public class FlashSaleManager {

    // productId → stock count
    HashMap<String, Integer> stockMap = new HashMap<>();

    // productId → waiting list (FIFO)
    HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    // Check stock
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase item (synchronized to avoid overselling)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            waitingList.putIfAbsent(productId, new LinkedList<>());
            waitingList.get(productId).add(userId);

            int position = waitingList.get(productId).size();
            return "Out of stock, added to waiting list at position #" + position;
        }
    }

    public static void main(String[] args) {

        FlashSaleManager fm = new FlashSaleManager();

        // Initial stock
        fm.stockMap.put("IPHONE15_256GB", 100);

        // Check stock
        System.out.println("Stock: " + fm.checkStock("IPHONE15_256GB") + " units");

        // Simulate purchases
        System.out.println(fm.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(fm.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock ending
        fm.stockMap.put("IPHONE15_256GB", 0);

        // Next user goes to waiting list
        System.out.println(fm.purchaseItem("IPHONE15_256GB", 99999));
    }
}