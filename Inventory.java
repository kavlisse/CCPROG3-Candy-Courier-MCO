
/**
 * Represents the player's inventory capacity in the Candy Courier game.
 * Manages the maximum number of candies the player can hold
 * as their star rank increases.
 *
 * @author Member 1
 * @version 1.0
 */
public class Inventory {

    /** The maximum number of candies the player can hold. */
    private int capacity;


    /**
     * Constructs an Inventory with the specified initial capacity.
     *
     * @param capacity the starting inventory capacity
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
    }


    /**
     * Returns the current maximum inventory capacity.
     *
     * @return the inventory capacity
     */
    public int getCapacity() {
        return capacity;
    }


    /**
     * Updates the inventory capacity when the player reaches
     * a higher star rank.
     *
     * @param newCapacity the new maximum inventory capacity
     */
    public void setCapacity(int newCapacity) {

        if (newCapacity > capacity) {
            capacity = newCapacity;
        }
    }

}
