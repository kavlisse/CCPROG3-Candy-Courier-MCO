import java.util.ArrayList;

/**
 * Represents the player's inventory in the Candy Courier game.
 * Manages the candies currently held by the player and the
 * maximum number of candies the player can carry.
 *
 * @author Katelyn and Shaira
 * @version 1.2
 */
public class Inventory {

    /** The maximum number of candies the player can hold. */
    private int capacity;

    /** The list of candies currently stored in the inventory. */
    private ArrayList<Candy> candies;

    /**
     * Constructs an Inventory with the specified initial capacity.
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
    }

    /**
     * Checks whether the inventory has reached its maximum capacity.
     *
     * @return true if the inventory is full; false otherwise
     */
    public boolean isFull() {
        return candies.size() >= capacity;
    }

    /**
     * Adds a candy to the player's inventory if space is available.
     *
     * @param candy the candy to be added
     * @return true if the candy was added; false if the inventory is full
     */
    public boolean addCandy(Candy candy) {

        if (candy == null || isFull()) {
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
     * Displays all candies currently stored in the inventory,
     * including their flavors when applicable.
     */
    public void displayInventory() {

        System.out.println("\n===== PLAYER INVENTORY =====");
        System.out.println("Capacity: " + getCandyCount() + "/" + capacity);

        if (candies.isEmpty()) {
            System.out.println("Your inventory is empty!");
        }
        else {
            for (int i = 0; i < candies.size(); i++) {

                Candy candy = candies.get(i);

                String candyName = candy.getCandyType();
                String flavor = candy.getCandyFlavor();

                // Display the flavor only when the candy has one.
                if (flavor != null
                        && !flavor.equalsIgnoreCase("N/A")) {
                    candyName += " (" + flavor + ")";
                }

                System.out.println((i + 1) + ". " + candyName);
            }
        }

        System.out.println("==============================");
    }

    /**
     * Returns the candy stored in the specified displayed slot.
     *
     * @param slotNumber the displayed position of the candy
     * @return the candy at that position, or null if the slot is invalid
     */
    public Candy getCandyAtSlot(int slotNumber) {

        if (slotNumber < 1 || slotNumber > candies.size()) {
            return null;
        }

        return candies.get(slotNumber - 1);
    }

    /**
     * Removes a candy from the inventory using its displayed slot number.
     *
     * @param slotNumber the position of the candy to be removed
     * @return true if the candy was removed; false if the slot is invalid
     */
    public boolean removeCandy(int slotNumber) {

        if (slotNumber < 1 || slotNumber > candies.size()) {
            return false;
        }

        candies.remove(slotNumber - 1);
        return true;
    }

    /**
     * Removes multiple candies using their displayed slot numbers.
     *
     * @param slotNumbers the list of selected candy slot numbers
     * @return the number of candies successfully removed
     */
    public int removeMultipleCandies(ArrayList<Integer> slotNumbers) {

        if (slotNumbers == null || slotNumbers.isEmpty()) {
            return 0;
        }

        // Validate every requested slot before changing the inventory.
        // This prevents a partly completed discard if one slot is invalid.
        for (int i = 0; i < slotNumbers.size(); i++) {

            Integer slotNumber = slotNumbers.get(i);

            if (slotNumber == null || slotNumber < 1
                    || slotNumber > candies.size()
                    || slotNumbers.indexOf(slotNumber) != i) {
                return 0;
            }
        }

        int removedCount = 0;

        // Remove from the last slot to avoid shifting remaining indices.
        for (int i = candies.size(); i >= 1; i--) {

            if (slotNumbers.contains(i)) {
                candies.remove(i - 1);
                removedCount++;
            }
        }

        return removedCount;
    }


    // ==========================================
    // HOUSE DELIVERY SYSTEM
    // ==========================================

    /**
     * Checks whether the inventory contains all candies required
     * by a house, including the required quantities and flavors.
     *
     * @param requiredCandies the candies required by the house
     * @param requiredQuantities the required quantity of each candy
     * @return true if the inventory can fulfill the entire order;
     *         false otherwise
     */
    public boolean hasEnoughCandy(
            Candy[] requiredCandies, int[] requiredQuantities) {

        return findMatchingCandyIndices(
                requiredCandies, requiredQuantities) != null;
    }

    /**
     * Removes the candies required by a house after verifying
     * that the entire order can be fulfilled.
     *
     * @param requiredCandies the candies required by the house
     * @param requiredQuantities the required quantity of each candy
     * @return true if all required candies were removed;
     *         false if the inventory does not contain enough
     */
    public boolean removeCandies(
            Candy[] requiredCandies, int[] requiredQuantities) {

        ArrayList<Integer> matchingIndices =
                findMatchingCandyIndices(requiredCandies, requiredQuantities);

        if (matchingIndices == null) {
            return false;
        }

        // Remove only the candies selected for this order.
        // Removing from the end prevents index shifting.
        for (int i = candies.size() - 1; i >= 0; i--) {

            if (matchingIndices.contains(i)) {
                candies.remove(i);
            }
        }

        return true;
    }

    /**
     * Finds unique inventory positions that can fulfill a house order.
     * Specific-flavor requirements are matched before any-flavor
     * requirements to prevent the same candy from being counted twice.
     *
     * @param requiredCandies the candies required by the house
     * @param requiredQuantities the required quantity of each candy
     * @return the matching inventory positions, or null if the
     *         entire order cannot be fulfilled
     */
    private ArrayList<Integer> findMatchingCandyIndices(
            Candy[] requiredCandies, int[] requiredQuantities) {

        if (requiredCandies == null || requiredQuantities == null
                || requiredCandies.length != requiredQuantities.length) {
            return null;
        }

        ArrayList<Integer> matchingIndices = new ArrayList<>();
        boolean[] used = new boolean[candies.size()];

        // First pass: fulfill requirements with specific flavors.
        // Second pass: fulfill requirements that accept any flavor.
        for (int pass = 0; pass < 2; pass++) {

            for (int i = 0; i < requiredCandies.length; i++) {

                Candy requiredCandy = requiredCandies[i];

                if (requiredCandy == null || requiredQuantities[i] <= 0) {
                    return null;
                }

                String requiredFlavor = requiredCandy.getCandyFlavor();
                boolean acceptsAnyFlavor =
                        "Any".equalsIgnoreCase(requiredFlavor);

                if ((pass == 0 && acceptsAnyFlavor)
                        || (pass == 1 && !acceptsAnyFlavor)) {
                    continue;
                }

                int matchedCount = 0;

                for (int j = 0; j < candies.size(); j++) {

                    if (!used[j]
                            && matchesRequiredCandy(
                                    candies.get(j), requiredCandy)) {

                        used[j] = true;
                        matchingIndices.add(j);
                        matchedCount++;

                        if (matchedCount == requiredQuantities[i]) {
                            break;
                        }
                    }
                }

                if (matchedCount < requiredQuantities[i]) {
                    return null;
                }
            }
        }

        return matchingIndices;
    }

    /**
     * Checks whether an inventory candy matches a required candy.
     * An "Any" flavor requirement accepts every flavor of that type.
     *
     * @param inventoryCandy the candy currently in the inventory
     * @param requiredCandy the candy specified by the house order
     * @return true if the candies match; false otherwise
     */
    private boolean matchesRequiredCandy(
            Candy inventoryCandy, Candy requiredCandy) {

        if (inventoryCandy == null || requiredCandy == null) {
            return false;
        }

        if (!inventoryCandy.getCandyType().equalsIgnoreCase(
                requiredCandy.getCandyType())) {
            return false;
        }

        String requiredFlavor = requiredCandy.getCandyFlavor();

        if ("Any".equalsIgnoreCase(requiredFlavor)) {
            return true;
        }

        String inventoryFlavor = inventoryCandy.getCandyFlavor();

        if (inventoryFlavor == null || requiredFlavor == null) {
            return inventoryFlavor == requiredFlavor;
        }

        return inventoryFlavor.equalsIgnoreCase(requiredFlavor);
    }
}