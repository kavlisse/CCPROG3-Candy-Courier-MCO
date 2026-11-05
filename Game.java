import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controls the Candy Courier game and coordinates
 * the player's activities and rank-based upgrades.
 *
 * @author Katelyn & Shaira
 * @version 1.3
 */
public class Game {

    // ==========================================
    // GENERAL GAME ATTRIBUTES
    // ==========================================

    /** The player participating in the game. */
    private Player player;

    /** The world in which the player travels. */
    private World world;

    /** The current day number. */
    private int dayNum;

    /** Indicates whether the game is currently running. */
    private boolean isRunning;

    /** Scanner used to receive player input. */
    private Scanner input;

    /** The highest rank for which houses have already been initialized. */
    private int highestHouseRankAdded = -1;

    /** Tracks the highest rank for which factories have been introduced. */
    private int highestFactoryRankAdded = -1;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    /**
     * Constructs a new game with the initial world size,
     * day number, and running status.
     */
    public Game() {
        this.world = new World(7);
        this.dayNum = 1;
        this.isRunning = true;
        this.input = new Scanner(System.in);
    }


    // ==========================================
    // GAME INITIALIZATION
    // ==========================================

    /**
     * Starts the game by asking for the player's name
     * and displaying the initial world and main menu.
     */
    public void startGame() {

        System.out.println("================================");
        System.out.println("       CANDY COURIER");
        System.out.println("================================");

        System.out.print("Enter your player name: ");
        String name = input.nextLine();

        initializePlayer(name);

        while (isRunning) {

            System.out.println("\n================================");
            System.out.println("DAY " + dayNum);
            System.out.println("================================");

            world.displayWorld(player);
            displayMainMenu();

            System.out.print("Enter your choice: ");
            String choiceInput = input.nextLine();

            if (choiceInput.equals("1")) {
                handleMenuChoice(1);
            }
            else if (choiceInput.equals("2")) {
                handleMenuChoice(2);
            }
            else if (choiceInput.equals("3")) {
                handleMenuChoice(3);
            }
            else if (choiceInput.equals("4")) {
                handleMenuChoice(4);
            }
            else if (choiceInput.equals("5")) {
                handleMenuChoice(5);
            }
            else if (choiceInput.equals("0")) {
                handleMenuChoice(0);
            }
            else {
                System.out.println("Invalid input. Please enter a valid menu option.");
            }
        }
    }


    /**
     * Initializes the player and places the player's home
     * at the center of the initial world grid.
     *
     * @param name the name of the player
     */
    public void initializePlayer(String name) {

        this.player = new Player(name);

        int center = world.getGridSize() / 2;

        player.updatePosition(center, center);

        PlayerHome home = new PlayerHome(center, center);

        world.placeBuilding(home, center, center);

        updateRankUnlocks();
    }


    // ==========================================
    // STAR-RANKING SYSTEM
    // ==========================================

    /**
     * Updates the world, factory grids, and inventory capacity
     * according to the player's current star rank.
     */
    public void updateRankUnlocks() {

        if (player != null) {

            int oldSize = world.getGridSize();
            int newSize = player.getWorldGridSize();

            // Expand the world when the player reaches a higher rank.
            if (newSize > oldSize) {

                int offset = (newSize - oldSize) / 2;

                world.expandWorld(newSize);

                // Adjust the player's coordinates after world expansion.
                player.updatePosition(
                        player.getRow() + offset,
                        player.getColumn() + offset);

                System.out.println("\nThe world has expanded to " +
                        newSize + " x " + newSize + "!");
            }

            // Expand all existing factory production grids.
            world.expandAllFactoryGrids(player.getFactoryGridSize());

            // Update the player's inventory capacity.
            player.getInventory().setCapacity(
                    player.getInventoryCapacity());

            // TASK 5: Initialize houses unlocked at the current rank.
            initializeNewHouses();

            // Introduce factories unlocked at the current rank.
            initializeFactories();
        }
    }


    /**
     * Awards experience points to the player and updates
     * rank-based unlocks when the player's rank changes.
     *
     * @param xp the experience points earned
     */
    public void awardXP(int xp) {

        int previousRank = player.getStarRank();

        player.addXP(xp);

        if (player.getStarRank() > previousRank) {

            updateRankUnlocks();

            System.out.println("Congratulations! You reached "
                    + player.getStarRank() + " stars!");

            System.out.println("Your inventory capacity is now "
                    + player.getInventory().getCapacity() + ".");
        }

        // The game ends only after the player reaches 5 stars AND
        // completes every house order in the world.
        checkGameCompletion();
    }


    /**
     * Checks whether the player has reached the required end-game condition.
     * The game ends only when the player has 5 stars and all 16 house orders
     * have been delivered.
     */
    private void checkGameCompletion() {

        if (player != null && player.hasReachedMaxRank()
                && areAllHouseOrdersDelivered()) {

            System.out.println("\n========================================");
            System.out.println("CONGRATULATIONS, " + player.getName() + "!");
            System.out.println("You are now a 5-star Candy Courier!");
            System.out.println("You have completed every delivery!");
            System.out.println("========================================");

            System.out.println("\n========== FINAL PLAYER STATS ==========");
            viewPlayerInfo();
            System.out.println("Thank you for playing Candy Courier!");

            isRunning = false;
        }
    }

    /**
     * Checks whether all 16 houses listed in the program overview
     * have been introduced and their orders have been completed.
     *
     * @return true if every house order has been delivered
     */
    private boolean areAllHouseOrdersDelivered() {

        ArrayList<House> houses = world.getHouses();

        if (houses.size() != 16) {
            return false;
        }

        for (House house : houses) {
            if (!house.isDelivered()) {
                return false;
            }
        }

        return true;
    }

    
    // ==========================================
    // FACTORY INITIALIZATION
    // ==========================================

    /**
     * Introduces factories according to the player's current star rank.
     * Each factory is created only once.
     */
    public void initializeFactories() {

        int rank = player.getStarRank();

        if (rank >= 0 && highestFactoryRankAdded < 0) {

            // Factory 1: Candy Corn and Lollipop
            placeFactory("Factory 1", 0, 6, new Candy[] {
                new Candy("Candy Corn", "N/A"),
                new Candy("Lollipop", "Ube"),
                new Candy("Lollipop", "Mango"),
                new Candy("Lollipop", "Macapuno"),
                new Candy("Lollipop", "Pandan")
            }, 0);

            // Factory 2: Gummy and Marshmallow
            placeFactory("Factory 2", 6, 0, new Candy[] {
                new Candy("Gummy", "Ube"),
                new Candy("Gummy", "Mango"),
                new Candy("Gummy", "Macapuno"),
                new Candy("Gummy", "Pandan"),
                new Candy("Marshmallow", "Ube"),
                new Candy("Marshmallow", "Mango"),
                new Candy("Marshmallow", "Macapuno"),
                new Candy("Marshmallow", "Pandan")
            }, 0);

            highestFactoryRankAdded = 0;
        }

        if (rank >= 1 && highestFactoryRankAdded < 1) {

            // Factory 3: Pastillas, Yema, and Polvoron
            placeFactory("Factory 3", 0, 0, new Candy[] {
                new Candy("Pastillas", "Ube"),
                new Candy("Pastillas", "Mango"),
                new Candy("Pastillas", "Macapuno"),
                new Candy("Pastillas", "Pandan"),
                new Candy("Yema", "Ube"),
                new Candy("Yema", "Mango"),
                new Candy("Yema", "Macapuno"),
                new Candy("Yema", "Pandan"),
                new Candy("Polvoron", "Ube"),
                new Candy("Polvoron", "Mango"),
                new Candy("Polvoron", "Macapuno"),
                new Candy("Polvoron", "Pandan")
            }, 1);

            highestFactoryRankAdded = 1;
        }

        if (rank >= 2 && highestFactoryRankAdded < 2) {

            // Factory 4: Six candy types with Tablea flavor
            placeFactory("Factory 4", 10, 10, new Candy[] {
                new Candy("Gummy", "Tablea"),
                new Candy("Lollipop", "Tablea"),
                new Candy("Marshmallow", "Tablea"),
                new Candy("Pastillas", "Tablea"),
                new Candy("Yema", "Tablea"),
                new Candy("Polvoron", "Tablea")
            }, 2);

            highestFactoryRankAdded = 2;
        }

        if (rank >= 3 && highestFactoryRankAdded < 3) {

            // Factory 5
            placeFactory("Factory 5", 0, 10, new Candy[] {
                new Candy("Gummy", "Jackfruit"),
                new Candy("Gummy", "Durian"),
                new Candy("Lollipop", "Jackfruit"),
                new Candy("Lollipop", "Durian"),
                new Candy("Marshmallow", "Jackfruit"),
                new Candy("Marshmallow", "Durian")
            }, 3);

            // Factory 6: All Lollipop flavors
            placeFactory("Factory 6", 12, 6, new Candy[] {
                new Candy("Lollipop", "Ube"),
                new Candy("Lollipop", "Mango"),
                new Candy("Lollipop", "Macapuno"),
                new Candy("Lollipop", "Pandan"),
                new Candy("Lollipop", "Tablea"),
                new Candy("Lollipop", "Jackfruit"),
                new Candy("Lollipop", "Durian")
            }, 3);

            // Factory 7: All Gummy flavors
            placeFactory("Factory 7", 5, 12, new Candy[] {
                new Candy("Gummy", "Ube"),
                new Candy("Gummy", "Mango"),
                new Candy("Gummy", "Macapuno"),
                new Candy("Gummy", "Pandan"),
                new Candy("Gummy", "Tablea"),
                new Candy("Gummy", "Jackfruit"),
                new Candy("Gummy", "Durian")
            }, 3);

            // Factory 8: All Marshmallow flavors
            placeFactory("Factory 8", 6, 0, new Candy[] {
                new Candy("Marshmallow", "Ube"),
                new Candy("Marshmallow", "Mango"),
                new Candy("Marshmallow", "Macapuno"),
                new Candy("Marshmallow", "Pandan"),
                new Candy("Marshmallow", "Tablea"),
                new Candy("Marshmallow", "Jackfruit"),
                new Candy("Marshmallow", "Durian")
            }, 3);

            highestFactoryRankAdded = 3;
        }

        if (rank >= 4 && highestFactoryRankAdded < 4) {

            // Factory 9
            placeFactory("Factory 9", 0, 0, new Candy[] {
                new Candy("Candy Corn", "N/A"),
                new Candy("Candy Cane", "N/A"),
                new Candy("Pastillas", "Ube"),
                new Candy("Pastillas", "Mango"),
                new Candy("Yema", "Ube"),
                new Candy("Yema", "Mango"),
                new Candy("Polvoron", "Ube"),
                new Candy("Polvoron", "Mango")
            }, 4);

            // Factory 10 / Factory X
            placeFactory("Factory 10", 14, 2, new Candy[] {
                new Candy("Pastillas", "Macapuno"),
                new Candy("Pastillas", "Pandan"),
                new Candy("Pastillas", "Jackfruit"),
                new Candy("Pastillas", "Durian"),
                new Candy("Yema", "Macapuno"),
                new Candy("Yema", "Pandan"),
                new Candy("Yema", "Jackfruit"),
                new Candy("Yema", "Durian"),
                new Candy("Polvoron", "Macapuno"),
                new Candy("Polvoron", "Pandan"),
                new Candy("Polvoron", "Jackfruit"),
                new Candy("Polvoron", "Durian")
            }, 4);

            highestFactoryRankAdded = 4;
        }
    }


    /**
     * Creates a factory and places it at the specified world coordinates.
     *
     * @param name the factory name
     * @param row the factory's row position
     * @param column the factory's column position
     * @param allowedCandies the candies that the factory can produce
     * @param introducedRank the rank at which the factory first becomes available
     */
    private void placeFactory(String name, int row, int column,
                              Candy[] allowedCandies, int introducedRank) {

        // Adjust the introduction coordinates if several ranks were unlocked
        // before the newly available factories were initialized.
        int introductionGridSize = 7 + (introducedRank * 2);
        int offset = (world.getGridSize() - introductionGridSize) / 2;

        int actualRow = row + offset;
        int actualColumn = column + offset;

        // Create the factory using the production-grid size available
        // when it is introduced. If the player skipped one or more ranks,
        // expand it afterward so newly unlocked slots remain empty until
        // the next day refresh event.
        int factoryIntroductionGridSize = 6 + introducedRank;

        Factory factory = new Factory(
                name,
                actualRow,
                actualColumn,
                allowedCandies,
                factoryIntroductionGridSize
        );

        factory.expandProductionGrid(player.getFactoryGridSize());
        world.placeBuilding(factory, actualRow, actualColumn);

        System.out.println(name + " has been introduced at (" +
                actualRow + ", " + actualColumn + ")!");
    }


    // ==========================================
    // TRAVEL AND MOVEMENT
    // ==========================================

    /**
     * Displays the available main menu options.
     */
    public void displayMainMenu() {

        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Travel");
        System.out.println("2. View Inventory");
        System.out.println("3. Search Candies");
        System.out.println("4. View Orders");
        System.out.println("5. View Player Info");
        System.out.println("0. Quit Game");
        System.out.println("===============================");
    }


    /**
     * Processes the player's main menu selection.
     *
     * @param choice the selected menu option
     */
    public void handleMenuChoice(int choice) {

        if (choice == 1) {
            handleTravel();
        }
        else if (choice == 2) {
            handleViewInventory();
        }
        else if (choice == 3) {
            searchCandies();
        }
        else if (choice == 4) {
            viewOrders();
        }
        else if (choice == 5) {
            viewPlayerInfo();
        }
        else if (choice == 0) {
            quitGame();
        }
        else {
            System.out.println("Invalid menu option. Please try again.");
        }
    }


    /**
     * Displays the player's current inventory.
     */
    public void handleViewInventory() {

        System.out.println("\n========== VIEW INVENTORY ==========");

        player.getInventory().displayInventory();

        System.out.println("====================================");
    }


    // ==========================================
    // TRAVEL AND BUILDING INTERACTIONS
    // ==========================================

    /**
     * Displays the available travel directions and
     * allows the player to move around the world.
     *
     * Diagonal movement is only available at 3 stars or higher.
     * Arriving at a factory or house triggers its respective event.
     */
    public void handleTravel() {

        boolean traveling = true;

        while (traveling && isRunning) {

            System.out.println("\n========== TRAVEL ==========");
            System.out.println("Current position: (" +
                    player.getRow() + ", " + player.getColumn() + ")");

            System.out.println("1. Up");
            System.out.println("2. Down");
            System.out.println("3. Left");
            System.out.println("4. Right");

            if (player.canMoveDiagonally()) {

                System.out.println("\nDiagonal Movement Unlocked!");
                System.out.println("5. Up-Left");
                System.out.println("6. Up-Right");
                System.out.println("7. Down-Left");
                System.out.println("8. Down-Right");
            }

            System.out.println("0. Return to Main Menu");
            System.out.println("============================");

            System.out.print("Choose a direction: ");

            String choiceInput = input.nextLine();

            String direction = "";

            if (choiceInput.equals("1")) {
                direction = "up";
            }
            else if (choiceInput.equals("2")) {
                direction = "down";
            }
            else if (choiceInput.equals("3")) {
                direction = "left";
            }
            else if (choiceInput.equals("4")) {
                direction = "right";
            }
            else if (choiceInput.equals("5") && player.canMoveDiagonally()) {
                direction = "up-left";
            }
            else if (choiceInput.equals("6") && player.canMoveDiagonally()) {
                direction = "up-right";
            }
            else if (choiceInput.equals("7") && player.canMoveDiagonally()) {
                direction = "down-left";
            }
            else if (choiceInput.equals("8") && player.canMoveDiagonally()) {
                direction = "down-right";
            }
            else if (choiceInput.equals("0")) {

                traveling = false;
                System.out.println("Returning to the main menu.");
            }
            else {
                System.out.println("Invalid travel option. Please try again.");
            }

            // Attempt movement only if a valid direction was selected.
            if (!direction.equals("")) {

                boolean moved = player.move(direction, world.getGridSize());

                if (moved) {

                    // Display the updated world after successful movement.
                    world.displayWorld(player);

                    // Check whether the player landed on a building.
                    Building building = world.getBuildingAt(
                            player.getRow(), player.getColumn());

                    if (building != null) {

                        // TASK 2: Handle factory interactions.
                        if (building.getSymbol() == 'F') {

                            System.out.println("\nYou have arrived at " +
                                    building.getName() + "!");

                            System.out.println("Would you like to enter?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            System.out.print("Choose an option: ");

                            String enterChoice = input.nextLine();

                            if (enterChoice.equals("1")) {
                                handleFactory((Factory) building);
                            }
                            else if (enterChoice.equals("2")) {
                                System.out.println(
                                        "You stayed outside the factory.");
                                System.out.println(
                                        "You may continue traveling.");
                            }
                            else {
                                System.out.println(
                                        "Invalid choice. You may continue traveling.");
                            }
                        }

                        // TASK 5: Handle house delivery interactions.
                        else if (building.getSymbol() == 'D') {

                            House house = (House) building;

                            // Completed houses no longer trigger delivery events.
                            if (!house.isDelivered()) {
                                handleHouse(house);
                            }
                        }

                        // TASK 6: Handle Player's Home interaction.
                        else if (building.getSymbol() == 'H') {
                            handlePlayerHome();
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays the factory menu and allows the player
     * to pick up candies or exit the factory.
     *
     * @param factory the factory the player has entered
     */
    public void handleFactory(Factory factory) {

        boolean insideFactory = true;

        while (insideFactory) {

            System.out.println("\n========== " +
                    factory.getName().toUpperCase() + " ==========");
            System.out.println("1. Pick Up Candy");
            System.out.println("0. Exit Factory");
            System.out.println("================================");

            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            if (choice.equals("1")) {
                handleCandyPickup(factory);
            }
            else if (choice.equals("0")) {
                insideFactory = false;
                System.out.println("You have exited the factory.");
                System.out.println("You may continue traveling.");
            }
            else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Allows the player to select multiple candies from the factory's
     * production grid and add them to their inventory.
     *
     * @param factory the factory where the player is picking up candy
     */
    public void handleCandyPickup(Factory factory) {

        boolean pickingCandies = true;

        while (pickingCandies) {

            // Display the updated production grid.
            factory.displayProductionGrid();

            System.out.print("\nEnter candy row: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid row. Please enter a number.");
                input.nextLine();
                continue;
            }

            int row = input.nextInt();
            input.nextLine();

            System.out.print("Enter candy column: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid column. Please enter a number.");
                input.nextLine();
                continue;
            }

            int column = input.nextInt();
            input.nextLine();

            // Attempt to pick up the selected candy.
            factory.pickUpCandy(row, column, player.getInventory());

            // Ask whether the player wants to pick up another candy.
            System.out.println("\nWould you like to pick up another candy?");
            System.out.println("1. Yes");
            System.out.println("0. No");
            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            if (choice.equals("0")) {
                pickingCandies = false;
                System.out.println("Returning to the factory menu.");
            }
            else if (!choice.equals("1")) {
                pickingCandies = false;
                System.out.println("Returning to the factory menu.");
            }
        }
    }

    // ==========================================
    // TASK 5: HOUSE DELIVERY SYSTEM
    // ==========================================

    /**
     * Handles the player's interaction with a house.
     * Checks the required candies, asks for delivery confirmation,
     * removes the required candies, and awards XP after delivery.
     *
     * @param house the house the player has arrived at
     */
    private void handleHouse(House house) {

        // Do not trigger another event for a completed house.
        if (house.isDelivered()) {
            return;
        }

        Inventory inventory = player.getInventory();

        System.out.println("\nYou have arrived at " +
                house.getName() + "!");

        // Check whether the player has all required candies.
        if (!house.hasEnoughCandy(inventory)) {

            System.out.println("You do not have enough candies to");
            System.out.println("complete this house's order yet.");
            System.out.println("You may continue exploring.");

            return;
        }

        // Ask for confirmation only when the complete order is available.
        System.out.println("\nYou have all the required candies!");
        System.out.println("Would you like to deliver the order?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Choose an option: ");

        String choice = input.nextLine();

        if (choice.equals("1")) {

            // Complete the delivery and remove the required candies.
            boolean successful = house.deliverOrder(inventory);

            if (successful) {

                System.out.println("\nDelivery successful!");
                System.out.println("You have completed the order for " +
                        house.getName() + "!");

                // Award the XP reward and process any rank progression.
                System.out.println("You earned " +
                        house.getXPReward() + " XP!");

                awardXP(house.getXPReward());

                if (isRunning) {
                    System.out.println("You may continue exploring.");
                }
            }
            else {
                System.out.println("Delivery could not be completed.");
                System.out.println("Your candies remain in your inventory.");
            }
        }
        else if (choice.equals("2")) {

            System.out.println("Delivery cancelled.");
            System.out.println("You may continue exploring.");
        }
        else {

            System.out.println("Invalid choice. No candies were delivered.");
            System.out.println("You may continue exploring.");
        }
    }

    // ==========================================
    // PLAYER'S HOME
    // ==========================================

    /**
     * Handles the player's interaction with their home.
     * Asks whether the player is ready to call it a day.
     *
     * If the player chooses yes, all factory production grids
     * are refreshed and the next day begins.
     */
    private void handlePlayerHome() {

        System.out.println("\nYou have arrived at your home!");
        System.out.println("Are you ready to call it a day?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Choose an option: ");

        String choice = input.nextLine();

        if (choice.equals("1")) {

            // Refresh all factory production grids.
            for (Factory factory : world.getFactories()) {
                factory.refreshProductionGrid();
            }

            // Proceed to the next day.
            dayNum++;

            System.out.println("\nAll factory candies have been refreshed!");
            System.out.println("Welcome to Day " + dayNum + "!");

        }
        else if (choice.equals("2")) {

            System.out.println("You decided to continue your day.");
            System.out.println("You may continue exploring.");

        }
        else {

            System.out.println("Invalid choice. You may continue exploring.");

        }
    }
    
    // ==========================================
    // SEARCH CANDIES
    // ==========================================

    /**
     * Allows the player to search for candies and displays
     * the factories that produce the specified candy.
     */
    public void searchCandies() {

        System.out.println("\n========== SEARCH CANDIES ==========");

        System.out.print("Enter candy type: ");
        String candyType = input.nextLine().trim();

        System.out.print("Enter candy flavor (leave blank for any flavor): ");
        String candyFlavor = input.nextLine().trim();

        boolean found = false;

        for (Factory factory : world.getFactories()) {

            boolean matches;

            if (candyFlavor.isEmpty()) {
                matches = factory.producesCandy(candyType);
            }
            else {
                matches = factory.producesCandy(candyType, candyFlavor);
            }

            if (matches) {
                System.out.println("Found in: " + factory.getName());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No factories produce the specified candy.");
        }

        System.out.println("====================================");
    }


    // ==========================================
    // VIEW PLAYER INFO
    // ==========================================

    /**
     * Displays the player's current statistics and rank progress.
     */
    public void viewPlayerInfo() {

        System.out.println("\n========== VIEW PLAYER INFO ==========");
        System.out.println("Player Name: " + player.getName());
        System.out.println("Total XP: " + player.getTotalXP());

        if (player.getStarRank() == 5) {
            System.out.println("XP Needed for Next Rank: Maximum rank reached!");
        }
        else {
            System.out.println("XP Needed for Next Rank: " +
                    player.getXPNeededForNextRank());
        }

        System.out.println("Star Rank: " + player.getStarRank() + "-star");
        System.out.println("Total Steps Traveled: " + player.getTotalSteps());
        System.out.println("Day Number: " + dayNum);
        System.out.println("======================================");
    }


    // ==========================================
    // VIEW ORDERS
    // ==========================================

    /**
     * Displays all accessible house orders, including their
     * coordinates, required candies, quantities, and XP rewards.
     */
    public void viewOrders() {

        System.out.println("\n========== VIEW ORDERS ==========");

        ArrayList<House> houses = world.getHouses();

        if (houses.isEmpty()) {
            System.out.println("There are currently no available house orders.");
        }
        else {

            for (House house : houses) {

                System.out.println("\n" + house.getName());

                System.out.println("Coordinates: (" +
                        house.getRow() + ", " + house.getColumn() + ")");

                System.out.println("Required Candies:");

                Candy[] requiredCandies = house.getRequiredCandies();
                int[] requiredQuantities = house.getRequiredQuantities();

                for (int i = 0; i < requiredCandies.length; i++) {

                    Candy candy = requiredCandies[i];

                    String candyName = candy.getCandyType();
                    String flavor = candy.getCandyFlavor();

                    if ("Any".equalsIgnoreCase(flavor)) {
                        candyName += " (any flavor)";
                    }
                    else if (flavor != null &&
                            !flavor.equalsIgnoreCase("N/A")) {
                        candyName += " (" + flavor + ")";
                    }

                    System.out.println("- " + requiredQuantities[i] +
                            "x " + candyName);
                }

                System.out.println("XP Reward: " + house.getXPReward() + " XP");
                System.out.println("Delivery Status: " +
                        (house.isDelivered() ? "Completed" : "Pending"));
            }
        }

        System.out.println("=================================");
    }


    /**
     * Initializes houses according to the player's current star rank.
     * Prevents houses from being placed more than once.
     */
    private void initializeNewHouses() {

        int currentRank = Math.min(player.getStarRank(), 4);

        for (int rank = highestHouseRankAdded + 1;
             rank <= currentRank; rank++) {

            placeHousesForRank(rank);

            highestHouseRankAdded = rank;
        }
    }


    /**
     * Places the houses introduced at the specified rank.
     *
     * @param rank the rank at which the houses are introduced
     */
    private void placeHousesForRank(int rank) {

        int introductionGridSize = 7 + (rank * 2);

        int offset = (world.getGridSize() - introductionGridSize) / 2;

        switch (rank) {

            case 0:
                // House A
                placeHouse("House A", 1, 1,
                    new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Gummy", "Any"),
                        new Candy("Lollipop", "Any"),
                        new Candy("Marshmallow", "Any")
                    },
                    new int[] {4, 2, 2, 2},
                    100, offset
                );

                // House B
                placeHouse("House B", 5, 2,
                    new Candy[] {
                        new Candy("Candy Corn")
                    },
                    new int[] {15},
                    150, offset
                );

                // House C
                placeHouse("House C", 3, 6,
                    new Candy[] {
                        new Candy("Gummy", "Any"),
                        new Candy("Marshmallow", "Any"),
                        new Candy("Pastillas", "Any"),
                        new Candy("Yema", "Any"),
                        new Candy("Polvoron", "Any")
                    },
                    new int[] {5, 5, 5, 5, 5},
                    250, offset
                );
                break;

            case 1:
                // House D
                placeHouse("House D", 8, 6,
                    new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Candy Cane"),
                        new Candy("Pastillas", "Any"),
                        new Candy("Yema", "Any"),
                        new Candy("Polvoron", "Any")
                    },
                    new int[] {5, 5, 10, 10, 10},
                    400, offset
                );
                break;

            case 2:
                // House E
                placeHouse("House E", 0, 6,
                    new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Yema", "Any"),
                        new Candy("Pastillas", "Any"),
                        new Candy("Lollipop", "Ube"),
                        new Candy("Lollipop", "Macapuno")
                    },
                    new int[] {10, 5, 5, 5, 5},
                    300, offset
                );

                // House F
                placeHouse("House F", 10, 1,
                    new Candy[] {
                        new Candy("Gummy", "Tablea"),
                        new Candy("Marshmallow", "Tablea"),
                        new Candy("Pastillas", "Tablea"),
                        new Candy("Polvoron", "Tablea")
                    },
                    new int[] {7, 7, 8, 8},
                    300, offset
                );

                // House G
                placeHouse("House G", 4, 0,
                    new Candy[] {
                        new Candy("Pastillas", "Any"),
                        new Candy("Pastillas", "Durian"),
                        new Candy("Pastillas", "Jackfruit")
                    },
                    new int[] {20, 10, 10},
                    400, offset
                );

                // House H
                placeHouse("House H", 6, 10,
                    new Candy[] {
                        new Candy("Polvoron", "Any"),
                        new Candy("Yema", "Any"),
                        new Candy("Pastillas", "Any"),
                        new Candy("Gummy", "Mango"),
                        new Candy("Marshmallow", "Pandan")
                    },
                    new int[] {10, 5, 5, 5, 5},
                    300, offset
                );
                break;

            case 3:
                // House I
                placeHouse("House I", 0, 3,
                    new Candy[] {
                        new Candy("Lollipop", "Jackfruit"),
                        new Candy("Lollipop", "Durian"),
                        new Candy("Lollipop", "Pandan"),
                        new Candy("Lollipop", "Ube"),
                        new Candy("Lollipop", "Macapuno")
                    },
                    new int[] {9, 9, 9, 9, 9},
                    450, offset
                );

                // House J
                placeHouse("House J", 3, 0,
                    new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Marshmallow", "Mango"),
                        new Candy("Yema", "Mango"),
                        new Candy("Pastillas", "Mango"),
                        new Candy("Gummy", "Mango")
                    },
                    new int[] {5, 6, 7, 8, 9},
                    350, offset
                );

                // House K
                placeHouse("House K", 9, 0,
                    new Candy[] {
                        new Candy("Gummy", "Ube"),
                        new Candy("Gummy", "Mango"),
                        new Candy("Gummy", "Macapuno"),
                        new Candy("Gummy", "Pandan")
                    },
                    new int[] {10, 10, 10, 10},
                    400, offset
                );

                // House L
                placeHouse("House L", 12, 5,
                    new Candy[] {
                        new Candy("Pastillas", "Ube"),
                        new Candy("Yema", "Ube"),
                        new Candy("Polvoron", "Ube")
                    },
                    new int[] {10, 10, 10},
                    300, offset
                );

                // House M
                placeHouse("House M", 12, 12,
                    new Candy[] {
                        new Candy("Pastillas", "Pandan"),
                        new Candy("Yema", "Pandan"),
                        new Candy("Polvoron", "Pandan")
                    },
                    new int[] {10, 10, 10},
                    300, offset
                );
                break;

            case 4:
                // House N
                placeHouse("House N", 3, 14,
                    new Candy[] {
                        new Candy("Candy Cane"),
                        new Candy("Polvoron", "Jackfruit"),
                        new Candy("Polvoron", "Durian"),
                        new Candy("Lollipop", "Tablea")
                    },
                    new int[] {16, 3, 3, 3},
                    250, offset
                );

                // House O
                placeHouse("House O", 14, 0,
                    new Candy[] {
                        new Candy("Candy Cane")
                    },
                    new int[] {100},
                    1000, offset
                );

                // House P
                placeHouse("House P", 14, 10,
                    new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Candy Cane"),
                        new Candy("Pastillas", "Macapuno"),
                        new Candy("Yema", "Macapuno"),
                        new Candy("Polvoron", "Macapuno")
                    },
                    new int[] {5, 5, 5, 5, 5},
                    250, offset
                );
                break;
        }
    }


    /**
     * Creates and places a house in the world.
     *
     * @param name the name of the house
     * @param row the introduction row coordinate
     * @param column the introduction column coordinate
     * @param candies the candies required by the house
     * @param quantities the required quantity of each candy
     * @param xp the XP reward for completing the order
     * @param offset the coordinate adjustment caused by world expansion
     */
    private void placeHouse(String name, int row, int column,
                            Candy[] candies, int[] quantities,
                            int xp, int offset) {

        int actualRow = row + offset;
        int actualColumn = column + offset;

        House house = new House(
            name,
            actualRow,
            actualColumn,
            candies,
            quantities,
            xp
        );

        world.placeBuilding(house, actualRow, actualColumn);
    }


    // ==========================================
    // GENERAL GAME GETTERS
    // ==========================================

    /**
     * Returns the current player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Returns the current world.
     *
     * @return the world
     */
    public World getWorld() {
        return world;
    }


    /**
     * Returns the current day number.
     *
     * @return the current day
     */
    public int getDayNum() {
        return dayNum;
    }


    /**
     * Returns whether the game is currently running.
     *
     * @return true if the game is running; false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }


    // ==========================================
    // GAME EXIT
    // ==========================================

    /**
     * Ends the current game session.
     */
    public void quitGame() {

        // MCO1 requirement: display the player's statistics before exiting.
        System.out.println("\n========== FINAL PLAYER STATS ==========");
        viewPlayerInfo();

        System.out.println("Thank you for playing Candy Courier!");
        System.out.println("Game session ended.");

        isRunning = false;
    }

    // ==========================================
    // MAIN METHOD
    // ==========================================

    /**
     * Main entry point of the Candy Courier game.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Game game = new Game();
        game.startGame();
    }

}
