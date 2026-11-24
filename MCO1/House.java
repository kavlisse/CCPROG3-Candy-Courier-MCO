/**
 * Represents a house in the Candy Courier game.
 * Stores the house's candy orders, required quantities,
 * XP reward, and delivery status.
 *
 * @author Katelyn and Shaira
 * @version 1.1
 */
public class House extends Building {

    // ==========================================
    // HOUSE DELIVERY SYSTEM
    // ==========================================

    /** The candies required by the house. */
    private Candy[] requiredCandies;

    /** The required quantity of each candy. */
    private int[] requiredQuantities;

    /** The XP reward for completing the house's order. */
    private int xpReward;

    /** Indicates whether the house's order has been completed. */
    private boolean delivered;

    /**
     * Constructs a House with the specified name, location,
     * candy orders, required quantities, and XP reward.
     *
     * @param name the name of the house
     * @param row the row coordinate of the house
     * @param column the column coordinate of the house
     * @param requiredCandies the candies required by the house
     * @param requiredQuantities the required quantity of each candy
     * @param xpReward the XP earned upon completing the order
     */
    public House(String name, int row, int column,
                 Candy[] requiredCandies, int[] requiredQuantities,
                 int xpReward) {

        super(name, row, column, 'D');

        this.requiredCandies = requiredCandies;
        this.requiredQuantities = requiredQuantities;
        this.xpReward = xpReward;
        this.delivered = false;
    }

    /**
     * Returns the candies required by the house.
     *
     * @return the required candies
     */
    public Candy[] getRequiredCandies() {
        return requiredCandies;
    }

    /**
     * Returns the required quantity of each candy.
     *
     * @return the required candy quantities
     */
    public int[] getRequiredQuantities() {
        return requiredQuantities;
    }

    /**
     * Returns the XP reward for completing the house's order.
     *
     * @return the XP reward
     */
    public int getXPReward() {
        return xpReward;
    }

    /**
     * Returns whether the house's order has been completed.
     *
     * @return true if the order has been delivered; false otherwise
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Updates the delivery status of the house's order.
     *
     * @param delivered the new delivery status
     */
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }


    // ==========================================
    // CHECKING HOUSE ORDERS
    // ==========================================

    /**
     * Checks whether the player's inventory contains all
     * candies required by this house.
     *
     * @param inventory the player's inventory
     * @return true if the entire order can be fulfilled;
     *         false otherwise
     */
    public boolean hasEnoughCandy(Inventory inventory) {

        if (delivered || inventory == null) {
            return false;
        }

        return inventory.hasEnoughCandy(
                requiredCandies, requiredQuantities);
    }


    // ==========================================
    // COMPLETING HOUSE DELIVERIES
    // ==========================================

    /**
     * Removes the required candies from the player's inventory
     * and marks the house's order as delivered.
     *
     * @param inventory the player's inventory
     * @return true if the delivery was successful; false otherwise
     */
    public boolean deliverOrder(Inventory inventory) {

        if (delivered || inventory == null) {
            return false;
        }

        // Remove the required candies only if the entire order is available.
        boolean removed = inventory.removeCandies(
                requiredCandies, requiredQuantities);

        if (removed) {
            delivered = true;
            return true;
        }

        return false;
    }
}