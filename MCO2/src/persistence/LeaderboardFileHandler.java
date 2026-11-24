package persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.PlayerRecord;

/**
 * Handles loading and saving leaderboard records
 * using a text file.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class LeaderboardFileHandler {

    private String filePath;

    public LeaderboardFileHandler(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<PlayerRecord> loadRecords() {
        ArrayList<PlayerRecord> records = new ArrayList<>();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");

                    if (parts.length != 4) continue;

                    String playerName = parts[0].trim();

                    int starRank = Integer.parseInt(
                        parts[1].trim().replace("-star", "").trim()
                    );

                    int daysSpent = Integer.parseInt(
                        parts[2].trim().replace("days", "").trim()
                    );

                    int totalSteps = Integer.parseInt(
                        parts[3].trim().replace("steps", "").trim()
                    );

                    records.add(new PlayerRecord(
                        playerName,
                        starRank,
                        daysSpent,
                        totalSteps
                    ));

                } catch (NumberFormatException e) {
                    // Skip records with invalid numbers.
                }
            }

        } catch (IOException e) {
            // Return an empty list if the file cannot be read.
        }

        return records;
    }

    public boolean saveRecords(ArrayList<PlayerRecord> records) {
        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(filePath))) {

            for (PlayerRecord record : records) {
                writer.write(
                    record.getPlayerName() + ", " +
                    record.getStarRank() + "-star, " +
                    record.getDaysSpent() + " days, " +
                    record.getTotalSteps() + " steps"
                );

                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
