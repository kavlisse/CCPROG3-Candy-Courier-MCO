import java.io.ByteArrayInputStream;

/**
 * Comprehensive automated tests for the Candy Courier game.
 * Tests initial setup, XP progression, rank upgrades, world expansion,
 * factory initialization, house initialization, house delivery,
 * and Player's Home interactions.
 *
 * @author Katelyn & Shaira
 * @version 1.1
 */
public class GameIntegrationTest {

    /** Tracks the number of successful tests. */
    private static int passed = 0;

    /** Tracks the number of failed tests. */
    private static int failed = 0;

    /**
     * Runs all automated integration tests.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Game game = new Game();
        game.initializePlayer("Tester");

        System.out.println("\n======================================");
        System.out.println(" CANDY COURIER COMPREHENSIVE TESTS");
        System.out.println("======================================");

        // Initial game setup
        testInitialSetup(game);

        // TASK 5: HOUSE DELIVERY SYSTEM
        testHouseInitialization(game);
        testHouseDelivery();
        testHouseRankUnlocks(game);

        // Rank progression and world expansion
        testRankProgression(game);

        // Factory systems
        testFinalFactoryPositions(game);
        testFactorySpecializations(game);
        testFactoryGridExpansion(game);

        // Player movement
        testDiagonalMovement();

        // Duplicate prevention
        testDuplicatePrevention(game);

        // TASK 6: PLAYER'S HOME
        testPlayerHomeNoChoice();
        testPlayerHomeYesChoice();

        // Final results
        System.out.println("\n======================================");
        System.out.println(" FINAL TEST SUMMARY");
        System.out.println("======================================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));

        if (failed == 0) {
            System.out.println("\nALL TESTS PASSED!");
        } else {
            System.out.println("\nSOME TESTS FAILED. REVIEW THE RESULTS.");
        }

        System.out.println("======================================");
    }

    /**
     * Records and displays an individual test result.
     *
     * @param description the test being performed
     * @param result whether the test passed
     */
    public static void check(String description, boolean result) {

        if (result) {
            System.out.println("[PASS] " + description);
            passed++;
        } else {
            System.out.println("[FAIL] " + description);
            failed++;
        }
    }


    // ==========================================
    // INITIAL GAME SETUP
    // ==========================================

    /**
     * Tests the initial game setup.
     *
     * @param game the game being tested
     */
    public static void testInitialSetup(Game game) {

        System.out.println("\n--- INITIAL SETUP ---");

        Player player = game.getPlayer();
        World world = game.getWorld();

        check("Player is initialized",
                player != null);

        check("Player starts with 0 XP",
                player.getTotalXP() == 0);

        check("Player starts at 0 stars",
                player.getStarRank() == 0);

        check("Initial world size is 7 x 7",
                world.getGridSize() == 7);

        check("Player starts at the center of the world",
                player.getRow() == 3 &&
                player.getColumn() == 3);

        check("Player's inventory starts with 20 slots",
                player.getInventory().getCapacity() == 20);

        Building home = world.getBuildingAt(3, 3);

        check("Player's Home is placed at the center",
                home != null &&
                home.getSymbol() == 'H');

        check("Two initial factories are introduced",
                world.getFactories().size() == 2);
    }


    // ==========================================
    // TASK 5: HOUSE INITIALIZATION
    // ==========================================

    /**
     * Tests the initial house placements and requirements.
     *
     * @param game the game being tested
     */
    public static void testHouseInitialization(Game game) {

        System.out.println("\n--- HOUSE INITIALIZATION ---");

        World world = game.getWorld();

        check("Three initial houses are introduced",
                world.getHouses().size() == 3);

        House houseA = findHouse(world, "House A");

        check("House A exists",
                houseA != null);

        check("House A has the correct coordinates",
                houseA != null &&
                houseA.getRow() == 1 &&
                houseA.getColumn() == 1);

        check("House A has the correct XP reward",
                houseA != null &&
                houseA.getXPReward() == 100);

        House houseB = findHouse(world, "House B");

        check("House B exists",
                houseB != null);

        check("House B has the correct coordinates",
                houseB != null &&
                houseB.getRow() == 5 &&
                houseB.getColumn() == 2);

        House houseC = findHouse(world, "House C");

        check("House C exists",
                houseC != null);

        check("House C has the correct coordinates",
                houseC != null &&
                houseC.getRow() == 3 &&
                houseC.getColumn() == 6);
    }


    /**
     * Tests the house delivery system.
     */
    public static void testHouseDelivery() {

        System.out.println("\n--- HOUSE DELIVERY SYSTEM ---");

        Candy[] requiredCandies = {
            new Candy("Candy Corn")
        };

        int[] requiredQuantities = {2};

        House house = new House(
                "Test House",
                1,
                1,
                requiredCandies,
                requiredQuantities,
                100
        );

        Inventory inventory = new Inventory(20);

        check("House initially has no completed delivery",
                !house.isDelivered());

        check("House cannot be delivered without enough candies",
                !house.hasEnoughCandy(inventory));

        inventory.addCandy(new Candy("Candy Corn"));

        check("House still requires the correct quantity",
                !house.hasEnoughCandy(inventory));

        inventory.addCandy(new Candy("Candy Corn"));

        check("House recognizes sufficient candies",
                house.hasEnoughCandy(inventory));

        check("House delivery succeeds",
                house.deliverOrder(inventory));

        check("House is marked as delivered",
                house.isDelivered());

        check("Required candies are removed from inventory",
                inventory.getCandyCount() == 0);

        check("Completed house cannot be delivered again",
                !house.deliverOrder(inventory));
    }


    /**
     * Tests whether houses are introduced at their correct ranks.
     *
     * @param game the game being tested
     */
    public static void testHouseRankUnlocks(Game game) {

        System.out.println("\n--- HOUSE RANK UNLOCKS ---");

        game.awardXP(100);

        check("House D is introduced at 1 star",
                findHouse(game.getWorld(), "House D") != null);

        game.awardXP(400);

        check("Houses E to H are introduced at 2 stars",
                findHouse(game.getWorld(), "House E") != null &&
                findHouse(game.getWorld(), "House F") != null &&
                findHouse(game.getWorld(), "House G") != null &&
                findHouse(game.getWorld(), "House H") != null);

        game.awardXP(900);

        check("Houses I to M are introduced at 3 stars",
                findHouse(game.getWorld(), "House I") != null &&
                findHouse(game.getWorld(), "House J") != null &&
                findHouse(game.getWorld(), "House K") != null &&
                findHouse(game.getWorld(), "House L") != null &&
                findHouse(game.getWorld(), "House M") != null);

        game.awardXP(1600);

        check("Houses N to P are introduced at 4 stars",
                findHouse(game.getWorld(), "House N") != null &&
                findHouse(game.getWorld(), "House O") != null &&
                findHouse(game.getWorld(), "House P") != null);

        check("All 16 houses are introduced at 4 stars",
                game.getWorld().getHouses().size() == 16);
    }


    // ==========================================
    // RANK PROGRESSION TESTS
    // ==========================================

    /**
     * Tests rank progression and rank-based upgrades.
     *
     * @param game the game being tested
     */
    public static void testRankProgression(Game game) {

        System.out.println("\n--- RANK PROGRESSION ---");

        Player player = game.getPlayer();

        check("Player reaches 4 stars",
                player.getStarRank() == 4);

        check("World expands to 15 x 15",
                game.getWorld().getGridSize() == 15);

        check("Factory grid expands to 10 x 10",
                player.getFactoryGridSize() == 10);

        check("Inventory capacity expands to 100",
                player.getInventory().getCapacity() == 100);

        game.awardXP(2500);

        check("Player reaches 5 stars",
                player.getStarRank() == 5);

        check("Player has reached maximum rank",
                player.hasReachedMaxRank());

        check("World remains 15 x 15 at maximum rank",
                game.getWorld().getGridSize() == 15);
    }


    // ==========================================
    // FACTORY POSITION TESTS
    // ==========================================

        /**
         * Tests the final positions of all factories.
         *
         * @param game the game being tested
         */
        public static void testFinalFactoryPositions(Game game) {

        System.out.println("\n--- FACTORY POSITIONS ---");

        World world = game.getWorld();

        check("Ten factories are introduced",
                world.getFactories().size() == 10);

        Factory factory1 = findFactory(world, "Factory 1");

        check("Factory 1 has the correct final position",
                factory1 != null &&
                factory1.getRow() == 4 &&
                factory1.getColumn() == 10);

        Factory factory2 = findFactory(world, "Factory 2");

        check("Factory 2 has the correct final position",
                factory2 != null &&
                factory2.getRow() == 10 &&
                factory2.getColumn() == 4);

        Factory factory3 = findFactory(world, "Factory 3");

        check("Factory 3 has the correct final position",
                factory3 != null &&
                factory3.getRow() == 3 &&
                factory3.getColumn() == 3);

        Factory factory4 = findFactory(world, "Factory 4");

        check("Factory 4 has the correct final position",
                factory4 != null &&
                factory4.getRow() == 12 &&
                factory4.getColumn() == 12);

        Factory factory5 = findFactory(world, "Factory 5");

        check("Factory 5 has the correct final position",
                factory5 != null &&
                factory5.getRow() == 1 &&
                factory5.getColumn() == 11);

        Factory factory6 = findFactory(world, "Factory 6");

        check("Factory 6 has the correct final position",
                factory6 != null &&
                factory6.getRow() == 13 &&
                factory6.getColumn() == 7);

        Factory factory7 = findFactory(world, "Factory 7");

        check("Factory 7 has the correct final position",
                factory7 != null &&
                factory7.getRow() == 6 &&
                factory7.getColumn() == 13);

        Factory factory8 = findFactory(world, "Factory 8");

        check("Factory 8 has the correct final position",
                factory8 != null &&
                factory8.getRow() == 7 &&
                factory8.getColumn() == 1);

        Factory factory9 = findFactory(world, "Factory 9");

        check("Factory 9 has the correct final position",
                factory9 != null &&
                factory9.getRow() == 0 &&
                factory9.getColumn() == 0);

        Factory factory10 = findFactory(world, "Factory 10");

        check("Factory 10 has the correct final position",
                factory10 != null &&
                factory10.getRow() == 14 &&
                factory10.getColumn() == 2);
        }

    // ==========================================
    // FACTORY SPECIALIZATION TESTS
    // ==========================================

    /**
     * Tests whether factories produce their assigned candies.
     *
     * @param game the game being tested
     */
    public static void testFactorySpecializations(Game game) {

        System.out.println("\n--- FACTORY SPECIALIZATIONS ---");

        World world = game.getWorld();

        Factory factory1 = findFactory(world, "Factory 1");

        check("Factory 1 produces Candy Corn",
                factory1 != null &&
                factory1.producesCandy("Candy Corn"));

        check("Factory 1 produces Lollipop",
                factory1 != null &&
                factory1.producesCandy("Lollipop"));

        check("Factory 1 does not produce Gummy",
                factory1 != null &&
                !factory1.producesCandy("Gummy"));

        Factory factory2 = findFactory(world, "Factory 2");

        check("Factory 2 produces Gummy",
                factory2 != null &&
                factory2.producesCandy("Gummy"));

        check("Factory 2 produces Marshmallow",
                factory2 != null &&
                factory2.producesCandy("Marshmallow"));

        Factory factory4 = findFactory(world, "Factory 4");

        check("Factory 4 produces Tablea Lollipop",
                factory4 != null &&
                factory4.producesCandy("Lollipop", "Tablea"));

        check("Factory 4 does not produce Gummy Ube",
                factory4 != null &&
                !factory4.producesCandy("Gummy", "Ube"));
    }


    /**
     * Tests factory grid dimensions and expansion.
     *
     * @param game the game being tested
     */
    public static void testFactoryGridExpansion(Game game) {

        System.out.println("\n--- FACTORY GRID EXPANSION ---");

        boolean allFactoriesExpanded = true;

        for (Factory factory : game.getWorld().getFactories()) {

            if (factory.getGridSize() != 10) {
                allFactoriesExpanded = false;
            }
        }

        check("All ten factories have 10 x 10 grids",
                allFactoriesExpanded);

        Factory factory1 = findFactory(
                game.getWorld(), "Factory 1");

        if (factory1 != null) {

            factory1.expandProductionGrid(11);

            check("Factory grid can expand to 11 x 11",
                    factory1.getGridSize() == 11);

            factory1.expandProductionGrid(10);

            check("Factory grid does not shrink",
                    factory1.getGridSize() == 11);
        }
    }


    // ==========================================
    // PLAYER MOVEMENT TESTS
    // ==========================================

    /**
     * Tests diagonal movement before and after unlocking.
     */
    public static void testDiagonalMovement() {

        System.out.println("\n--- DIAGONAL MOVEMENT ---");

        Player lockedPlayer = new Player("Locked Tester");

        check("Diagonal movement is locked at 0 stars",
                !lockedPlayer.canMoveDiagonally());

        boolean lockedMove =
                lockedPlayer.move("down-right", 7);

        check("Diagonal movement is rejected at 0 stars",
                !lockedMove &&
                lockedPlayer.getRow() == 0 &&
                lockedPlayer.getColumn() == 0);

        Player player = new Player("Movement Tester");
        player.addXP(1400);

        check("Diagonal movement is unlocked at 3 stars",
                player.canMoveDiagonally());

        player.updatePosition(7, 7);

        boolean successfulMove =
                player.move("down-right", 15);

        check("Diagonal movement works at 3 stars",
                successfulMove &&
                player.getRow() == 8 &&
                player.getColumn() == 8);

        player.updatePosition(0, 0);

        boolean boundaryMove =
                player.move("up-left", 15);

        check("Diagonal movement respects world boundaries",
                !boundaryMove &&
                player.getRow() == 0 &&
                player.getColumn() == 0);

        // Test ordinary movement.
        player.updatePosition(5, 5);

        boolean normalMove = player.move("right", 15);

        check("Horizontal movement works",
                normalMove &&
                player.getRow() == 5 &&
                player.getColumn() == 6);

        // Test invalid movement.
        boolean invalidMove = player.move("north", 15);

        check("Invalid movement direction is rejected",
                !invalidMove &&
                player.getRow() == 5 &&
                player.getColumn() == 6);
    }


    // ==========================================
    // DUPLICATE PREVENTION
    // ==========================================

    /**
     * Tests whether repeated rank updates introduce duplicate buildings.
     *
     * @param game the game being tested
     */
    public static void testDuplicatePrevention(Game game) {

        System.out.println("\n--- DUPLICATE PREVENTION ---");

        game.updateRankUnlocks();
        game.updateRankUnlocks();

        check("Repeated rank updates do not duplicate factories",
                game.getWorld().getFactories().size() == 10);

        check("Repeated rank updates do not duplicate houses",
                game.getWorld().getHouses().size() == 16);
    }


    // ==========================================
    // TASK 6: PLAYER'S HOME TESTS
    // ==========================================

    /**
     * Tests that choosing No at the player's home does not
     * advance the day.
     */
    public static void testPlayerHomeNoChoice() {

        System.out.println("\n--- PLAYER'S HOME: NO CHOICE ---");

        Game game = createHomeTestGame("3\n4\n2\n0\n");

        game.handleTravel();

        check("Choosing No does not advance the day",
                game.getDayNum() == 1);

        check("Player remains at the home tile after choosing No",
                game.getPlayer().getRow() == 3 &&
                game.getPlayer().getColumn() == 3);
    }


    /**
     * Tests that choosing Yes advances the day and refreshes
     * all factory production slots.
     */
    public static void testPlayerHomeYesChoice() {

        System.out.println("\n--- PLAYER'S HOME: YES CHOICE ---");

        Game game = createHomeTestGame("3\n4\n1\n0\n");

        Factory factory = findFactory(
                game.getWorld(), "Factory 1");

        // Pick up one candy first to create a vacant slot.
        Inventory inventory = new Inventory(100);

        boolean pickedUpBeforeRefresh =
                factory != null &&
                factory.pickUpCandy(0, 0, inventory);

        game.handleTravel();

        check("Choosing Yes advances to the next day",
                game.getDayNum() == 2);

        boolean vacantSlotRefreshed =
                pickedUpBeforeRefresh &&
                factory != null &&
                factory.pickUpCandy(0, 0, inventory);

        check("The previously vacant slot is filled after the refresh",
                vacantSlotRefreshed);

        // Check the remaining factory slots.
        boolean allRemainingSlotsRefreshed = factory != null;

        if (factory != null) {

            for (int row = 0; row < factory.getGridSize(); row++) {

                for (int column = 0;
                     column < factory.getGridSize(); column++) {

                    if (row == 0 && column == 0) {
                        continue;
                    }

                    if (!factory.pickUpCandy(row, column, inventory)) {
                        allRemainingSlotsRefreshed = false;
                    }
                }
            }
        }

        check("All remaining factory slots contain candies after refresh",
                allRemainingSlotsRefreshed);
    }


    /**
     * Creates a game with scripted input for Player's Home tests.
     *
     * @param scriptedInput the travel and home menu choices
     * @return the initialized game
     */
    public static Game createHomeTestGame(String scriptedInput) {

        System.setIn(new ByteArrayInputStream(
                scriptedInput.getBytes()));

        Game game = new Game();
        game.initializePlayer("Home Tester");

        return game;
    }


    // ==========================================
    // HELPER METHODS
    // ==========================================

    /**
     * Finds a factory by its name.
     *
     * @param world the world containing the factory
     * @param name the factory name to find
     * @return the matching factory, or null if not found
     */
    public static Factory findFactory(World world, String name) {

        for (Factory factory : world.getFactories()) {

            if (factory.getName().equals(name)) {
                return factory;
            }
        }

        return null;
    }


    /**
     * Finds a house by its name.
     *
     * @param world the world containing the houses
     * @param name the house name to find
     * @return the matching house, or null if not found
     */
    public static House findHouse(World world, String name) {

        for (House house : world.getHouses()) {

            if (house.getName().equals(name)) {
                return house;
            }
        }

        return null;
    }

}