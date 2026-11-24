
package controller;

import java.util.ArrayList;

import model.Leaderboard;
import model.PlayerRecord;
import persistence.LeaderboardFileHandler;

/**
 * Controls leaderboard operations and coordinates
 * the leaderboard model with the file handler.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class LeaderboardController {

    /** The leaderboard being controlled. */
    private Leaderboard leaderboard;

    /** Handles reading and writing leaderboard records. */
    private LeaderboardFileHandler fileHandler;


    /**
     * Constructs a LeaderboardController.
     *
     * @param leaderboard the leaderboard model
     * @param fileHandler the file handler
     */
    public LeaderboardController(
            Leaderboard leaderboard,
            LeaderboardFileHandler fileHandler) {

        this.leaderboard = leaderboard;
        this.fileHandler = fileHandler;
    }


    // ==========================================
    // LOAD LEADERBOARD
    // ==========================================

    /**
     * Loads existing player records from the text file.
     */
    public void loadLeaderboard() {
        ArrayList<PlayerRecord> records = fileHandler.loadRecords();
        leaderboard.setRecords(records);
    }


    // ==========================================
    // SAVE PLAYER RECORD
    // ==========================================

    /**
     * Adds a player's completed game record to the leaderboard
     * and saves the updated records.
     *
     * @param record the player's completed game record
     */
    public boolean savePlayerRecord(PlayerRecord record) {

        if (record == null) {
            return false;
        }

        // Keep the current records in case saving fails.
        ArrayList<PlayerRecord> previousRecords =
            leaderboard.getRecords();

        // Add the new record and save the updated leaderboard.
        leaderboard.addRecord(record);

        boolean saved = fileHandler.saveRecords(
            leaderboard.getRecords()
        );

        // Restore the previous records if saving was unsuccessful.
        if (!saved) {
            leaderboard.setRecords(previousRecords);
            return false;
        }

        return true;
    }

    // ==========================================
    // LEADERBOARD INFORMATION
    // ==========================================

    /**
     * Returns all leaderboard records.
     *
     * @return the list of player records
     */
    public ArrayList<PlayerRecord> getLeaderboard() {
        return leaderboard.getRecords();
    }

}
