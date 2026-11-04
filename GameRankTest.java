
/**
 * Tests the integration of the Star-Ranking System
 * with the Game, World, Factory, and Inventory classes.
 *
 * @author Katelyn & Shaira
 * @version 1.0
 */
public class GameRankTest {

    /**
     * Runs the Star-Ranking integration tests.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Game game = new Game();
        game.initializePlayer("Courier");

        Player player = game.getPlayer();

        Factory factory = new Factory(
            "Test Factory",
            0,
            0,
            new String[]{"Chocolate", "Strawberry"},
            6
        );

        int[] xpGains = {100, 400, 900, 1600, 2500};

        System.out.println("STAR-RANKING INTEGRATION TEST");

        displayResults(game, factory);

        for (int xp : xpGains) {

            player.addXP(xp);
            game.updateRankUnlocks();

            factory.expandProductionGrid(
                player.getFactoryGridSize()
            );

            displayResults(game, factory);
        }
    }

    /**
     * Displays the current upgrade values of each object.
     *
     * @param game the game being tested
     * @param factory the factory being tested
     */
    public static void displayResults(Game game, Factory factory) {

        Player player = game.getPlayer();

        System.out.println("---------------------------");
        System.out.println("Total XP: " + player.getTotalXP());
        System.out.println("Star Rank: " + player.getStarRank());
        System.out.println("World Grid: "
                + game.getWorld().getGridSize() + "x"
                + game.getWorld().getGridSize());
        System.out.println("Factory Grid: "
                + factory.getGridSize() + "x"
                + factory.getGridSize());
        System.out.println("Inventory Capacity: "
                + player.getInventory().getCapacity());
        System.out.println();
    }
}
