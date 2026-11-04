
/**
 * Controls the Candy Courier game and coordinates
 * the upgrades unlocked through the player's star rank.
 *
 * @author Katelyn & Shaira
 * @version 1.0
 */
public class Game {

    /** The player participating in the game. */
    private Player player;

    /** The world in which the player travels. */
    private World world;

    /** The current day number. */
    private int dayNum;

    /** Indicates whether the game is currently running. */
    private boolean isRunning;

    /**
     * Constructs a new game with the initial world size and day number.
     */
    public Game() {
        this.world = new World(7);
        this.dayNum = 1;
        this.isRunning = true;
    }

    /**
     * Initializes the player using the provided name.
     *
     * @param name the name of the player
     */
    public void initializePlayer(String name) {
        this.player = new Player(name);
        updateRankUnlocks();
    }

    /**
     * Updates the world, factory grids, and inventory capacity
     * according to the player's current star rank.
     */
    public void updateRankUnlocks() {

        if (player != null) {

            world.expandWorld(player.getWorldGridSize());

            world.expandAllFactoryGrids(
                    player.getFactoryGridSize());

            player.getInventory().setCapacity(
                    player.getInventoryCapacity());
        }
    }

    
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

}
