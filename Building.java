
/**
 * Represents the general structure of a building in the Candy Courier game.
 * Stores the building's name, position, and identifying symbol.
 *
 * @author Katelyn and Shaira
 * @version 1.1
 */
public class Building {

    /** The name of the building. */
    private String name;

    /** The row position of the building in the world grid. */
    private int row;

    /** The column position of the building in the world grid. */
    private int column;

    /** The symbol used to identify the type of building. */
    private char symbol;

    /**
     * Constructs a Building with the specified name, position, and symbol.
     *
     * @param name the name of the building
     * @param row the row position of the building
     * @param column the column position of the building
     * @param symbol the identifying symbol of the building
     */
    public Building(String name, int row, int column, char symbol) {
        this.name = name;
        this.row = row;
        this.column = column;
        this.symbol = symbol;
    }

    /** 
    * Returns the name of the building
    *
    * @return the building name
    */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the building's identifying symbol.
     *
     * @return the building symbol
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the row position of the building.
     *
     * @return the row coordinate
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Returns the column position of the building.
     *
     * @return the column coordinate
     */
    public int getColumn() {
        return column;
    }

    /**
     * Updates the building's position in the world grid.
     *
     * @param row the new row position
     * @param column the new column position
     */
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
