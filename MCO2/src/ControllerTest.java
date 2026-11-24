
import java.io.File;
import java.util.ArrayList;

import model.*;
import controller.*;
import persistence.*;

/**
 * Functional tests for the Candy Courier Model and Controller layers.
 * Tests actual gameplay behavior using assertions.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class ControllerTest {

    private static int passed = 0;
    private static int failed = 0;

    /**
     * Runs an individual test and records its result.
     */
    private static void runTest(String name, Runnable test) {
        System.out.print("\nTEST: " + name + " ... ");

        try {
            test.run();
            passed++;
            System.out.println("PASS");
        } catch (Throwable error) {
            failed++;
            System.out.println("FAIL");
            System.out.println("Reason: " + error.getMessage());
        }
    }

    /**
     * Checks whether a condition is true.
     */
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Checks whether two integer values are equal.
     */
    private static void checkEquals(int expected, int actual,
                                    String message) {
        if (expected != actual) {
            throw new AssertionError(
                message + " Expected: " + expected
                + ", Actual: " + actual
            );
        }
    }

    /**
     * Creates a new initialized game.
     */
    private static Game createGame() {
        Game game = new Game();
        game.initializePlayer("Test Player");
        return game;
    }

    /**
     * Returns the first factory in the initialized world.
     */
    private static Factory getFirstFactory(Game game) {
        check(!game.getWorld().getFactories().isEmpty(),
              "No factories were initialized.");

        return game.getWorld().getFactories().get(0);
    }

    /**
     * Main method that executes all functional tests.
     */
    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" CANDY COURIER FUNCTIONAL TEST SUITE");
        System.out.println("======================================");

        runTest("1. Factory generation and allowed candies",
                ControllerTest::testFactoryGeneration);

        runTest("2. Factory grid access and defensive copying",
                ControllerTest::testFactoryGrid);

        runTest("3. Factory pickup and empty slots",
                ControllerTest::testFactoryPickup);

        runTest("4. Factory inventory capacity restriction",
                ControllerTest::testFactoryCapacity);

        runTest("5. Factory grid expansion and refresh",
                ControllerTest::testFactoryExpansion);

        runTest("6. Factory candy type and flavor searching",
                ControllerTest::testFactorySearch);

        runTest("7. Candy searching through FactoryController",
                ControllerTest::testCandySearchController);

        runTest("8. Multiple candy pickup",
                ControllerTest::testMultiplePickup);

        runTest("9. Inventory individual discarding",
                ControllerTest::testDiscardOne);

        runTest("10. Inventory multiple discarding",
                ControllerTest::testDiscardMultiple);

        runTest("11. Inventory capacity and candy storage",
                ControllerTest::testInventory);

        runTest("12. House order requirement validation",
                ControllerTest::testHouseRequirements);

        runTest("13. Successful house order delivery",
                ControllerTest::testHouseDelivery);

        runTest("14. House controller order counts",
                ControllerTest::testHouseController);

        runTest("15. XP and star-rank progression",
                ControllerTest::testRankProgression);

        runTest("16. Rank-based world and factory unlocking",
                ControllerTest::testRankUnlocks);

        runTest("17. Player diagonal movement restriction",
                ControllerTest::testDiagonalMovement);

        runTest("18. Player movement boundaries",
                ControllerTest::testMovementBoundaries);

        runTest("19. Day progression and factory refresh",
                ControllerTest::testDayProgression);

        runTest("20. Leaderboard sorting",
                ControllerTest::testLeaderboardSorting);

        runTest("21. Leaderboard file saving and loading",
                ControllerTest::testLeaderboardPersistence);

        runTest("22. Game completion and automatic record saving",
                ControllerTest::testGameCompletion);


        // ==========================================
        // ADDITIONAL CONTROLLER EDGE-CASE TESTS
        // ==========================================

        runTest("23. FactoryController rejects invalid multiple-pickup coordinates",
                ControllerTest::testMultiplePickupInvalidCoordinates);

        runTest("24. FactoryController stops multiple pickup when inventory is full",
                ControllerTest::testMultiplePickupFullInventory);

        runTest("25. FactoryController returns no results for nonexistent searches",
                ControllerTest::testNonexistentCandySearch);

        runTest("26. InventoryController rejects duplicate slot selections",
                ControllerTest::testDuplicateDiscardSlots);

        runTest("27. InventoryController rejects empty and null selections",
                ControllerTest::testEmptyDiscardSelections);

        runTest("28. InventoryController preserves the exact remaining candies",
                ControllerTest::testExactRemainingCandies);

        runTest("29. HouseController rejects insufficient candy quantities",
                ControllerTest::testHouseInsufficientQuantities);

        runTest("30. HouseController rejects incorrect candy flavors",
                ControllerTest::testHouseIncorrectFlavors);

        runTest("31. HouseController consumes exact requirements and preserves extras",
                ControllerTest::testHouseExactCandyConsumption);

        runTest("32. HouseController rejects duplicate delivery",
                ControllerTest::testHouseDuplicateDelivery);

        runTest("33. HouseController rejects houses from another game",
                ControllerTest::testForeignHouse);

        runTest("34. GameController saves a record when the player quits",
                ControllerTest::testManualQuitSavesRecord);

        runTest("35. GameController does not save a record more than once",
                ControllerTest::testRecordSavedOnlyOnce);

        runTest("36. GameController does not end before all orders are delivered",
                ControllerTest::testGameDoesNotEndPrematurely);

        runTest("37. GameController rejects invalid player names",
                ControllerTest::testInvalidPlayerNames);

        runTest("38. GameController does not initialize a second player",
                ControllerTest::testSecondPlayerInitialization);

        runTest("39. LeaderboardController rolls back records after save failure",
                ControllerTest::testLeaderboardSaveFailureRollback);

        runTest("40. Leaderboard file handler saves and loads multiple records",
                ControllerTest::testMultipleLeaderboardRecords);

        System.out.println("\n======================================");
        System.out.println(" TEST SUMMARY");
        System.out.println("======================================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));

        if (failed == 0) {
            System.out.println("\nALL TESTS PASSED!");
        } else {
            System.out.println("\nSOME TESTS FAILED. CHECK THE ERRORS ABOVE.");
        }

        System.out.println("======================================");
    }

    // ==========================================
    // FACTORY TESTS
    // ==========================================

    /**
     * Tests factory initialization and random candy generation.
     */
    private static void testFactoryGeneration() {

        Candy[] allowed = {
            new Candy("Gummy", "Ube"),
            new Candy("Lollipop", "Mango")
        };

        Factory factory = new Factory(
            "Test Factory", 0, 0, allowed, 6
        );

        checkEquals(6, factory.getGridSize(),
                    "Incorrect initial grid size.");

        Candy generated = factory.generateRandomCandy();

        check(generated != null, "Generated candy is null.");

        boolean valid =
            (generated.getCandyType().equals("Gummy")
                && generated.getCandyFlavor().equals("Ube"))
            ||
            (generated.getCandyType().equals("Lollipop")
                && generated.getCandyFlavor().equals("Mango"));

        check(valid, "Generated candy is not allowed.");

        Candy[][] grid = factory.getProductionGrid();

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {

                Candy candy = grid[row][column];

                check(candy != null,
                      "Initial production grid contains an empty slot.");

                check(
                    (candy.getCandyType().equals("Gummy")
                        && candy.getCandyFlavor().equals("Ube"))
                    ||
                    (candy.getCandyType().equals("Lollipop")
                        && candy.getCandyFlavor().equals("Mango")),
                    "Factory generated a candy outside its allowed list."
                );
            }
        }
    }

    /**
     * Tests grid access, invalid coordinates, and defensive copying.
     */
    private static void testFactoryGrid() {

        Factory factory = new Factory(
            "Grid Test", 0, 0,
            new Candy[] { new Candy("Gummy", "Ube") },
            3
        );

        Candy original = factory.getCandyAt(0, 0);

        check(original != null, "Expected a candy at (0,0).");

        check(factory.getCandyAt(-1, 0) == null,
              "Negative row should return null.");

        check(factory.getCandyAt(0, 3) == null,
              "Out-of-range column should return null.");

        Candy[][] copy = factory.getProductionGrid();

        copy[0][0] = null;

        check(factory.getCandyAt(0, 0) != null,
              "Changing copied grid affected original grid.");
    }

    /**
     * Tests successful and unsuccessful candy pickups.
     */
    private static void testFactoryPickup() {

        Factory factory = new Factory(
            "Pickup Test", 0, 0,
            new Candy[] { new Candy("Gummy", "Ube") },
            3
        );

        Inventory inventory = new Inventory(10);

        Candy selected = factory.getCandyAt(0, 0);

        boolean pickedUp = factory.pickUpCandy(0, 0, inventory);

        check(pickedUp, "Valid candy pickup failed.");

        checkEquals(1, inventory.getCandyCount(),
                    "Inventory count did not increase.");

        check(factory.getCandyAt(0, 0) == null,
              "Candy was not removed from the factory.");

        check(!factory.pickUpCandy(0, 0, inventory),
              "Picking up an empty slot should fail.");

        check(!factory.pickUpCandy(-1, 0, inventory),
              "Invalid coordinates should fail.");

        check(inventory.getCandies().contains(selected),
              "Picked-up candy was not stored in inventory.");
    }

    /**
     * Tests that a full inventory prevents candy pickup.
     */
    private static void testFactoryCapacity() {

        Factory factory = new Factory(
            "Capacity Test", 0, 0,
            new Candy[] { new Candy("Gummy", "Ube") },
            2
        );

        Inventory inventory = new Inventory(2);

        inventory.addCandy(new Candy("Gummy", "Ube"));
        inventory.addCandy(new Candy("Lollipop", "Mango"));

        Candy remaining = factory.getCandyAt(0, 0);

        check(inventory.isFull(), "Inventory should be full.");

        check(!factory.pickUpCandy(0, 0, inventory),
              "Pickup should fail when inventory is full.");

        checkEquals(2, inventory.getCandyCount(),
                    "Inventory count changed despite failed pickup.");

        check(factory.getCandyAt(0, 0) == remaining,
              "Factory candy was removed despite failed pickup.");
    }

    /**
     * Tests factory expansion and daily refresh.
     */
    private static void testFactoryExpansion() {

        Factory factory = new Factory(
            "Expansion Test", 0, 0,
            new Candy[] { new Candy("Gummy", "Ube") },
            2
        );

        Candy original = factory.getCandyAt(0, 0);

        factory.expandProductionGrid(3);

        checkEquals(3, factory.getGridSize(),
                    "Grid did not expand.");

        check(factory.getCandyAt(0, 0) == original,
              "Existing candy was not preserved.");

        check(factory.getCandyAt(2, 2) == null,
              "New grid slot should initially be empty.");

        factory.expandProductionGrid(2);

        checkEquals(3, factory.getGridSize(),
                    "Grid should not shrink.");

        factory.refreshProductionGrid();

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                check(factory.getCandyAt(row, column) != null,
                      "Refresh did not fill every slot.");
            }
        }
    }

    /**
     * Tests factory candy type and flavor restrictions.
     */
    private static void testFactorySearch() {

        Factory factory = new Factory(
            "Search Test", 0, 0,
            new Candy[] {
                new Candy("Gummy", "Ube"),
                new Candy("Lollipop", "Mango")
            },
            2
        );

        check(factory.producesCandy("Gummy"),
              "Factory should produce Gummy.");

        check(factory.producesCandy("gummy"),
              "Candy type search should ignore capitalization.");

        check(!factory.producesCandy("Marshmallow"),
              "Factory should not produce Marshmallow.");

        check(factory.producesCandy("Gummy", "Ube"),
              "Factory should produce Gummy Ube.");

        check(factory.producesCandy("Lollipop", "Mango"),
              "Factory should produce Lollipop Mango.");

        check(!factory.producesCandy("Gummy", "Mango"),
              "Factory should not produce Gummy Mango.");
    }

    /**
     * Tests candy searching through FactoryController.
     */
    private static void testCandySearchController() {

        Game game = createGame();

        FactoryController controller = new FactoryController(game);

        ArrayList<Factory> results =
            controller.searchCandies("Candy Corn");

        check(!results.isEmpty(),
              "Candy Corn search returned no factories.");

        for (Factory factory : results) {
            check(factory.producesCandy("Candy Corn"),
                  "Search returned an unrelated factory.");
        }

        ArrayList<Factory> flavorResults =
            controller.searchCandies("Lollipop", "Ube");

        check(!flavorResults.isEmpty(),
              "Lollipop Ube search returned no factories.");

        for (Factory factory : flavorResults) {
            check(factory.producesCandy("Lollipop", "Ube"),
                  "Flavor search returned an unrelated factory.");
        }
    }

    /**
     * Tests multiple candy pickup through FactoryController.
     */
    private static void testMultiplePickup() {

        Game game = createGame();

        FactoryController controller = new FactoryController(game);

        Factory factory = new Factory(
            "Multiple Pickup", 0, 0,
            new Candy[] { new Candy("Gummy", "Ube") },
            3
        );

        int initialCount = game.getPlayer()
                                .getInventory()
                                .getCandyCount();

        int[][] positions = {
            {0, 0},
            {0, 1},
            {1, 0}
        };

        int pickedUp =
            controller.handleMultipleCandyPickup(factory, positions);

        checkEquals(3, pickedUp,
                    "Incorrect number of candies picked up.");

        checkEquals(initialCount + 3,
                    game.getPlayer().getInventory().getCandyCount(),
                    "Inventory did not receive all selected candies.");
    }

    // ==========================================
    // INVENTORY TESTS
    // ==========================================

    /**
     * Tests individual candy discarding.
     */
    private static void testDiscardOne() {

        Game game = createGame();

        InventoryController controller =
            new InventoryController(game);

        Inventory inventory = game.getPlayer().getInventory();

        inventory.addCandy(new Candy("Gummy", "Ube"));
        inventory.addCandy(new Candy("Lollipop", "Mango"));

        int before = controller.getCandyCount();

        check(controller.discardOneCandy(1),
              "Discarding a valid slot failed.");

        checkEquals(before - 1, controller.getCandyCount(),
                    "Inventory count did not decrease.");

        check(!controller.discardOneCandy(999),
              "Invalid slot should not be discarded.");
    }

    /**
     * Tests multiple candy discarding.
     */
    private static void testDiscardMultiple() {

        Game game = createGame();

        InventoryController controller =
            new InventoryController(game);

        Inventory inventory = game.getPlayer().getInventory();

        for (int i = 0; i < 5; i++) {
            inventory.addCandy(new Candy("Gummy", "Ube"));
        }

        ArrayList<Integer> slots = new ArrayList<>();
        slots.add(1);
        slots.add(2);

        int removed = controller.discardMultipleCandies(slots);

        checkEquals(2, removed,
                    "Incorrect number of candies discarded.");

        checkEquals(3, controller.getCandyCount(),
                    "Inventory should contain three candies.");

        ArrayList<Integer> invalidSlots = new ArrayList<>();
        invalidSlots.add(1);
        invalidSlots.add(999);

        int before = controller.getCandyCount();

        int invalidResult =
            controller.discardMultipleCandies(invalidSlots);

        checkEquals(0, invalidResult,
                    "Invalid multiple discard should fail.");

        checkEquals(before, controller.getCandyCount(),
                    "Inventory changed after invalid discard.");
    }

    /**
     * Tests inventory storage and capacity.
     */
    private static void testInventory() {

        Inventory inventory = new Inventory(2);

        checkEquals(2, inventory.getCapacity(),
                    "Incorrect initial capacity.");

        check(inventory.addCandy(new Candy("Gummy", "Ube")),
              "First candy addition failed.");

        check(inventory.addCandy(new Candy("Lollipop", "Mango")),
              "Second candy addition failed.");

        check(inventory.isFull(),
              "Inventory should be full.");

        check(!inventory.addCandy(new Candy("Yema", "Ube")),
              "Inventory accepted candy beyond capacity.");

        checkEquals(2, inventory.getCandyCount(),
                    "Inventory count should remain two.");

        inventory.setCapacity(3);

        checkEquals(3, inventory.getCapacity(),
                    "Capacity did not update.");

        check(inventory.addCandy(new Candy("Yema", "Ube")),
              "Candy addition failed after capacity expansion.");
    }

    // ==========================================
    // HOUSE TESTS
    // ==========================================

    /**
     * Tests whether house requirements are correctly validated.
     */
    private static void testHouseRequirements() {

        Game game = createGame();

        House house = game.getWorld().getHouses().get(0);

        Inventory inventory = game.getPlayer().getInventory();

        check(!house.hasEnoughCandy(inventory),
              "Empty inventory should not satisfy House A.");

        Candy[] required = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        checkEquals(4, required.length,
                    "House A should have four candy requirements.");

        checkEquals(4, quantities[0],
                    "Candy Corn requirement should be four.");

        inventory.addCandy(new Candy("Candy Corn"));
        inventory.addCandy(new Candy("Candy Corn"));
        inventory.addCandy(new Candy("Candy Corn"));
        inventory.addCandy(new Candy("Candy Corn"));

        inventory.addCandy(new Candy("Gummy", "Ube"));
        inventory.addCandy(new Candy("Gummy", "Mango"));

        inventory.addCandy(new Candy("Lollipop", "Ube"));
        inventory.addCandy(new Candy("Lollipop", "Mango"));

        inventory.addCandy(new Candy("Marshmallow", "Ube"));
        inventory.addCandy(new Candy("Marshmallow", "Mango"));

        check(house.hasEnoughCandy(inventory),
              "Correct House A order should be accepted.");
    }

    /**
     * Tests successful house delivery and XP rewards.
     */
    private static void testHouseDelivery() {

        Game game = createGame();

        HouseController controller = new HouseController(game);

        House house = game.getWorld().getHouses().get(0);

        Inventory inventory = game.getPlayer().getInventory();

        // Attempt delivery without enough candy.
        check(!controller.deliverOrder(house),
              "House should reject insufficient inventory.");

        check(!house.isDelivered(),
              "House should remain pending after failed delivery.");

        // Supply the exact House A requirements.
        inventory.addCandy(new Candy("Candy Corn"));
        inventory.addCandy(new Candy("Candy Corn"));
        inventory.addCandy(new Candy("Candy Corn"));
        inventory.addCandy(new Candy("Candy Corn"));

        inventory.addCandy(new Candy("Gummy", "Ube"));
        inventory.addCandy(new Candy("Gummy", "Mango"));

        inventory.addCandy(new Candy("Lollipop", "Ube"));
        inventory.addCandy(new Candy("Lollipop", "Mango"));

        inventory.addCandy(new Candy("Marshmallow", "Ube"));
        inventory.addCandy(new Candy("Marshmallow", "Mango"));

        int initialXP = game.getPlayer().getTotalXP();

        check(controller.deliverOrder(house),
              "Valid House A delivery failed.");

        check(house.isDelivered(),
              "House was not marked as delivered.");

        checkEquals(initialXP + 100,
                    game.getPlayer().getTotalXP(),
                    "Incorrect XP reward.");

        checkEquals(0, inventory.getCandyCount(),
                    "Required candies were not consumed.");

        check(!controller.deliverOrder(house),
              "Already delivered house should not be delivered again.");
    }

    /**
     * Tests completed and pending order counts.
     */
    private static void testHouseController() {

        Game game = createGame();

        HouseController controller = new HouseController(game);

        checkEquals(3, controller.getPendingOrderCount(),
                    "Initial pending house count should be three.");

        checkEquals(0, controller.getCompletedOrderCount(),
                    "No houses should initially be completed.");

        ArrayList<House> houses = controller.getHouses();

        checkEquals(3, houses.size(),
                    "Incorrect initial house count.");

        House first = houses.get(0);
        first.setDelivered(true);

        checkEquals(1, controller.getCompletedOrderCount(),
                    "Completed count did not update.");

        checkEquals(2, controller.getPendingOrderCount(),
                    "Pending count did not update.");

        check(!controller.canInteractWithHouse(first),
              "A delivered house should no longer be available for an order.");
    }

    // ==========================================
    // PLAYER AND RANK TESTS
    // ==========================================

    /**
     * Tests all XP thresholds and star-rank progression.
     */
    private static void testRankProgression() {

        Player player = new Player("Rank Tester");

        int[] xpAwards = {100, 400, 900, 1600, 2500};

        int[] expectedXP = {100, 500, 1400, 3000, 5500};

        for (int i = 0; i < xpAwards.length; i++) {

            player.addXP(xpAwards[i]);

            checkEquals(i + 1, player.getStarRank(),
                        "Incorrect rank at XP threshold "
                        + expectedXP[i] + ".");

            checkEquals(expectedXP[i], player.getTotalXP(),
                        "Incorrect total XP.");
        }

        check(player.hasReachedMaxRank(),
              "Player should have reached maximum rank.");

        checkEquals(0, player.getXPNeededForNextRank(),
                    "Maximum rank should not require additional XP.");
    }

    /**
     * Tests rank-based world, factory, house, and inventory unlocking.
     */
    private static void testRankUnlocks() {

        Game game = createGame();

        int[] xpAwards = {100, 400, 900, 1600};

        int[] expectedWorldSizes = {9, 11, 13, 15};
        int[] expectedFactoryCounts = {3, 4, 8, 10};
        int[] expectedHouseCounts = {4, 8, 13, 16};
        int[] expectedInventoryCapacities = {40, 60, 80, 100};

        for (int i = 0; i < xpAwards.length; i++) {

            game.awardXP(xpAwards[i]);

            checkEquals(i + 1, game.getPlayer().getStarRank(),
                        "Incorrect unlocked rank.");

            checkEquals(expectedWorldSizes[i],
                        game.getWorld().getGridSize(),
                        "Incorrect world expansion.");

            checkEquals(expectedFactoryCounts[i],
                        game.getWorld().getFactories().size(),
                        "Incorrect factory unlock count.");

            checkEquals(expectedHouseCounts[i],
                        game.getWorld().getHouses().size(),
                        "Incorrect house unlock count.");

            checkEquals(expectedInventoryCapacities[i],
                        game.getPlayer().getInventory().getCapacity(),
                        "Incorrect inventory capacity.");
        }
    }

    /**
     * Tests the diagonal movement restriction.
     */
    private static void testDiagonalMovement() {

        Game game = createGame();

        Player player = game.getPlayer();

        check(!player.canMoveDiagonally(),
              "Diagonal movement should be locked initially.");

        check(!player.move("down-right", game.getWorld().getGridSize()),
              "Diagonal movement should fail at zero stars.");

        game.awardXP(1400);

        check(player.canMoveDiagonally(),
              "Diagonal movement should unlock at three stars.");

        check(player.move("down-right", game.getWorld().getGridSize()),
              "Diagonal movement should work at three stars.");
    }

    /**
     * Tests movement boundaries and invalid directions.
     */
    private static void testMovementBoundaries() {

        Player player = new Player("Movement Tester");

        player.updatePosition(3, 3);

        check(!player.move("up", 7) == false,
              "Valid upward movement should succeed.");

        player.updatePosition(0, 0);

        check(!player.move("up", 7),
              "Player should not move beyond the upper boundary.");

        check(!player.move("left", 7),
              "Player should not move beyond the left boundary.");

        check(!player.move("invalid", 7),
              "Invalid movement direction should fail.");

        check(player.move("right", 7),
              "Valid right movement should succeed.");
    }

    // ==========================================
    // DAY PROGRESSION TESTS
    // ==========================================

    /**
     * Tests day progression and factory production refresh.
     */
    private static void testDayProgression() {

        Game game = createGame();

        Factory factory = getFirstFactory(game);

        Candy[][] originalGrid = factory.getProductionGrid();

        // Empty a factory slot.
        factory.pickUpCandy(
            0, 0, game.getPlayer().getInventory()
        );

        check(factory.getCandyAt(0, 0) == null,
              "Factory slot should be empty after pickup.");

        int previousDay = game.getDayNum();

        game.advanceDay();

        checkEquals(previousDay + 1, game.getDayNum(),
                    "Day number did not increase.");

        for (int row = 0; row < factory.getGridSize(); row++) {
            for (int column = 0;
                 column < factory.getGridSize(); column++) {

                check(factory.getCandyAt(row, column) != null,
                      "Factory refresh left an empty slot.");
            }
        }
    }

    // ==========================================
    // LEADERBOARD TESTS
    // ==========================================

    /**
     * Tests leaderboard sorting by rank, days, and steps.
     */
    private static void testLeaderboardSorting() {

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addRecord(
            new PlayerRecord("Player A", 3, 10, 100)
        );

        leaderboard.addRecord(
            new PlayerRecord("Player B", 4, 20, 200)
        );

        leaderboard.addRecord(
            new PlayerRecord("Player C", 4, 10, 300)
        );

        leaderboard.addRecord(
            new PlayerRecord("Player D", 4, 10, 100)
        );

        ArrayList<PlayerRecord> records =
            leaderboard.getRecords();

        checkEquals(4, records.size(),
                    "Incorrect leaderboard size.");

        check(records.get(0).getPlayerName().equals("Player D"),
              "Incorrect first leaderboard position.");

        check(records.get(1).getPlayerName().equals("Player C"),
              "Incorrect second leaderboard position.");

        check(records.get(2).getPlayerName().equals("Player B"),
              "Incorrect third leaderboard position.");

        check(records.get(3).getPlayerName().equals("Player A"),
              "Incorrect fourth leaderboard position.");
    }

    /**
     * Tests actual leaderboard file saving and loading.
     */
    private static void testLeaderboardPersistence() {

        String testPath = "leaderboard_persistence_test.txt";

        File file = new File(testPath);

        try {
            file.delete();

            Leaderboard leaderboard = new Leaderboard();

            LeaderboardFileHandler handler =
                new LeaderboardFileHandler(testPath);

            ArrayList<PlayerRecord> records = new ArrayList<>();

            records.add(
                new PlayerRecord("Test Player", 3, 12, 150)
            );

            check(handler.saveRecords(records),
                  "Leaderboard file could not be saved.");

            check(file.exists(),
                  "Leaderboard file was not created.");

            ArrayList<PlayerRecord> loaded =
                handler.loadRecords();

            checkEquals(1, loaded.size(),
                        "Incorrect number of loaded records.");

            PlayerRecord record = loaded.get(0);

            check(record.getPlayerName().equals("Test Player"),
                  "Player name was not saved correctly.");

            checkEquals(3, record.getStarRank(),
                        "Star rank was not saved correctly.");

            checkEquals(12, record.getDaysSpent(),
                        "Days spent was not saved correctly.");

            checkEquals(150, record.getTotalSteps(),
                        "Total steps were not saved correctly.");

        } finally {
            file.delete();
        }
    }

    /**
     * Tests automatic game completion and final leaderboard saving.
     */
    private static void testGameCompletion() {

        String testPath = "leaderboard_completion_test.txt";

        File file = new File(testPath);

        try {
            file.delete();

            Game game = new Game();

            Leaderboard leaderboard = new Leaderboard();

            LeaderboardFileHandler fileHandler =
                new LeaderboardFileHandler(testPath);

            LeaderboardController leaderboardController =
                new LeaderboardController(leaderboard, fileHandler);

            GameController gameController =
                new GameController(game, leaderboardController);

            gameController.startGame("Completion Tester");

            // Reach five stars.
            game.awardXP(5500);

            checkEquals(5, game.getPlayer().getStarRank(),
                        "Player did not reach five stars.");

            check(game.isRunning(),
                  "Game should not end before all orders are delivered.");

            ArrayList<House> houses = game.getWorld().getHouses();

            checkEquals(16, houses.size(),
                        "Five-star world should contain sixteen houses.");

            // Mark every house as delivered except House O.
            for (House house : houses) {
                if (!house.getName().equals("House O")) {
                    house.setDelivered(true);
                }
            }

            // House O requires 100 Candy Cane.
            House finalHouse = houses.get(houses.size() - 2);

            // Find House O by name rather than relying on list position.
            for (House house : houses) {
                if (house.getName().equals("House O")) {
                    finalHouse = house;
                    break;
                }
            }

            check(finalHouse.getName().equals("House O"),
                  "Could not locate House O.");

            // Prepare enough inventory for the final order.
            Inventory inventory = game.getPlayer().getInventory();

            // At five stars, the normal inventory capacity is 100.
            checkEquals(100, inventory.getCapacity(),
                        "Five-star inventory capacity should be 100.");

            for (int i = 0; i < 100; i++) {
                check(inventory.addCandy(new Candy("Candy Cane")),
                      "Could not prepare final house order.");
            }

            // Deliver the last order through GameController.
            check(gameController.deliverHouseOrder(finalHouse),
                  "Final house delivery failed.");

            check(finalHouse.isDelivered(),
                  "Final house was not marked as delivered.");

            check(!game.isRunning(),
                  "Game should end after all sixteen orders are delivered.");

            check(file.exists(),
                  "Automatic completion did not create leaderboard file.");

            ArrayList<PlayerRecord> records =
                fileHandler.loadRecords();

            checkEquals(1, records.size(),
                        "Final player record was not saved exactly once.");

            check(records.get(0).getPlayerName()
                    .equals("Completion Tester"),
                  "Incorrect final player record.");

            checkEquals(5, records.get(0).getStarRank(),
                        "Final record should show five stars.");

            // Repeated quit calls must not save the same final record again.
            gameController.quitGame();
            gameController.quitGame();

            records = fileHandler.loadRecords();

            checkEquals(1, records.size(),
                        "Repeated quit calls created duplicate final records.");

        } finally {
            file.delete();
        }
    }

    // ==========================================
    // ADDITIONAL FACTORY CONTROLLER TESTS
    // ==========================================

    /**
     * Tests that invalid and malformed positions are ignored
     * while valid positions are still collected.
     */
    private static void testMultiplePickupInvalidCoordinates() {

        Game game = createGame();
        FactoryController controller = new FactoryController(game);
        Factory factory = getFirstFactory(game);

        int initialCount = game.getPlayer().getInventory().getCandyCount();
        int size = factory.getGridSize();

        int[][] positions = {
            {-1, 0},
            {size, 0},
            {0, size},
            null,
            {0},
            {0, 0}
        };

        int collected =
            controller.handleMultipleCandyPickup(factory, positions);

        checkEquals(1, collected,
                    "Only the valid position should be collected.");

        checkEquals(initialCount + 1,
                    game.getPlayer().getInventory().getCandyCount(),
                    "Invalid positions changed the inventory.");

        check(factory.getCandyAt(0, 0) == null,
              "The valid candy was not removed from the factory.");
    }


    /**
     * Tests that multiple pickup stops when the inventory is full.
     */
    private static void testMultiplePickupFullInventory() {

        Game game = createGame();
        FactoryController controller = new FactoryController(game);
        Factory factory = getFirstFactory(game);

        Inventory inventory = game.getPlayer().getInventory();

        // Fill the inventory using its actual current capacity.
        while (!inventory.isFull()) {
            check(inventory.addCandy(new Candy("Test Candy")),
                  "Could not fill the player's inventory.");
        }

        int initialCount = inventory.getCandyCount();
        Candy factoryCandy = factory.getCandyAt(0, 0);

        int[][] positions = {
            {0, 0},
            {0, 1},
            {1, 0}
        };

        int collected =
            controller.handleMultipleCandyPickup(factory, positions);

        checkEquals(0, collected,
                    "Pickup should stop when the inventory is full.");

        checkEquals(initialCount, inventory.getCandyCount(),
                    "Inventory changed despite being full.");

        check(factory.getCandyAt(0, 0) == factoryCandy,
              "Factory candy was removed despite the full inventory.");
    }


    /**
     * Tests searches for nonexistent, blank, and null candy types.
     */
    private static void testNonexistentCandySearch() {

        Game game = createGame();
        FactoryController controller = new FactoryController(game);

        check(controller.searchCandies("Candy That Does Not Exist").isEmpty(),
              "Nonexistent candy type should return no factories.");

        check(controller.searchCandies(
                "Candy That Does Not Exist", "Unknown Flavor").isEmpty(),
              "Nonexistent candy flavor should return no factories.");

        check(controller.searchCandies("").isEmpty(),
              "Blank candy type should return no factories.");

        check(controller.searchCandies(null).isEmpty(),
              "Null candy type should return no factories.");

        check(controller.searchCandies("Gummy", null).size() > 0,
              "Null flavor should search by candy type.");
    }


    // ==========================================
    // ADDITIONAL INVENTORY CONTROLLER TESTS
    // ==========================================

    /**
     * Tests that duplicate slot numbers are rejected without
     * changing the inventory.
     */
    private static void testDuplicateDiscardSlots() {

        Game game = createGame();
        InventoryController controller = new InventoryController(game);
        Inventory inventory = game.getPlayer().getInventory();

        inventory.addCandy(new Candy("Gummy", "Ube"));
        inventory.addCandy(new Candy("Lollipop", "Mango"));
        inventory.addCandy(new Candy("Yema", "Pandan"));

        ArrayList<Candy> before = controller.getCandies();

        ArrayList<Integer> duplicateSlots = new ArrayList<>();
        duplicateSlots.add(1);
        duplicateSlots.add(1);

        checkEquals(0, controller.discardMultipleCandies(duplicateSlots),
                    "Duplicate slots should be rejected.");

        checkEquals(before.size(), controller.getCandyCount(),
                    "Inventory count changed after duplicate selection.");

        ArrayList<Candy> after = controller.getCandies();

        for (int i = 0; i < before.size(); i++) {
            check(before.get(i) == after.get(i),
                  "Inventory contents changed after duplicate selection.");
        }
    }


    /**
     * Tests that empty and null slot selections are rejected.
     */
    private static void testEmptyDiscardSelections() {

        Game game = createGame();
        InventoryController controller = new InventoryController(game);
        Inventory inventory = game.getPlayer().getInventory();

        inventory.addCandy(new Candy("Gummy", "Ube"));

        int before = controller.getCandyCount();

        checkEquals(0, controller.discardMultipleCandies(null),
                    "Null slot selection should be rejected.");

        checkEquals(0,
                    controller.discardMultipleCandies(new ArrayList<Integer>()),
                    "Empty slot selection should be rejected.");

        checkEquals(before, controller.getCandyCount(),
                    "Inventory changed after empty/null selection.");
    }


    /**
     * Tests the exact candies remaining after multiple discards.
     */
    private static void testExactRemainingCandies() {

        Game game = createGame();
        InventoryController controller = new InventoryController(game);
        Inventory inventory = game.getPlayer().getInventory();

        Candy gummy = new Candy("Gummy", "Ube");
        Candy lollipop = new Candy("Lollipop", "Mango");
        Candy yema = new Candy("Yema", "Pandan");
        Candy marshmallow = new Candy("Marshmallow", "Ube");
        Candy candyCorn = new Candy("Candy Corn");

        inventory.addCandy(gummy);       // Slot 1
        inventory.addCandy(lollipop);    // Slot 2
        inventory.addCandy(yema);        // Slot 3
        inventory.addCandy(marshmallow); // Slot 4
        inventory.addCandy(candyCorn);   // Slot 5

        ArrayList<Integer> slotsToDiscard = new ArrayList<>();
        slotsToDiscard.add(2);
        slotsToDiscard.add(4);

        checkEquals(2, controller.discardMultipleCandies(slotsToDiscard),
                    "Two selected candies should be discarded.");

        ArrayList<Candy> remaining = controller.getCandies();

        checkEquals(3, remaining.size(),
                    "Three candies should remain.");

        check(remaining.get(0) == gummy,
              "Gummy should remain in the first position.");

        check(remaining.get(1) == yema,
              "Yema should remain in the second position.");

        check(remaining.get(2) == candyCorn,
              "Candy Corn should remain in the third position.");
    }


    // ==========================================
    // ADDITIONAL HOUSE CONTROLLER TESTS
    // ==========================================

    /**
     * Tests that insufficient quantities are rejected.
     */
    private static void testHouseInsufficientQuantities() {

        Game game = createGame();
        HouseController controller = new HouseController(game);
        House house = game.getWorld().getHouses().get(0);

        Inventory inventory = game.getPlayer().getInventory();

        Candy[] required = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        // Add one fewer than the required quantity of the first candy.
        for (int i = 0; i < required.length; i++) {
            int amount = quantities[i];

            if (i == 0) {
                amount = Math.max(0, amount - 1);
            }

            for (int j = 0; j < amount; j++) {
                Candy candy = required[i];

                if (candy.getCandyFlavor() == null
                        || candy.getCandyFlavor().equalsIgnoreCase("N/A")) {
                    inventory.addCandy(new Candy(candy.getCandyType()));
                } else {
                    inventory.addCandy(new Candy(
                        candy.getCandyType(), candy.getCandyFlavor()
                    ));
                }
            }
        }

        check(!controller.hasEnoughCandy(house),
              "House accepted an order with insufficient quantity.");

        int before = inventory.getCandyCount();

        check(!controller.deliverOrder(house),
              "House delivery succeeded with insufficient quantity.");

        checkEquals(before, inventory.getCandyCount(),
                    "Failed delivery consumed candies.");

        check(!house.isDelivered(),
              "House was marked delivered despite insufficient candy.");
    }


    /**
     * Tests that a candy of the wrong flavor cannot satisfy
     * a flavor-specific house requirement.
     */
    private static void testHouseIncorrectFlavors() {

        Inventory inventory = new Inventory(10);

        Candy[] requiredCandies = {
            new Candy("Lollipop", "Strawberry")
        };

        int[] requiredQuantities = {2};

        House house = new House(
            "Test House",
            0,
            0,
            requiredCandies,
            requiredQuantities,
            100
        );

        // Add one correct candy.
        inventory.addCandy(new Candy("Lollipop", "Strawberry"));

        // Add one candy with the wrong flavor.
        inventory.addCandy(new Candy("Lollipop", "Chocolate"));

        // The order requires two Strawberry Lollipops.
        check(!house.hasEnoughCandy(inventory),
            "Wrong candy flavor incorrectly satisfied the house order.");

        check(!house.deliverOrder(inventory),
            "House accepted delivery containing an incorrect flavor.");

        check(!house.isDelivered(),
            "House was marked delivered with an incorrect flavor.");

        check(inventory.getCandyCount() == 2,
            "Inventory changed despite unsuccessful delivery.");
    }

    /**
     * Tests that successful delivery consumes only the required
     * candy quantities and preserves extra candies.
     */
    private static void testHouseExactCandyConsumption() {

        Game game = createGame();
        HouseController controller = new HouseController(game);
        House house = game.getWorld().getHouses().get(0);

        Inventory inventory = game.getPlayer().getInventory();

        Candy[] required = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        for (int i = 0; i < required.length; i++) {

            Candy requiredCandy = required[i];

            for (int j = 0; j < quantities[i]; j++) {

                if (requiredCandy.getCandyFlavor() == null
                        || requiredCandy.getCandyFlavor().equalsIgnoreCase("N/A")) {
                    inventory.addCandy(new Candy(requiredCandy.getCandyType()));
                } else {
                    inventory.addCandy(new Candy(
                        requiredCandy.getCandyType(),
                        requiredCandy.getCandyFlavor()
                    ));
                }
            }
        }

        Candy extraCandy = new Candy("Extra Test Candy", "Special");
        check(inventory.addCandy(extraCandy),
              "Could not add extra candy for the preservation test.");

        int expectedConsumed = 0;

        for (int quantity : quantities) {
            expectedConsumed += quantity;
        }

        int before = inventory.getCandyCount();

        check(controller.deliverOrder(house),
              "Valid house order delivery failed.");

        check(house.isDelivered(),
              "House was not marked as delivered.");

        checkEquals(before - expectedConsumed,
                    inventory.getCandyCount(),
                    "Delivery did not consume the exact required quantity.");

        check(inventory.getCandies().contains(extraCandy),
              "Extra candy was incorrectly consumed.");
    }


    /**
     * Tests that a house cannot be delivered more than once.
     */
    private static void testHouseDuplicateDelivery() {

        Game game = createGame();
        HouseController controller = new HouseController(game);
        House house = game.getWorld().getHouses().get(0);

        Inventory inventory = game.getPlayer().getInventory();

        Candy[] required = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        for (int i = 0; i < required.length; i++) {
            for (int j = 0; j < quantities[i]; j++) {

                Candy candy = required[i];

                if (candy.getCandyFlavor() == null
                        || candy.getCandyFlavor().equalsIgnoreCase("N/A")) {
                    inventory.addCandy(new Candy(candy.getCandyType()));
                } else {
                    inventory.addCandy(new Candy(
                        candy.getCandyType(), candy.getCandyFlavor()
                    ));
                }
            }
        }

        check(controller.deliverOrder(house),
              "First valid delivery failed.");

        int xpAfterFirstDelivery = game.getPlayer().getTotalXP();
        int inventoryAfterFirstDelivery = inventory.getCandyCount();

        check(!controller.deliverOrder(house),
              "Second delivery of the same house should fail.");

        checkEquals(xpAfterFirstDelivery, game.getPlayer().getTotalXP(),
                    "Duplicate delivery awarded additional XP.");

        checkEquals(inventoryAfterFirstDelivery, inventory.getCandyCount(),
                    "Duplicate delivery consumed additional candies.");
    }


    /**
     * Tests that a house from another game cannot be delivered
     * through this game's HouseController.
     */
    private static void testForeignHouse() {

        Game firstGame = createGame();
        Game secondGame = createGame();

        HouseController controller = new HouseController(firstGame);

        House foreignHouse = secondGame.getWorld().getHouses().get(0);

        check(!controller.canInteractWithHouse(foreignHouse),
              "Controller accepted a house from another game.");

        check(!controller.hasEnoughCandy(foreignHouse),
              "Foreign house should not pass order validation.");

        check(!controller.deliverOrder(foreignHouse),
              "Foreign house should not be deliverable.");

        check(!foreignHouse.isDelivered(),
              "Foreign house was unexpectedly marked delivered.");
    }


    // ==========================================
    // ADDITIONAL GAME CONTROLLER TESTS
    // ==========================================

    /**
     * Creates a GameController using a temporary leaderboard file.
     */
    private static GameController createGameController(
            String filePath, Game game) {

        Leaderboard leaderboard = new Leaderboard();

        LeaderboardFileHandler handler =
            new LeaderboardFileHandler(filePath);

        LeaderboardController leaderboardController =
            new LeaderboardController(leaderboard, handler);

        return new GameController(game, leaderboardController);
    }


    /**
     * Tests that manually quitting saves the player's record.
     */
    private static void testManualQuitSavesRecord() {

        String path = "leaderboard_manual_quit_test.txt";
        File file = new File(path);

        try {
            file.delete();

            Game game = new Game();
            GameController controller = createGameController(path, game);

            controller.startGame("Manual Quit Tester");

            game.getPlayer().addXP(500);

            controller.quitGame();

            check(!game.isRunning(),
                  "Game should stop after manual quit.");

            check(file.exists(),
                  "Manual quit did not create the leaderboard file.");

            ArrayList<PlayerRecord> records =
                new LeaderboardFileHandler(path).loadRecords();

            checkEquals(1, records.size(),
                        "Manual quit should save exactly one record.");

            check(records.get(0).getPlayerName().equals("Manual Quit Tester"),
                  "Incorrect player name in saved record.");

            checkEquals(game.getPlayer().getStarRank(),
                        records.get(0).getStarRank(),
                        "Incorrect star rank in saved record.");

        } finally {
            file.delete();
        }
    }


    /**
     * Tests that repeated quit calls do not create duplicate records.
     */
    private static void testRecordSavedOnlyOnce() {

        String path = "leaderboard_single_save_test.txt";
        File file = new File(path);

        try {
            file.delete();

            Game game = new Game();
            GameController controller = createGameController(path, game);

            controller.startGame("Single Save Tester");

            controller.quitGame();
            controller.quitGame();
            controller.quitGame();

            ArrayList<PlayerRecord> records =
                new LeaderboardFileHandler(path).loadRecords();

            checkEquals(1, records.size(),
                        "Repeated quit calls created duplicate records.");

        } finally {
            file.delete();
        }
    }


    /**
     * Tests that reaching five stars alone does not end the game.
     */
    private static void testGameDoesNotEndPrematurely() {

        String path = "leaderboard_premature_end_test.txt";
        File file = new File(path);

        try {
            file.delete();

            Game game = new Game();
            GameController controller = createGameController(path, game);

            controller.startGame("Premature End Tester");

            game.awardXP(5500);

            checkEquals(5, game.getPlayer().getStarRank(),
                        "Player should reach five stars.");

            check(game.isRunning(),
                  "Game ended before all sixteen house orders were delivered.");

            House firstHouse = game.getWorld().getHouses().get(0);

            check(!controller.deliverHouseOrder(firstHouse),
                  "House without required candy should not be delivered.");

            check(game.isRunning(),
                  "Failed house delivery incorrectly ended the game.");

            check(!file.exists(),
                  "Leaderboard record should not be saved before game ends or player quits.");

        } finally {
            file.delete();
        }
    }


    /**
     * Tests that null and blank player names are rejected.
     */
    private static void testInvalidPlayerNames() {

        Game game = new Game();
        GameController controller = createGameController(
            "leaderboard_invalid_name_test.txt", game
        );

        controller.startGame(null);
        check(game.getPlayer() == null,
              "Null player name should not initialize a player.");

        controller.startGame("");
        check(game.getPlayer() == null,
              "Empty player name should not initialize a player.");

        controller.startGame("   ");
        check(game.getPlayer() == null,
              "Whitespace-only player name should not initialize a player.");

        controller.startGame("Valid Player");
        check(game.getPlayer() != null,
              "Valid player name should initialize a player.");

        check(game.getPlayer().getName().equals("Valid Player"),
              "Player name was not trimmed correctly.");
    }


    /**
     * Tests that startGame cannot replace an already initialized player.
     */
    private static void testSecondPlayerInitialization() {

        Game game = new Game();
        GameController controller = createGameController(
            "leaderboard_second_player_test.txt", game
        );

        controller.startGame("First Player");

        Player originalPlayer = game.getPlayer();

        controller.startGame("Second Player");

        check(game.getPlayer() == originalPlayer,
              "Starting again replaced the original player.");

        check(game.getPlayer().getName().equals("First Player"),
              "The original player's name was changed.");
    }


    // ==========================================
    // ADDITIONAL LEADERBOARD TESTS
    // ==========================================

    /**
     * Tests that a failed file save rolls back the newly added record.
     */
    private static void testLeaderboardSaveFailureRollback() {

        Leaderboard leaderboard = new Leaderboard();

        // Writing directly to a directory must fail.
        File directory = new File("leaderboard_save_failure_directory");

        directory.mkdir();

        LeaderboardFileHandler handler =
            new LeaderboardFileHandler(directory.getPath());

        LeaderboardController controller =
            new LeaderboardController(leaderboard, handler);

        checkEquals(0, controller.getLeaderboard().size(),
                    "Leaderboard should initially be empty.");

        boolean saved = controller.savePlayerRecord(
            new PlayerRecord("Save Failure Tester", 2, 5, 20)
        );

        check(!saved,
              "Saving to a nonexistent directory should fail.");

        checkEquals(0, controller.getLeaderboard().size(),
                    "Failed save did not roll back the in-memory record.");

        directory.delete();
    }


    /**
     * Tests saving and loading multiple leaderboard records.
     */
    private static void testMultipleLeaderboardRecords() {

        String path = "leaderboard_multiple_records_test.txt";
        File file = new File(path);

        try {
            file.delete();

            LeaderboardFileHandler handler =
                new LeaderboardFileHandler(path);

            ArrayList<PlayerRecord> records = new ArrayList<>();

            records.add(new PlayerRecord("Player One", 2, 10, 100));
            records.add(new PlayerRecord("Player Two", 4, 8, 90));
            records.add(new PlayerRecord("Player Three", 3, 12, 150));

            check(handler.saveRecords(records),
                  "Saving multiple records failed.");

            ArrayList<PlayerRecord> loaded = handler.loadRecords();

            checkEquals(3, loaded.size(),
                        "Not all leaderboard records were loaded.");

            check(loaded.get(0).getPlayerName().equals("Player One"),
                  "First player's name was not preserved.");

            check(loaded.get(1).getPlayerName().equals("Player Two"),
                  "Second player's name was not preserved.");

            check(loaded.get(2).getPlayerName().equals("Player Three"),
                  "Third player's name was not preserved.");

            checkEquals(4, loaded.get(1).getStarRank(),
                        "Second player's star rank was not preserved.");

            checkEquals(8, loaded.get(1).getDaysSpent(),
                        "Second player's days were not preserved.");

            checkEquals(90, loaded.get(1).getTotalSteps(),
                        "Second player's steps were not preserved.");

        } finally {
            file.delete();
        }
    }

}
