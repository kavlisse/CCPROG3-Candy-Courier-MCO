/**
 * Represents the player in the Candy Courier game.
 * Stores the player's information, handles XP accumulation,
 * rank progression, and player movement.
 *
 * @author Katelyn & Shaira
 * @version 1.3
 */
public class Player {

    // ==========================================
    // GENERAL PLAYER ATTRIBUTES
    // ==========================================

    /** The name of the player. */
    private String name;

    /** The total experience points accumulated by the player. */
    private int totalXP;

    /** The current star rank of the player, ranging from 0 to 5. */
    private int starRank;

    /** The total number of successful movement steps taken by the player. */
    private int totalSteps;

    /** The inventory owned by the player. */
    private Inventory inventory;


    // ==========================================
    // TASK 2: TRAVEL AND MOVEMENT ATTRIBUTES
    // ==========================================

    /** The player's current row position in the world grid. */
    private int row;

    /** The player's current column position in the world grid. */
    private int column;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    /**
     * Constructs a new Player with an initial XP of 0,
     * a rank of 0 stars, and an inventory capacity of 20.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.totalXP = 0;
        this.starRank = 0;
        this.totalSteps = 0;
        this.inventory = new Inventory(20);

        // Starting position is assigned by the Game class.
        this.row = 0;
        this.column = 0;
    }


    // ==========================================
    // TASK 1: STAR-RANKING SYSTEM
    // ==========================================

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


    // ==========================================
    // TASK 2: TRAVEL AND MOVEMENT
    // ==========================================

    /**
     * Updates the player's current position in the world grid.
     *
     * @param newRow the new row position
     * @param newColumn the new column position
     */
    public void updatePosition(int newRow, int newColumn) {
        this.row = newRow;
        this.column = newColumn;
    }

    /**
     * Returns the player's current row position.
     *
     * @return the current row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the player's current column position.
     *
     * @return the current column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Attempts to move the player in the specified direction.
     * Diagonal movement is only available to players with at least 3 stars.
     *
     * @param direction the direction of movement
     * @param worldSize the dimension of the world grid
     * @return true if the movement is successful; false otherwise
     */
    public boolean move(String direction, int worldSize) {

        int newRow = row;
        int newColumn = column;

        // Vertical and horizontal movement
        if (direction.equalsIgnoreCase("up")) {
            newRow--;
        }
        else if (direction.equalsIgnoreCase("down")) {
            newRow++;
        }
        else if (direction.equalsIgnoreCase("left")) {
            newColumn--;
        }
        else if (direction.equalsIgnoreCase("right")) {
            newColumn++;
        }

        // Diagonal movement (unlocked at 3 stars)
        else if (direction.equalsIgnoreCase("up-left")) {
            if (!canMoveDiagonally()) {
                System.out.println("Diagonal movement is locked. Reach 3 stars to unlock it.");
                return false;
            }
            newRow--;
            newColumn--;
        }
        else if (direction.equalsIgnoreCase("up-right")) {
            if (!canMoveDiagonally()) {
                System.out.println("Diagonal movement is locked. Reach 3 stars to unlock it.");
                return false;
            }
            newRow--;
            newColumn++;
        }
        else if (direction.equalsIgnoreCase("down-left")) {
            if (!canMoveDiagonally()) {
                System.out.println("Diagonal movement is locked. Reach 3 stars to unlock it.");
                return false;
            }
            newRow++;
            newColumn--;
        }
        else if (direction.equalsIgnoreCase("down-right")) {
            if (!canMoveDiagonally()) {
                System.out.println("Diagonal movement is locked. Reach 3 stars to unlock it.");
                return false;
            }
            newRow++;
            newColumn++;
        }
        else {
            System.out.println("Invalid movement direction.");
            return false;
        }

        // Prevent movement outside the world boundaries.
        if (newRow < 0 || newRow >= worldSize ||
            newColumn < 0 || newColumn >= worldSize) {

            System.out.println("You cannot move beyond the world boundaries.");
            System.out.println("Current position: (" + row + ", " + column + ")");
            return false;
        }

        // Update the player's position after a successful move.
        row = newRow;
        column = newColumn;
        totalSteps++;

        System.out.println("You moved " + direction.toLowerCase() + ".");
        System.out.println("New position: (" + row + ", " + column + ")");

        return true;
    }


    // ==========================================
    // GENERAL PLAYER GETTERS
    // ==========================================

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
     * Returns the player's total number of successful movement steps.
     *
     * @return the total steps traveled
     */
    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Returns the XP still needed to reach the next star rank.
     * Returns 0 when the player has already reached 5 stars.
     *
     * @return the XP needed for the next rank
     */
    public int getXPNeededForNextRank() {

        int nextRankThreshold;

        if (starRank == 0) {
            nextRankThreshold = 100;
        }
        else if (starRank == 1) {
            nextRankThreshold = 500;
        }
        else if (starRank == 2) {
            nextRankThreshold = 1400;
        }
        else if (starRank == 3) {
            nextRankThreshold = 3000;
        }
        else if (starRank == 4) {
            nextRankThreshold = 5500;
        }
        else {
            return 0;
        }

        return nextRankThreshold - totalXP;
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
