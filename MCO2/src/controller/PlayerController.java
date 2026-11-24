
package controller;

import model.Game;
import model.Player;
import model.Building;

/**
 * Controls the player's movement and position
 * within the game world.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class PlayerController {

    /** The game being controlled. */
    private Game game;


    /**
     * Constructs a PlayerController for the specified game.
     *
     * @param game the game containing the player and world
     */
    public PlayerController(Game game) {
        this.game = game;
    }


    // ==========================================
    // PLAYER MOVEMENT
    // ==========================================

    /**
     * Moves the player in the specified direction.
     *
     * Diagonal movement is only available at 3 stars or higher.
     *
     * @param direction the direction in which the player moves
     * @return true if the movement is successful; false otherwise
     */
    public boolean movePlayer(String direction) {

        Player player = game.getPlayer();

        if (player == null || !game.isRunning()) {
            return false;
        }

        return player.move(direction, game.getWorld().getGridSize());
    }


    // ==========================================
    // PLAYER POSITION
    // ==========================================

    /**
     * Returns the building at the player's current position.
     *
     * @return the building at the current position, or null if none exists
     */
    public Building getCurrentBuilding() {

        Player player = game.getPlayer();

        if (player == null) {
            return null;
        }

        return game.getWorld().getBuildingAt(
                player.getRow(),
                player.getColumn()
        );
    }


    /**
     * Checks whether diagonal movement is unlocked.
     *
     * @return true if diagonal movement is available
     */
    public boolean canMoveDiagonally() {

        Player player = game.getPlayer();

        return player != null && player.canMoveDiagonally();
    }


    /**
     * Returns the player's current row.
     *
     * @return the player's row position
     */
    public int getCurrentRow() {

        if (game.getPlayer() == null) {
            return -1;
        }

        return game.getPlayer().getRow();
    }


    /**
     * Returns the player's current column.
     *
     * @return the player's column position
     */
    public int getCurrentColumn() {

        if (game.getPlayer() == null) {
            return -1;
        }

        return game.getPlayer().getColumn();
    }

}
