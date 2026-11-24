
package controller;

import java.util.ArrayList;

import model.Game;
import model.Player;
import model.Factory;
import model.Candy;
import model.Inventory;

/**
 * Controls the player's interactions with factories,
 * including candy collection and candy searches.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class FactoryController {

    /** The game being controlled. */
    private Game game;


    /**
     * Constructs a FactoryController for the specified game.
     *
     * @param game the game containing the factories and player
     */
    public FactoryController(Game game) {
        this.game = game;
    }


    // ==========================================
    // FACTORY INTERACTION
    // ==========================================

    /**
     * Checks whether the player can interact with a factory.
     *
     * @param factory the factory to enter
     * @return true if the factory is valid and the player can enter
     */
    public boolean canEnterFactory(Factory factory) {

        return game.isRunning()
                && game.getPlayer() != null
                && factory != null;
    }


    // ==========================================
    // CANDY PICKUP
    // ==========================================

    /**
     * Allows the player to pick up one candy from a factory.
     *
     * @param factory the factory where the candy is collected
     * @param row the candy's row in the production grid
     * @param column the candy's column in the production grid
     * @return true if the candy is successfully collected
     */
    public boolean handleCandyPickup(
            Factory factory, int row, int column) {

        if (!canEnterFactory(factory)) {
            return false;
        }

        Player player = game.getPlayer();
        Inventory inventory = player.getInventory();

        return factory.pickUpCandy(row, column, inventory);
    }


    /**
     * Allows the player to pick up multiple candies
     * from a factory's production grid.
     *
     * @param factory the factory where the candies are collected
     * @param positions the row and column positions of the selected candies
     * @return the number of successfully collected candies
     */
    public int handleMultipleCandyPickup(
            Factory factory, int[][] positions) {

        if (!canEnterFactory(factory) || positions == null) {
            return 0;
        }

        int successfulPickups = 0;

        for (int[] position : positions) {

            if (position == null || position.length < 2) {
                continue;
            }

            if (game.getPlayer().getInventory().isFull()) {
                break;
            }

            boolean pickedUp = handleCandyPickup(
                    factory,
                    position[0],
                    position[1]
            );

            if (pickedUp) {
                successfulPickups++;
            }
        }

        return successfulPickups;
    }


    // ==========================================
    // CANDY SEARCH
    // ==========================================

    /**
     * Searches for factories that produce a specific candy type.
     *
     * @param candyType the candy type to search for
     * @return a list of factories that produce the specified candy
     */
    public ArrayList<Factory> searchCandies(String candyType) {

        return searchCandies(candyType, "");
    }


    /**
     * Searches for factories that produce a specific candy type
     * and flavor.
     *
     * @param candyType the candy type to search for
     * @param candyFlavor the candy flavor to search for
     * @return a list of matching factories
     */
    public ArrayList<Factory> searchCandies(
            String candyType, String candyFlavor) {

        ArrayList<Factory> matchingFactories = new ArrayList<>();

        if (candyType == null || candyType.trim().isEmpty()
                || game.getWorld() == null) {
            return matchingFactories;
        }

        for (Factory factory : game.getWorld().getFactories()) {

            boolean matches;

            if (candyFlavor == null || candyFlavor.trim().isEmpty()) {
                matches = factory.producesCandy(candyType.trim());
            }
            else {
                matches = factory.producesCandy(
                        candyType.trim(),
                        candyFlavor.trim()
                );
            }

            if (matches) {
                matchingFactories.add(factory);
            }
        }

        return matchingFactories;
    }


    // ==========================================
    // FACTORY GETTERS
    // ==========================================

    /**
     * Returns the production grid of a factory.
     *
     * @param factory the factory whose grid will be retrieved
     * @return the factory's production grid, or null if invalid
     */
    public Candy[][] getProductionGrid(Factory factory) {

        if (factory == null) {
            return null;
        }

        return factory.getProductionGrid();
    }


    /**
     * Returns a candy at a specific production grid position.
     *
     * @param factory the factory containing the candy
     * @param row the candy's row
     * @param column the candy's column
     * @return the candy at the specified position, or null if unavailable
     */
    public Candy getCandyAt(
            Factory factory, int row, int column) {

        if (factory == null) {
            return null;
        }

        return factory.getCandyAt(row, column);
    }


    /**
     * Returns all factories in the current game world.
     *
     * @return the list of factories
     */
    public ArrayList<Factory> getFactories() {

        if (game.getWorld() == null) {
            return new ArrayList<>();
        }

        return game.getWorld().getFactories();
    }

}
