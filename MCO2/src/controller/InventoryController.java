
package controller;

import java.util.ArrayList;

import model.Game;
import model.Player;
import model.Inventory;
import model.Candy;

/**
 * Controls the player's inventory interactions,
 * including viewing and discarding candies.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class InventoryController {

    /** The game being controlled. */
    private Game game;


    /**
     * Constructs an InventoryController for the specified game.
     *
     * @param game the game containing the player's inventory
     */
    public InventoryController(Game game) {
        this.game = game;
    }


    // ==========================================
    // INVENTORY INFORMATION
    // ==========================================

    /**
     * Returns the candies currently in the player's inventory.
     *
     * @return a list of the player's candies
     */
    public ArrayList<Candy> getCandies() {

        if (game.getPlayer() == null) {
            return new ArrayList<>();
        }

        return game.getPlayer().getInventory().getCandies();
    }


    /**
     * Returns the current number of candies in the inventory.
     *
     * @return the number of candies
     */
    public int getCandyCount() {

        if (game.getPlayer() == null) {
            return 0;
        }

        return game.getPlayer().getInventory().getCandyCount();
    }


    /**
     * Returns the maximum inventory capacity.
     *
     * @return the inventory capacity
     */
    public int getCapacity() {

        if (game.getPlayer() == null) {
            return 0;
        }

        return game.getPlayer().getInventory().getCapacity();
    }


    // ==========================================
    // DISCARD ONE CANDY
    // ==========================================

    /**
     * Discards one candy using its inventory slot number.
     *
     * @param slotNumber the selected inventory slot
     * @return true if the candy is successfully discarded
     */
    public boolean discardOneCandy(int slotNumber) {

        if (game.getPlayer() == null) {
            return false;
        }

        Inventory inventory = game.getPlayer().getInventory();

        if (inventory.getCandyAtSlot(slotNumber) == null) {
            return false;
        }

        return inventory.removeCandy(slotNumber);
    }


    // ==========================================
    // DISCARD MULTIPLE CANDIES
    // ==========================================

    /**
     * Discards multiple candies using their inventory slot numbers.
     *
     * @param slotNumbers the selected inventory slots
     * @return the number of candies successfully discarded
     */
    public int discardMultipleCandies(ArrayList<Integer> slotNumbers) {

        if (game.getPlayer() == null || slotNumbers == null
                || slotNumbers.isEmpty()) {
            return 0;
        }

        Inventory inventory = game.getPlayer().getInventory();

        // Validate all selected slots before removing any candy.
        for (int i = 0; i < slotNumbers.size(); i++) {

            Integer slotNumber = slotNumbers.get(i);

            if (slotNumber == null
                    || inventory.getCandyAtSlot(slotNumber) == null) {
                return 0;
            }

            // Prevent duplicate slot selections.
            if (slotNumbers.indexOf(slotNumber) != i) {
                return 0;
            }
        }

        return inventory.removeMultipleCandies(slotNumbers);
    }

}
