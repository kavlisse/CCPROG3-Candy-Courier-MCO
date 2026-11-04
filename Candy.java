
/**
 * Represents a candy produced by a factory
 * in the Candy Courier game.
 *
 * @author Katelyn & Shaira
 * @version 1.0
 */
public class Candy {

    /** The type of candy. */
    private String candyType;

    /**
     * Constructs a Candy with the specified type.
     *
     * @param candyType the type of candy
     */
    public Candy(String candyType) {
        this.candyType = candyType;
    }

    /**
     * Returns the type of candy.
     *
     * @return the candy type
     */
    public String getCandyType() {
        return candyType;
    }
}
