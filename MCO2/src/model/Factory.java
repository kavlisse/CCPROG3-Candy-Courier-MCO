package model; 
/**
 * Represents a factory in the Candy Courier game.
 * Manages the candy production grid, generates random candies,
 * allows candy pickups, and expands its dimensions when the
 * player reaches a higher star rank.
 *
 * @author Katelyn and Shaira
 * @version 2
 */
public class Factory extends Building {

    /** The two-dimensional grid containing the factory's candies. */
    private Candy[][] productionGrid;

    /** The candies and flavors that this factory is allowed to produce. */
    private Candy[] allowedCandies;

    /** The current dimension of the square production grid. */
    private int gridSize;

    /**
     * Constructs a Factory with the specified name, location,
     * allowed candy types, and production grid size.
     *
     * @param name the name of the factory
     * @param row the row coordinate of the factory
     * @param column the column coordinate of the factory
     * @param allowedCandies the candy types this factory can produce
     * @param gridSize the initial dimension of the production grid
     */
    public Factory(String name, int row, int column,
                   Candy[] allowedCandies, int gridSize) {

        super(name, row, column, 'F');

        this.allowedCandies = allowedCandies;
        this.gridSize = gridSize;
        this.productionGrid = new Candy[gridSize][gridSize];

        // Initialize candies without calling an overridable method
        // from the constructor.
        initializeProductionGrid();
    }


    // ==========================================
    // FACTORY CANDY GENERATION
    // ==========================================

    /**
     * Generates a random candy from the factory's allowed candies.
     *
     * @return a randomly generated candy
     */
    public Candy generateRandomCandy() {

        return createRandomCandy();
    }

    /**
     * Creates a random candy without invoking an overridable method.
     *
     * @return a randomly generated candy from this factory's allowed candies
     */
    private Candy createRandomCandy() {

        int randomIndex = (int) (Math.random() * allowedCandies.length);
        Candy selectedCandy = allowedCandies[randomIndex];

        return new Candy(
                selectedCandy.getCandyType(),
                selectedCandy.getCandyFlavor()
        );
    }

    /**
     * Refreshes the factory's production grid by generating
     * new random candies in every slot.
     *
     * This method is called when the factory is first created
     * and when a new day begins.
     */
    public void refreshProductionGrid() {

        initializeProductionGrid();
    }

    /**
     * Fills every slot in the current production grid with a new candy.
     * This private helper is safe to call from the constructor.
     */
    private void initializeProductionGrid() {

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                productionGrid[row][column] = createRandomCandy();
            }
        }
    }


    // ==========================================
    // STAR-RANKING SYSTEM
    // ==========================================

    /**
     * Expands the production grid to the specified new dimension.
     * Existing candies retain their original positions.
     * Newly added slots remain empty until the next day refresh.
     *
     * @param newSize the new dimension of the production grid
     */
    public void expandProductionGrid(int newSize) {

        if (newSize <= gridSize) {
            return;
        }

        Candy[][] newGrid = new Candy[newSize][newSize];

        // Preserve existing candies in their original positions.
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                newGrid[row][column] = productionGrid[row][column];

            }
        }

        productionGrid = newGrid;
        gridSize = newSize;
    }


    // ==========================================
    // FACTORY CANDY PICKUP
    // ==========================================

    /**
     * Attempts to pick up a candy from the production grid
     * and add it to the player's inventory.
     *
     * The candy is removed from the factory only if the
     * inventory successfully accepts it.
     *
     * @param row the selected candy's row
     * @param column the selected candy's column
     * @param inventory the player's inventory
     * @return true if the candy was successfully picked up;
     *         false otherwise
     */
    public boolean pickUpCandy(int row, int column, Inventory inventory) {

        // Check whether the selected position is within the grid.
        if (row < 0 || row >= gridSize ||
            column < 0 || column >= gridSize) {
            return false;
        }

        // Get the candy from the selected position.
        Candy candy = productionGrid[row][column];

        // Check whether the selected slot contains a candy.
        if (candy == null) {
            return false;
        }

        // Check whether the inventory can accept the candy.
        if (!inventory.addCandy(candy)) {
            return false;
        }

        // Remove the candy after a successful pickup.
        productionGrid[row][column] = null;

        return true;
    }

    // ==========================================
    // FACTORY CANDY SEARCH
    // ==========================================

    /**
     * Checks whether the factory produces the specified candy type.
     *
     * @param candyType the type of candy to search for
     * @return true if the factory produces the candy type;
     *         false otherwise
     */
    public boolean producesCandy(String candyType) {

        for (Candy candy : allowedCandies) {

            if (candy.getCandyType().equalsIgnoreCase(candyType.trim())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the candy at the specified production grid position.
     *
     * @param row the selected row
     * @param column the selected column
     * @return the candy at that position, or null if invalid or empty
     */
    public Candy getCandyAt(int row, int column) {

        if (row < 0 || row >= gridSize ||
            column < 0 || column >= gridSize) {
            return null;
        }

        return productionGrid[row][column];
    }

    /**
     * Returns a copy of the factory's production grid.
     *
     * @return a copy of the production grid
     */
    public Candy[][] getProductionGrid() {

        Candy[][] copy = new Candy[gridSize][gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                copy[row][column] = productionGrid[row][column];
            }
        }

        return copy;
    }

    /**
     * Checks whether the factory produces the specified candy type
     * and flavor.
     *
     * @param candyType the type of candy to search for
     * @param candyFlavor the flavor of candy to search for
     * @return true if the factory produces the specified candy;
     *         false otherwise
     */
    public boolean producesCandy(String candyType, String candyFlavor) {

        for (Candy candy : allowedCandies) {

            if (candy.getCandyType().equalsIgnoreCase(candyType.trim()) &&
                candy.getCandyFlavor().equalsIgnoreCase(candyFlavor.trim())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Returns the current dimension of the production grid.
     *
     * @return the production grid dimension
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Returns the current allowed candy types.
     *
     * @return the current allowed candy types
     */
    public Candy[] getAllowedCandies() {
        return allowedCandies;
    }

}