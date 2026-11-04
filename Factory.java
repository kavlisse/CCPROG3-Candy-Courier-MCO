
/**
 * Represents a factory in the Candy Courier game.
 * Manages the candy production grid and expands its dimensions
 * when the player reaches a higher star rank.
 *
 * @author Katelyn and Shaira
 * @version 1.1
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
    }


    /**
     * Expands the production grid to the specified new dimension.
     * Existing candies retain their relative positions.
     * Newly added slots remain empty until the next day refresh.
     *
     * @param newSize the new dimension of the production grid
     */
    public void expandProductionGrid(int newSize) {

        if (newSize <= gridSize) {
            return;
        }

        Candy[][] newGrid = new Candy[newSize][newSize];

        int offset = (newSize - gridSize) / 2;

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                newGrid[row + offset][column + offset] =
                        productionGrid[row][column];
            }
        }

        productionGrid = newGrid;
        gridSize = newSize;
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
     * Returns the current allowed candy types
     *
     * @return the current allowed candy types
     */
    public Candy[] getAllowedCandies() {
        return allowedCandies;
    } 

    /**
     * Checks whether the factory produces the specified candy type.
     *
     * @param candyType the type of candy to search for
     * @return true if the factory produces the candy type; false otherwise
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
     * Checks whether the factory produces the specified candy type and flavor.
     *
     * @param candyType the type of candy to search for
     * @param candyFlavor the flavor of candy to search for
     * @return true if the factory produces the specified candy; false otherwise
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
    
}
