
package controller;

import java.util.ArrayList;

import model.Game;
import model.House;
import model.Inventory;

/**
 * Controls the player's interactions with houses
 * and the delivery of house orders.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class HouseController {

    /** The game being controlled. */
    private Game game;


    /**
     * Constructs a HouseController for the specified game.
     *
     * @param game the game containing the houses and player
     */
    public HouseController(Game game) {
        this.game = game;
    }


    // ==========================================
    // HOUSE INTERACTION
    // ==========================================

    /**
     * Checks whether the player can interact with a house.
     *
     * @param house the house to interact with
     * @return true if the house is available for interaction
     */
    public boolean canInteractWithHouse(House house) {

        return game.isRunning()
                && game.getPlayer() != null
                && house != null;
    }


    // ==========================================
    // HOUSE ORDER DELIVERY
    // ==========================================

    /**
     * Checks whether the player's inventory contains
     * enough candies to complete a house order.
     *
     * @param house the house whose order will be checked
     * @return true if the player has all required candies
     */
    public boolean hasEnoughCandy(House house) {

        if (!canInteractWithHouse(house) || house.isDelivered()) {
            return false;
        }

        Inventory inventory = game.getPlayer().getInventory();

        return house.hasEnoughCandy(inventory);
    }


    /**
     * Attempts to deliver a house order.
     *
     * @param house the house whose order will be delivered
     * @return true if the delivery is successful
     */
    public boolean deliverOrder(House house) {

        if (!canInteractWithHouse(house)) {
            return false;
        }

        return game.deliverHouseOrder(house);
    }


    // ==========================================
    // HOUSE INFORMATION
    // ==========================================

    /**
     * Returns all houses in the current game world.
     *
     * @return the list of houses
     */
    public ArrayList<House> getHouses() {

        if (game.getWorld() == null) {
            return new ArrayList<>();
        }

        return game.getWorld().getHouses();
    }


    /**
     * Returns the houses located at a specific position.
     *
     * @param row the row coordinate
     * @param column the column coordinate
     * @return the houses at the specified position
     */
    public ArrayList<House> getHousesAt(int row, int column) {

        if (game.getWorld() == null) {
            return new ArrayList<>();
        }

        return game.getWorld().getHousesAt(row, column);
    }


    /**
     * Returns the number of houses whose orders have been delivered.
     *
     * @return the number of completed house orders
     */
    public int getCompletedOrderCount() {

        int completedCount = 0;

        for (House house : getHouses()) {

            if (house.isDelivered()) {
                completedCount++;
            }
        }

        return completedCount;
    }


    /**
     * Returns the number of house orders that remain incomplete.
     *
     * @return the number of pending house orders
     */
    public int getPendingOrderCount() {

        return getHouses().size() - getCompletedOrderCount();
    }

}
