
/**
 * Represents the player in the Candy Courier game.
 * Stores the player's name, total experience points, and current star rank.
 * Handles XP accumulation, rank progression, and rank-based unlocks.
 *
 * @author Katelyn & Shaira 
 * @version 1.0
 */
public class Player {

    /** The name of the player. */
    private String name;

    /** The total experience points accumulated by the player. */
    private int totalXP;

    /** The current star rank of the player, ranging from 0 to 5. */
    private int starRank;

    /** The inventory owned by the player. */
    private Inventory inventory;
    
    /**
     * Constructs a new Player with an initial XP of 0 and a rank of 0 stars.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.totalXP = 0;
        this.starRank = 0;
        this.inventory = new Inventory(20);
    }


    /**
     * Adds experience points to the player's total XP
     * and checks whether the player qualifies for promotion.
     *
     * @param xp the amount of experience points earned
     */
    public void addXP(int xp) {
        if (xp > 0) {
            totalXP += xp;
            checkRankProgression();
        }
    }


    /**
     * Checks the player's total XP and updates their star rank
     * according to the required promotion thresholds.
     *
     * Rank requirements:
     * 1 star - 100 XP
     * 2 stars - 500 XP
     * 3 stars - 1400 XP
     * 4 stars - 3000 XP
     * 5 stars - 5500 XP
     */
    public void checkRankProgression() {

        if (totalXP >= 5500) {
            starRank = 5;
        }
        else if (totalXP >= 3000) {
            starRank = 4;
        }
        else if (totalXP >= 1400) {
            starRank = 3;
        }
        else if (totalXP >= 500) {
            starRank = 2;
        }
        else if (totalXP >= 100) {
            starRank = 1;
        }
    }


    /**
     * Returns the world grid dimension based on the player's current rank.
     *
     * @return the world grid dimension
     */
    public int getWorldGridSize() {

        if (starRank == 0) {
            return 7;
        }
        else if (starRank == 1) {
            return 9;
        }
        else if (starRank == 2) {
            return 11;
        }
        else if (starRank == 3) {
            return 13;
        }
        else {
            return 15;
        }
    }



    /**
     * Returns the candy production grid dimension based on the player's rank.
     *
     * @return the factory grid dimension, up to 10
     */
    public int getFactoryGridSize() {
        if (starRank >= 4) {
            return 10;
        }

        return 6 + starRank;
    }


    /**
     * Returns the maximum inventory capacity based on the player's rank.
     *
     * @return the inventory capacity, up to 100
     */
    public int getInventoryCapacity() {
        if (starRank >= 4) {
            return 100;
        }

        return 20 + (starRank * 20);
    }


    /**
     * Checks whether the player has unlocked diagonal movement.
     *
     * @return true if the player has reached at least 3 stars;
     *         false otherwise
     */
    public boolean canMoveDiagonally() {
        return starRank >= 3;
    }


    /**
     * Checks whether the player has reached the maximum star rank.
     *
     * @return true if the player has reached 5 stars; false otherwise
     */
    public boolean hasReachedMaxRank() {
        return starRank == 5;
    }


    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the player's total accumulated XP.
     *
     * @return the player's total XP
     */
    public int getTotalXP() {
        return totalXP;
    }


    /**
     * Returns the player's current star rank.
     *
     * @return the player's star rank
     */
    public int getStarRank() {
        return starRank;
    }

    
    /**
     * Returns the player's inventory.
     *
     * @return the player's inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    
}
