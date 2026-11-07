import java.util.ArrayList;
/**
 * Represents the world grid in the Candy Courier game.
 * Manages the world size, building placement, building retrieval,
 * and world expansion.
 *
 * @author Katelyn and Shaira
 * @version 1.1
 */

public class World {

    /** The two-dimensional grid containing the buildings in the world. */
    private Building[][] grid;

    /** The current dimension of the square world grid. */
    private int gridSize;

    /** Stores all houses, including houses sharing coordinates. */
    private ArrayList<House> houses;

    /**
     * Returns all factories currently placed in the world.
     *
     * @return a list of all factories in the world
     */
    public ArrayList<Factory> getFactories() {

        ArrayList<Factory> factories = new ArrayList<>();

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                Building building = grid[row][column];

                if (building != null && building.getSymbol() == 'F') {
                    factories.add((Factory) building);
                }
            }
        }

        return factories;
    }

    /**
     * Returns all houses currently placed in the world.
     *
     * @return a list of all houses in the world
     */
    public ArrayList<House> getHouses() {
        return new ArrayList<>(houses);
    }

    /**
     * Returns all houses located at the specified coordinates.
     *
     * @param row the row position
     * @param column the column position
     * @return the houses at the specified position
     */
    public ArrayList<House> getHousesAt(int row, int column) {

        ArrayList<House> housesAtPosition = new ArrayList<>();

        for (House house : houses) {
            if (house.getRow() == row && house.getColumn() == column) {
                housesAtPosition.add(house);
            }
        }

        return housesAtPosition;
    }

    /**
     * Adds a house to the list of houses in the world.
     *
     * @param house the house to add
     */
    public void addHouse(House house) {
        if (house != null) {
            houses.add(house);
        }
    }

    /**
     * Constructs a World with the specified grid size.
     *
     * @param gridSize the initial dimension of the world grid
     */
    public World(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new Building[gridSize][gridSize];
        this.houses = new ArrayList<>();
    }


    // ==========================================
    // STAR-RANKING SYSTEM
    // ==========================================

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
            }
        }

        // Update every house, including houses that share a grid position.
        for (House house : houses) {
            house.setPosition(
                    house.getRow() + offset,
                    house.getColumn() + offset);
        }

        // Update the positions of non-house buildings in the grid.
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                Building building = grid[row][column];

                if (building != null && building.getSymbol() != 'D') {
                    building.setPosition(row + offset, column + offset);
                }
            }
        }

        grid = newGrid;
        gridSize = newSize;
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


    // ==========================================
    // TRAVEL AND MOVEMENT
    // ==========================================

    /**
     * Places a building at its specified position in the world grid.
     * If the position is occupied, the existing building remains displayed.
     *
     * @param building the building to be placed
     * @param row the row position
     * @param column the column position
     */
    public void placeBuilding(Building building, int row, int column) {

        if (building == null || !isWithinBounds(row, column)) {
            return;
        }

        if (grid[row][column] == null) {
            grid[row][column] = building;
        }

        building.setPosition(row, column);
    }

    /**
     * Returns the building located at the specified position.
     *
     * @param row the row position
     * @param column the column position
     * @return the building at the specified position, or null if empty
     */
    public Building getBuildingAt(int row, int column) {

        if (!isWithinBounds(row, column)) {
            return null;
        }

        return grid[row][column];
    }

    /**
     * Checks whether the specified coordinates are within the world grid.
     *
     * @param row the row position
     * @param column the column position
     * @return true if the position is within the grid; false otherwise
     */
    public boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < gridSize
                && column >= 0 && column < gridSize;
    }

    /**
     * Displays the world grid and the player's current position.
     * The player is represented by P, while other buildings
     * are represented by their identifying symbols.
     *
     * @param player the player whose position will be displayed
     */
    public void displayWorld(Player player) {

        System.out.println("\nWORLD GRID (" + gridSize + " x " + gridSize + ")");

        for (int row = 0; row < gridSize; row++) {

            for (int column = 0; column < gridSize; column++) {

                if (player.getRow() == row &&
                    player.getColumn() == column) {

                    System.out.print("[ P ]");
                }
                else if (grid[row][column] != null) {

                    System.out.print("[ " +
                            grid[row][column].getSymbol() + " ]");
                }
                else {
                    System.out.print("[ . ]");
                }
            }

            System.out.println();
        }

        System.out.println("P = Player | H = Player's Home | F = Factory "
                + "| D = House | . = Empty tile");
    }

    /**
     * Returns the current dimension of the world grid.
     *
     * @return the world grid dimension
     */
    public int getGridSize() {
        return gridSize;
    }

}
