
/**
 * Represents a candy produced by a factory
 * in the Candy Courier game.
 *
 * @author Katelyn & Shaira
 * @version 1.1
 */
public class Candy {

    /** The type of candy. */
    private String candyType;
    /** The flacor of the candy*/
    private String candyFlavor;

    /** 
    * Constructs a Candy with the type and default flavor.
    *
    * @param candyType the type of candy
    */
    private Candy(String candyType) {
        this(candyType, "N/A");
    }
    
    /**
     * Constructs a Candy with the specified type and flavor.
     *
     * @param candyType the type of candy 
     * @param candyFlavor the flavor of candy
     */
    public Candy(String candyType, String candyFlavor) {
        this.candyType = candyType;
        this.candyFlavor = candyFlavor;
    }

    /**
     * Returns the type of candy.
     *
     * @return the candy type
     */
    public String getCandyType() {
        return candyType;
    }

    /**
    * Returns the flavor of candy
    *
    * @return the candy flavor
    */
    public String getCandyFlavor() {
        return candyFlavor;
    }
    
}
