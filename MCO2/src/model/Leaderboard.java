
package model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Stores and manages player records.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class Leaderboard {

    private ArrayList<PlayerRecord> records;

    public Leaderboard() {
        records = new ArrayList<>();
    }

    public void addRecord(PlayerRecord record) {
        if (record == null) return;

        records.add(record);
        sortRecords();
    }

    public void setRecords(ArrayList<PlayerRecord> records) {
        this.records = new ArrayList<>(records);
        sortRecords();
    }

    public ArrayList<PlayerRecord> getRecords() {
        return new ArrayList<>(records);
    }

    private void sortRecords() {
        records.sort(
            Comparator.comparingInt(PlayerRecord::getStarRank).reversed()
                .thenComparingInt(PlayerRecord::getDaysSpent)
                .thenComparingInt(PlayerRecord::getTotalSteps)
        );
    }
}
