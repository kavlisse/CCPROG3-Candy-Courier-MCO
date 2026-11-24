
package controller;

import model.Game;

/**
 * Controls the main game flow and coordinates
 * game-related actions.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class GameController {

    /** The game being controlled. */
    private Game game;


    /**
     * Constructs a GameController for the specified game.
     *
     * @param game the game to control
     */
    public GameController(Game game) {
        this.game = game;
    }


    // ==========================================
    // GAME INITIALIZATION
    // ==========================================

    /**
     * Starts a new game by initializing the player.
     *
     * @param playerName the name of the player
     */
    public void startGame(String playerName) {

        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        game.initializePlayer(playerName.trim());
    }


    // ==========================================
    // MAIN MENU
    // ==========================================

    /**
     * Processes the player's main menu selection.
     *
     * @param choice the selected menu option
     * @return the action associated with the selected option
     */
    public MenuAction handleMenuChoice(int choice) {

        switch (choice) {

            case 1:
                return MenuAction.TRAVEL;

            case 2:
                return MenuAction.INVENTORY;

            case 3:
                return MenuAction.SEARCH_CANDIES;

            case 4:
                return MenuAction.VIEW_ORDERS;

            case 5:
                return MenuAction.VIEW_PLAYER_INFO;

            case 0:
                quitGame();
                return MenuAction.QUIT;

            default:
                return MenuAction.INVALID;
        }
    }


    // ==========================================
    // PLAYER'S HOME
    // ==========================================

    /**
     * Handles the player's decision to end the current day.
     *
     * @param confirm true if the player wants to end the day
     */
    public void handlePlayerHome(boolean confirm) {

        if (confirm && game.isRunning()) {
            game.advanceDay();
        }
    }


    // ==========================================
    // GAME EXIT
    // ==========================================

    /**
     * Ends the current game session.
     */
    public void quitGame() {
        game.endGame();
    }


    // ==========================================
    // GENERAL GETTERS
    // ==========================================

    /**
     * Returns the current game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }


    /**
     * Represents the available main menu actions.
     */
    public enum MenuAction {
        TRAVEL,
        INVENTORY,
        SEARCH_CANDIES,
        VIEW_ORDERS,
        VIEW_PLAYER_INFO,
        QUIT,
        INVALID
    }

}
