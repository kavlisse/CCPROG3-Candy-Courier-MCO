
/**
 * Represents the player's inventory capacity in the Candy Courier game.
 * Manages the maximum number of candies the player can hold
 * as their star rank increases.
 *
 * @author Katelyn and Shaira
 * @version 1.2
 */
import java.util.ArrayList; 

public class Inventory {

    /** The maximum number of candies the player can hold. */
    private int capacity;
    /** The list of candies currently stored in the player's inventory. */
    private ArrayList<Candy> candies;

    /**
     * Constructs an Inventory with the specified initial capacity .
     *
     * @param capacity the starting inventory capacity
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
        this.candies = new ArrayList<>();
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

    /** Checks wether the inventory has reached the maximum capacity. 
    *
    * @return true if he inventory is full, otherwise false
    */
    public boolean isFull() {
        return candies.size() >= capacity;
    }
        
    /** 
    * Adds a candy to the player's inventory if there is available space.
    *
    * @param candy the candy to be added
    * @return true if the candy was successfully added, false if the inventory is full
    */
    public boolean addCandy (Candy candy) {
        if (isFull()) {
            return false;
        }

        candies.add(candy);
            return true;
    }

    /**
    * Returns the current number of candies stored in the inventory.
    *
    * @return the number of candies in the inventory
    */
    public int getCandyCount() {
        return candies.size();
    }

    /**
    * Displays all candies currently stored in the inventory and its current capacity
    */
    public void displayInventory() {
        System.out.println("\n===== PLAYER INVENTORY =====");
        System.out.println("Capacity: " + getCandyCount() + "/" + capacity);

        if(candies.isEmpty()) {
            System.out.println("Your inventory is empty!");
        }
        else {
            for (int i = 0; i < candies.size(); i++) {
                System.out.println((i + 1) + ". " + candies.get(i).getCandyType());
             }
        }
        System.out.println("==============================");
    }

    /** 
    * Removes a candy from the player's inventory using its displayed slot number
    * 
    * @param slotNumber the position of the candy to be removed
    * @return true if the candy was successfully removed, false if the slot number is invalid
    */
    public boolean removeCandy(int slotNumber) {
        if(slotNumber < 1 || slotNumber > candies.size()) {
            return false;
        }

        candies.remove(slotNumber - 1);
            return true;
    }

    /**
    * Removes multiple candies from the inventory using the displayed slot numbers.
    *
    * @parma slotNumbers the list of selected candy slot number
    * @return the number of candies successfully removed
    */
    public int removeMultpleCandies(ArrayList<Integer> slotNumbers) {
        int removedCount = 0;
        if(slotNumbers == null || slotNumbers.isEmpty()) {
            return 0;
        }
        for(int i = candies.size(); i >= 1; i--) {
            if(slotNumbers.contains(i)) {
                candies.remove(i -1);
                removedCount++;
            }
        }

        return removedCount;
    }

    
    

}
