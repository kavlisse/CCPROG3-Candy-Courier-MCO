
/**
 * Represents the player's home in the Candy Courier game.
 * Serves as the player's starting location in the world.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class PlayerHome extends Building {

    /**
     * Constructs the player's home at the specified world position.
     *
     * @param row the row position of the home
     * @param column the column position of the home
     */
    public PlayerHome(int row, int column) {
        super("Player's Home", row, column, 'H');
    }

}
