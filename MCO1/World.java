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
     * Each building is identified by its name and a corresponding legend.
     *
     * @param player the player whose position will be displayed
     */
    public void displayWorld(Player player) {

        int cellWidth = 10;
        int rowWidth = 5;
        int totalWidth = rowWidth + (gridSize * cellWidth);

        // Top border and title.
        printLine('=', totalWidth);

        System.out.printf("%" + (totalWidth / 2 + 10) + "s%n",
                "WORLD GRID (" + gridSize + " x " + gridSize + ")");

        printLine('=', totalWidth);

        // Display column headings.
        System.out.printf("%-" + rowWidth + "s", "ROW");

        for (int column = 0; column < gridSize; column++) {
            System.out.printf("%-" + cellWidth + "d", column);
        }

        System.out.println();

        // Separator below column headings.
        printLine('-', totalWidth);

        // Display world grid.
        for (int row = 0; row < gridSize; row++) {

            System.out.printf("%-" + rowWidth + "d", row);

            for (int column = 0; column < gridSize; column++) {

                ArrayList<String> labels = new ArrayList<>();

                // Display player.
                if (player != null
                        && player.getRow() == row
                        && player.getColumn() == column) {

                    labels.add("P");
                }

                // Display houses.
                for (House house : houses) {

                    if (house.getRow() == row
                            && house.getColumn() == column) {

                        labels.add(house.getName()
                                .replace("House ", "H"));
                    }
                }

                // Display factories and player's home.
                Building building = grid[row][column];

                if (building != null) {

                    if (building.getSymbol() == 'F') {

                        labels.add(building.getName()
                                .replace("Factory ", "F"));
                    }
                    else if (building.getSymbol() == 'H') {

                        labels.add("PH");
                    }
                }

                // Display tile contents.
                String cellContent;

                if (labels.isEmpty()) {
                    cellContent = ".";
                }
                else {
                    cellContent = String.join("/", labels);
                }

                System.out.printf("%-" + cellWidth + "s",
                        "[" + cellContent + "]");
            }

            System.out.println();
        }

        // Bottom border.
        printLine('=', totalWidth);

        // Display legend.
        System.out.println("\nWORLD GRID LEGEND");
        printLine('-', 25);

        System.out.println("P  = Player");
        System.out.println("PH = Player's Home");

        for (Factory factory : getFactories()) {

            String code = factory.getName()
                    .replace("Factory ", "F");

            System.out.println(code + "  = " + factory.getName());
        }

        for (House house : houses) {

            String code = house.getName()
                    .replace("House ", "H");

            System.out.println(code + "  = " + house.getName());
        }

        System.out.println(".  = Empty tile");

        printLine('=', totalWidth);
    }


    /**
     * Prints a horizontal line using the specified character.
     *
     * @param symbol the character used to create the line
     * @param length the length of the line
     */
    private void printLine(char symbol, int length) {

        for (int i = 0; i < length; i++) {
            System.out.print(symbol);
        }

        System.out.println();
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
