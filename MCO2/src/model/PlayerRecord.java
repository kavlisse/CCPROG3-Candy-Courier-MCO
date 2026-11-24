
package model;

/**
 * Stores the information of a player who has completed a game.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class PlayerRecord {

    /** The player's name. */
    private String playerName;

    /** The player's final star rank. */
    private int starRank;

    /** The number of days spent completing the game. */
    private int daysSpent;

    /** The player's total steps traveled. */
    private int totalSteps;


    /**
     * Constructs a PlayerRecord.
     *
     * @param playerName the player's name
     * @param starRank the player's final star rank
     * @param daysSpent the number of days spent
     * @param totalSteps the total steps traveled
     */
    public PlayerRecord(String playerName, int starRank,
                        int daysSpent, int totalSteps) {

        this.playerName = playerName;
        this.starRank = starRank;
        this.daysSpent = daysSpent;
        this.totalSteps = totalSteps;
    }


    // ==========================================
    // GETTERS
    // ==========================================

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }


    /**
     * Returns the player's final star rank.
     *
     * @return the star rank
     */
    public int getStarRank() {
        return starRank;
    }


    /**
     * Returns the number of days spent.
     *
     * @return the days spent
     */
    public int getDaysSpent() {
        return daysSpent;
    }


    /**
     * Returns the player's total steps traveled.
     *
     * @return the total steps
     */
    public int getTotalSteps() {
        return totalSteps;
    }

}
