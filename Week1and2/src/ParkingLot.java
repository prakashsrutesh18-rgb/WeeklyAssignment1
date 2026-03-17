import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        this.occupied = false;
    }
}

public class ParkingLot {

    int SIZE = 10; // use 500 in real case
    ParkingSpot[] table = new ParkingSpot[SIZE];

    int totalVehicles = 0;
    int totalProbes = 0;

    public ParkingLot() {
        for (int i = 0; i < SIZE; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    public int hash(String plate) {
        return Math.abs(plate.hashCode()) % SIZE;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % SIZE;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        totalVehicles++;
        totalProbes += probes;

        System.out.println("Vehicle " + plate +
                " parked at spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {
            if (table[index].licensePlate.equals(plate)) {

                long exitTime = System.currentTimeMillis();
                long duration = (exitTime - table[index].entryTime) / 1000; // seconds

                double fee = duration * 0.01; // simple fee

                table[index].occupied = false;

                System.out.println("Vehicle " + plate +
                        " exited from spot #" + index +
                        ", Duration: " + duration + " sec" +
                        ", Fee: $" + fee);
                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found!");
    }

    // Statistics
    public void getStatistics() {

        int occupiedCount = 0;

        for (ParkingSpot spot : table) {
            if (spot.occupied) occupiedCount++;
        }

        double occupancy = (occupiedCount * 100.0) / SIZE;
        double avgProbes = (totalVehicles == 0) ? 0 : (totalProbes * 1.0 / totalVehicles);

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Average Probes: " + avgProbes);
    }

    public static void main(String[] args) throws InterruptedException {

        ParkingLot pl = new ParkingLot();

        // Park vehicles
        pl.parkVehicle("ABC-1234");
        pl.parkVehicle("ABC-1235");
        pl.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        // Exit
        pl.exitVehicle("ABC-1234");

        // Stats
        pl.getStatistics();
    }
}