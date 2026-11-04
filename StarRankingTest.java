
/**
 * Tests the Star-Ranking System of the Candy Courier game.
 * Checks XP thresholds, rank progression, and unlocked upgrades.
 *
 * @author Member 1
 * @version 1.0
 */
public class StarRankingTest {

    /**
     * Tests the player's promotion from 0 to 5 stars.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Player player = new Player("Courier");

        System.out.println("STAR-RANKING SYSTEM TEST");

        // Initial rank
        displayRank(player);

        // 1-star: 100 XP
        player.addXP(100);
        displayRank(player);

        // 2-star: 500 XP
        player.addXP(400);
        displayRank(player);

        // 3-star: 1,400 XP
        player.addXP(900);
        displayRank(player);

        // 4-star: 3,000 XP
        player.addXP(1600);
        displayRank(player);

        // 5-star: 5,500 XP
        player.addXP(2500);
        displayRank(player);
    }


    /**
     * Displays the player's current rank and unlocked upgrades.
     *
     * @param player the player being tested
     */
    public static void displayRank(Player player) {

        System.out.println("\n---------------------------");
        System.out.println("Total XP: " + player.getTotalXP());
        System.out.println("Star Rank: " + player.getStarRank());
        System.out.println("World Grid: "
                + player.getWorldGridSize() + "x"
                + player.getWorldGridSize());
        System.out.println("Factory Grid: "
                + player.getFactoryGridSize() + "x"
                + player.getFactoryGridSize());
        System.out.println("Inventory Capacity: "
                + player.getInventoryCapacity());
        System.out.println("Diagonal Movement: "
                + player.canMoveDiagonally());
        System.out.println("Maximum Rank Reached: "
                + player.hasReachedMaxRank());
    }

}
