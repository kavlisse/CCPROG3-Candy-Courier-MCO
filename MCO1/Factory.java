/**
 * Represents a factory in the Candy Courier game.
 * Manages the candy production grid, generates random candies,
 * allows candy pickups, and expands its dimensions when the
 * player reaches a higher star rank.
 *
 * @author Katelyn and Shaira
 * @version 1.2
 */
public class Factory extends Building {

    /** The two-dimensional grid containing the factory's candies. */
    private Candy[][] productionGrid;

    /** The candies and flavors that this factory is allowed to produce. */
    private Candy[] allowedCandies;

    /** The current dimension of the square production grid. */
    private int gridSize;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

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

            System.out.println("Invalid factory position.");
            return false;
        }

        // Get the candy from the selected position.
        Candy candy = productionGrid[row][column];

        // Check whether the selected slot contains a candy.
        if (candy == null) {
            System.out.println("There is no candy in this slot.");
            return false;
        }

        // Check whether the inventory can accept the candy.
        if (!inventory.addCandy(candy)) {
            System.out.println("Your inventory is full! You cannot pick up more candies.");
            return false;
        }

        // Remove the candy after a successful pickup.
        productionGrid[row][column] = null;

        System.out.println("Successfully picked up " +
                getCandyDisplayName(candy) + "!");

        return true;
    }


    /**
     * Displays the candies currently available in the factory
     * using a properly formatted two-dimensional grid.
     * Empty slots are represented by a dash.
     */
    public void displayProductionGrid() {

        int rowHeaderWidth = 8;
        int cellWidth = 10;

        // Determine the required width based on the longest candy name.
        for (Candy candy : allowedCandies) {

            String candyName = getCandyDisplayName(candy);

            if (candyName.length() + 4 > cellWidth) {
                cellWidth = candyName.length() + 4;
            }
        }

        // Calculate the total width of the grid.
        int totalWidth = 1 + rowHeaderWidth + 1
                + gridSize * (cellWidth + 1);

        // Display factory title.
        printGridBorder(rowHeaderWidth, cellWidth);

        String title = getName().toUpperCase()
                + " - PRODUCTION GRID ("
                + gridSize + " x " + gridSize + ")";

        int padding = (totalWidth - title.length()) / 2;

        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }

        System.out.println(title);

        // Display top border.
        printGridBorder(rowHeaderWidth, cellWidth);

        // Display column headings.
        System.out.printf("|%-" + rowHeaderWidth + "s|", "Row/Col");

        for (int column = 0; column < gridSize; column++) {
            System.out.printf("%" + cellWidth + "d|", column);
        }

        System.out.println();

        // Separate column headings from candy slots.
        printGridBorder(rowHeaderWidth, cellWidth);

        // Display candy production grid.
        for (int row = 0; row < gridSize; row++) {

            System.out.printf("|%-" + rowHeaderWidth + "d|", row);

            for (int column = 0; column < gridSize; column++) {

                Candy candy = productionGrid[row][column];

                String cellContent;

                if (candy == null) {
                    cellContent = "[ - ]";
                }
                else {
                    cellContent = "[ "
                            + getCandyDisplayName(candy)
                            + " ]";
                }

                System.out.printf("%-" + cellWidth + "s|", cellContent);
            }

            System.out.println();

            // Separate each row.
            printGridBorder(rowHeaderWidth, cellWidth);
        }

        System.out.println("\nCoordinates start at 0.");
    }


    /**
     * Prints a horizontal border for the factory production grid.
     *
     * @param rowHeaderWidth the width of the row heading
     * @param cellWidth the width of each candy cell
     */
    private void printGridBorder(int rowHeaderWidth, int cellWidth) {

        System.out.print("+");

        for (int i = 0; i < rowHeaderWidth; i++) {
            System.out.print("-");
        }

        System.out.print("+");

        for (int column = 0; column < gridSize; column++) {

            for (int i = 0; i < cellWidth; i++) {
                System.out.print("-");
            }

            System.out.print("+");
        }

        System.out.println();
    }


    /**
     * Returns the candy type and flavor in a readable display format.
     *
     * @param candy the candy to display
     * @return the candy type with its flavor when applicable
     */
    private String getCandyDisplayName(Candy candy) {

        String candyName = candy.getCandyType();
        String flavor = candy.getCandyFlavor();

        if (flavor != null && !flavor.equalsIgnoreCase("N/A")) {
            candyName += " (" + flavor + ")";
        }

        return candyName;
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