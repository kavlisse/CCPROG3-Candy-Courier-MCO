
/**
 * Represents the world grid in the Candy Courier game.
 * Manages the world size and expands the grid when the player
 * reaches a higher star rank.
 *
 * @author Member 1
 * @version 1.0
 */
public class World {

    /** The two-dimensional grid containing the buildings in the world. */
    private Building[][] grid;

    /** The current dimension of the square world grid. */
    private int gridSize;


    /**
     * Constructs a World with the specified grid size.
     *
     * @param gridSize the initial dimension of the world grid
     */
    public World(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new Building[gridSize][gridSize];
    }


    /**
     * Expands the world grid to the specified new dimension.
     * Existing buildings retain their relative positions.
     *
     * @param newSize the new dimension of the world grid
     */
    public void expandWorld(int newSize) {

        if (newSize <= gridSize) {
            return;
        }

        Building[][] newGrid = new Building[newSize][newSize];

        int offset = (newSize - gridSize) / 2;

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                newGrid[row + offset][column + offset] = grid[row][column];

                if (grid[row][column] != null) {
                    grid[row][column].setPosition(
                            row + offset, column + offset);
                }
            }
        }

        grid = newGrid;
        gridSize = newSize;
    }


    /**
     * Returns the current dimension of the world grid.
     *
     * @return the world grid dimension
     */
    public int getGridSize() {
        return gridSize;
    }


    /**
     * Expands the production grids of all factories in the world.
     *
     * @param newSize the new production grid dimension
     */
    public void expandAllFactoryGrids(int newSize) {

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                Building building = grid[row][column];

                if (building != null && building.getSymbol() == 'F') {
                    Factory factory = (Factory) building;
                    factory.expandProductionGrid(newSize);
                }
            }
        }
    }


}
