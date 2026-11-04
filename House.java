/**
 * Represents a house in the Candy Courier game.
 * Stores the house's candy orders, required quantities,
 * XP reward, and delivery status.
 *
 * @author Katelyn & Shaira
 * @version 1.0
 */

public class House extends Building {

}
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

    super(name, row, column, 'H');

    if (requiredCandies == null || requiredQuantities == null ||
        requiredCandies.length != requiredQuantities.length) {
        throw new IllegalArgumentException("Invalid house order.");
    }

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
 * Checks whether the house's order has been completed.
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


