
/**
 * Represents a factory in the Candy Courier game.
 * Manages the candy production grid and expands its dimensions
 * when the player reaches a higher star rank.
 *
 * @author Member 1
 * @version 1.0
 */
public class Factory extends Building {

    /** The two-dimensional grid containing the factory's candies. */
    private Candy[][] productionGrid;

    /** The types of candies that this factory is allowed to produce. */
    private String[] allowedCandyTypes;

    /** The current dimension of the square production grid. */
    private int gridSize;


    /**
     * Constructs a Factory with the specified name, location,
     * allowed candy types, and production grid size.
     *
     * @param name the name of the factory
     * @param row the row coordinate of the factory
     * @param column the column coordinate of the factory
     * @param allowedCandyTypes the candy types this factory can produce
     * @param gridSize the initial dimension of the production grid
     */
    public Factory(String name, int row, int column,
                   String[] allowedCandyTypes, int gridSize) {

        super(name, row, column, 'F');

        this.allowedCandyTypes = allowedCandyTypes;
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

}
