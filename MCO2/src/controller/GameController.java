
package controller;

import java.util.ArrayList;

import model.Game;
import model.PlayerRecord;
import model.Building;
import model.House;

/**
 * Controls the overall game flow and coordinates
 * the different controllers.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class GameController {

    /** The game being controlled. */
    private Game game;

    /** Controls player movement and position. */
    private PlayerController playerController;

    /** Controls factory interactions. */
    private FactoryController factoryController;

    /** Controls house interactions. */
    private HouseController houseController;

    /** Controls inventory interactions. */
    private InventoryController inventoryController;

    /** Controls leaderboard operations. */
    private LeaderboardController leaderboardController;

    /** Tracks whether the final player record was saved. */
    private boolean recordSaved;


    /**
     * Constructs a GameController and initializes
     * the other controllers.
     *
     * @param game the game being controlled
     * @param leaderboardController the leaderboard controller
     */
    public GameController(
            Game game,
            LeaderboardController leaderboardController) {

        if (game == null || leaderboardController == null) {
            throw new IllegalArgumentException(
                "Game and LeaderboardController cannot be null."
            );
        }

        this.game = game;
        this.leaderboardController = leaderboardController;

        this.playerController = new PlayerController(game);
        this.factoryController = new FactoryController(game);
        this.houseController = new HouseController(game);
        this.inventoryController = new InventoryController(game);

        this.recordSaved = false;

        this.leaderboardController.loadLeaderboard();
    }


    // ==========================================
    // GAME INITIALIZATION
    // ==========================================

    /**
     * Initializes a new player.
     *
     * @param playerName the player's name
     */
    public void startGame(String playerName) {

        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        // Prevent initializing another player in an existing session.
        if (game.getPlayer() != null || !game.isRunning()) {
            return;
        }

        game.initializePlayer(playerName.trim());
        recordSaved = false;
    }


    // ==========================================
    // MAIN MENU
    // ==========================================

    /**
     * Handles the player's main menu selection.
     *
     * @param choice the selected menu option
     * @return the corresponding menu action
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
    // PLAYER MOVEMENT
    // ==========================================

    /**
     * Moves the player in the specified direction.
     *
     * @param direction the movement direction
     * @return true if movement is successful
     */
    public boolean movePlayer(String direction) {

        if (!game.isRunning() || game.getPlayer() == null) {
            return false;
        }

        return playerController.movePlayer(direction);
    }


    /**
     * Returns the building at the player's current position.
     *
     * @return the current building, or null if none exists
     */
    public Building getCurrentBuilding() {

        return playerController.getCurrentBuilding();
    }


    /**
     * Returns all houses at the player's current position.
     *
     * @return the houses at the current location
     */
    public ArrayList<House> getHousesAtCurrentLocation() {

        int row = playerController.getCurrentRow();
        int column = playerController.getCurrentColumn();

        if (row < 0 || column < 0) {
            return new ArrayList<>();
        }

        return houseController.getHousesAt(row, column);
    }


    // ==========================================
    // PLAYER HOME
    // ==========================================

    /**
     * Handles interaction with the player's home.
     *
     * Advances the day when the player confirms.
     *
     * @param confirm true if the player wants to advance the day
     */
    public void handlePlayerHome(boolean confirm) {

        if (confirm && game.isRunning()
                && game.getPlayer() != null) {

            game.advanceDay();
        }
    }


    // ==========================================
    // HOUSE ORDER DELIVERY
    // ==========================================

    /**
     * Handles house order delivery through HouseController.
     *
     * @param house the house whose order will be delivered
     * @return true if delivery is successful
     */
    public boolean deliverHouseOrder(House house) {

        if (!game.isRunning() || game.getPlayer() == null) {
            return false;
        }

        boolean successful = houseController.deliverOrder(house);

        if (successful) {
            checkGameCompletion();
        }

        return successful;
    }


    // ==========================================
    // GAME COMPLETION
    // ==========================================

    /**
     * Checks whether the game has ended.
     *
     * Saves the final player record after automatic completion.
     */
    private void checkGameCompletion() {

        if (!game.isRunning()) {
            saveFinalRecord();
        }
    }


    /**
     * Ends the game and saves the player's final record.
     */
    public void quitGame() {

        if (game.getPlayer() == null) {
            game.endGame();
            return;
        }

        game.endGame();
        saveFinalRecord();
    }


    /**
     * Saves the player's final game record.
     *
     * Prevents duplicate saving after a successful save.
     */
    private void saveFinalRecord() {

        if (recordSaved || game.getPlayer() == null) {
            return;
        }

        PlayerRecord record = new PlayerRecord(
            game.getPlayer().getName(),
            game.getPlayer().getStarRank(),
            game.getDayNum(),
            game.getPlayer().getTotalSteps()
        );

        boolean saved =
            leaderboardController.savePlayerRecord(record);

        if (saved) {
            recordSaved = true;
        }
    }


    // ==========================================
    // CONTROLLER ACCESSORS
    // ==========================================

    /**
     * Returns the player controller.
     *
     * @return the player controller
     */
    public PlayerController getPlayerController() {
        return playerController;
    }


    /**
     * Returns the factory controller.
     *
     * @return the factory controller
     */
    public FactoryController getFactoryController() {
        return factoryController;
    }


    /**
     * Returns the house controller.
     *
     * @return the house controller
     */
    public HouseController getHouseController() {
        return houseController;
    }


    /**
     * Returns the inventory controller.
     *
     * @return the inventory controller
     */
    public InventoryController getInventoryController() {
        return inventoryController;
    }


    /**
     * Returns the leaderboard controller.
     *
     * @return the leaderboard controller
     */
    public LeaderboardController getLeaderboardController() {
        return leaderboardController;
    }


    /**
     * Returns the current game.
     *
     * @return the game model
     */
    public Game getGame() {
        return game;
    }


    // ==========================================
    // MENU ACTIONS
    // ==========================================

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
