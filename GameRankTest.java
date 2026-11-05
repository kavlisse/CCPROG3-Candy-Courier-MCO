
/**
 * Tests the XP and star-ranking system of Candy Courier.
 *
 * @author Katelyn & Shaira
 * @version 1.0
 */
public class GameRankTest {

    /**
     * Tests XP thresholds, star ranks, world sizes,
     * factory sizes, inventory capacity, and unlocks.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" CANDY COURIER - RANK TESTS");
        System.out.println("=================================");

        testRank(0, 0, 7, 6, 20);
        testRank(99, 0, 7, 6, 20);
        testRank(100, 1, 9, 7, 40);
        testRank(499, 1, 9, 7, 40);
        testRank(500, 2, 11, 8, 60);
        testRank(1399, 2, 11, 8, 60);
        testRank(1400, 3, 13, 9, 80);
        testRank(2999, 3, 13, 9, 80);
        testRank(3000, 4, 15, 10, 100);
        testRank(5499, 4, 15, 10, 100);
        testRank(5500, 5, 15, 10, 100);

        testDiagonalMovement();
        testMaximumRank();

        System.out.println("=================================");
        System.out.println(" RANK TESTING COMPLETED");
        System.out.println("=================================");
    }

    /**
     * Tests the player's rank and corresponding upgrades.
     *
     * @param xp the XP to award
     * @param expectedRank the expected star rank
     * @param expectedWorld the expected world size
     * @param expectedFactory the expected factory grid size
     * @param expectedInventory the expected inventory capacity
     */
    public static void testRank(int xp, int expectedRank,
                                int expectedWorld, int expectedFactory,
                                int expectedInventory) {

        Player player = new Player("Tester");
        player.addXP(xp);

        boolean passed =
            player.getStarRank() == expectedRank &&
            player.getWorldGridSize() == expectedWorld &&
            player.getFactoryGridSize() == expectedFactory &&
            player.getInventoryCapacity() == expectedInventory;

        if (passed) {
            System.out.println("[PASS] " + xp + " XP - "
                + expectedRank + " stars");
        } else {
            System.out.println("[FAIL] " + xp + " XP");
            System.out.println("Actual rank: " + player.getStarRank());
            System.out.println("Actual world: " + player.getWorldGridSize());
            System.out.println("Actual factory: " + player.getFactoryGridSize());
            System.out.println("Actual inventory: " + player.getInventoryCapacity());
        }
    }

    /**
     * Tests whether diagonal movement unlocks at 3 stars.
     */
    public static void testDiagonalMovement() {

        Player player = new Player("Tester");

        player.addXP(1399);
        boolean locked = !player.canMoveDiagonally();

        player.addXP(1);
        boolean unlocked = player.canMoveDiagonally();

        if (locked && unlocked) {
            System.out.println("[PASS] Diagonal movement unlock");
        } else {
            System.out.println("[FAIL] Diagonal movement unlock");
        }
    }

    /**
     * Tests whether the player reaches the maximum rank at 5500 XP.
     */
    public static void testMaximumRank() {

        Player player = new Player("Tester");
        player.addXP(5500);

        if (player.hasReachedMaxRank()) {
            System.out.println("[PASS] Maximum rank");
        } else {
            System.out.println("[FAIL] Maximum rank");
        }
    }
}
