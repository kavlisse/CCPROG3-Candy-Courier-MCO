
import java.util.Scanner;

/**
 * Controls the Candy Courier game and coordinates
 * the player's activities and rank-based upgrades.
 *
 * @author Katelyn & Shaira
 * @version 1.0
 */
public class Game {

    // ==========================================
    // GENERAL GAME ATTRIBUTES
    // ==========================================

    /** The player participating in the game. */
    private Player player;

    /** The world in which the player travels. */
    private World world;

    /** The current day number. */
    private int dayNum;

    /** Indicates whether the game is currently running. */
    private boolean isRunning;

    /** Scanner used to receive player input. */
    private Scanner input;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    /**
     * Constructs a new game with the initial world size,
     * day number, and running status.
     */
    public Game() {
        this.world = new World(7);
        this.dayNum = 1;
        this.isRunning = true;
        this.input = new Scanner(System.in);
    }


    // ==========================================
    // GAME INITIALIZATION
    // ==========================================

    /**
     * Starts the game by asking for the player's name
     * and displaying the initial world and main menu.
     */
    public void startGame() {

        System.out.println("================================");
        System.out.println("       CANDY COURIER");
        System.out.println("================================");

        System.out.print("Enter your player name: ");
        String name = input.nextLine();

        initializePlayer(name);

        while (isRunning) {

            System.out.println("\n================================");
            System.out.println("DAY " + dayNum);
            System.out.println("================================");

            world.displayWorld(player);
            displayMainMenu();

            System.out.print("Enter your choice: ");

            String choiceInput = input.nextLine();

            try {
                int choice = Integer.parseInt(choiceInput);
                handleMenuChoice(choice);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Initializes the player and places the player's home
     * at the center of the initial world grid.
     *
     * @param name the name of the player
     */
    public void initializePlayer(String name) {

        this.player = new Player(name);

        int center = world.getGridSize() / 2;

        player.updatePosition(center, center);

        PlayerHome home = new PlayerHome(center, center);

        world.placeBuilding(home, center, center);

        updateRankUnlocks();
    }


    // ==========================================
    // TASK 1: STAR-RANKING SYSTEM
    // ==========================================

    /**
     * Updates the world, factory grids, and inventory capacity
     * according to the player's current star rank.
     */
    public void updateRankUnlocks() {

        if (player != null) {

            int oldSize = world.getGridSize();
            int newSize = player.getWorldGridSize();

            int offset = (newSize - oldSize) / 2;

            world.expandWorld(newSize);

            // Adjust the player's coordinates after world expansion.
            if (newSize > oldSize) {
                player.updatePosition(
                        player.getRow() + offset,
                        player.getColumn() + offset);
            }

            world.expandAllFactoryGrids(
                    player.getFactoryGridSize());

            player.getInventory().setCapacity(
                    player.getInventoryCapacity());
        }
    }


    // ==========================================
    // TASK 2: TRAVEL AND MOVEMENT
    // ==========================================

    /**
     * Displays the main menu.
     *
     * Travel is currently implemented.
     * Other game options will be added in their respective tasks.
     */
    public void displayMainMenu() {

        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Travel");
        System.out.println("2. Search Candies");
        System.out.println("0. Quit Game");
        System.out.println("===============================");
    }

    /**
     * Processes the player's main menu selection.
     *
     * @param choice the selected menu option
     */
    public void handleMenuChoice(int choice) {

        if (choice == 1) {
            handleTravel();
        }
        else if (choice == 2) {
            searchCandies();
        }
        else if (choice == 0) {
            quitGame();
        }
        else {
            System.out.println("Invalid menu option. Please try again.");
        }
    }

    /**
     * Displays the available travel directions and
     * allows the player to move around the world.
     *
     * Diagonal movement is only available at 3 stars or higher.
     */
    public void handleTravel() {

        boolean traveling = true;

        while (traveling) {

            System.out.println("\n========== TRAVEL ==========");
            System.out.println("Current position: (" +
                    player.getRow() + ", " + player.getColumn() + ")");

            System.out.println("1. Up");
            System.out.println("2. Down");
            System.out.println("3. Left");
            System.out.println("4. Right");

            if (player.canMoveDiagonally()) {

                System.out.println("\nDiagonal Movement Unlocked!");
                System.out.println("5. Up-Left");
                System.out.println("6. Up-Right");
                System.out.println("7. Down-Left");
                System.out.println("8. Down-Right");
            }

            System.out.println("0. Return to Main Menu");
            System.out.println("============================");

            System.out.print("Choose a direction: ");

            String choiceInput = input.nextLine();

            try {

                int choice = Integer.parseInt(choiceInput);

                String direction = "";

                switch (choice) {

                    case 1:
                        direction = "up";
                        break;

                    case 2:
                        direction = "down";
                        break;

                    case 3:
                        direction = "left";
                        break;

                    case 4:
                        direction = "right";
                        break;

                    case 5:
                        direction = "up-left";
                        break;

                    case 6:
                        direction = "up-right";
                        break;

                    case 7:
                        direction = "down-left";
                        break;

                    case 8:
                        direction = "down-right";
                        break;

                    case 0:
                        traveling = false;
                        System.out.println("Returning to the main menu.");
                        continue;

                    default:
                        System.out.println("Invalid travel option.");
                        continue;
                }

                // Attempt the movement using the Player class.
                player.move(direction, world.getGridSize());

                // Display the updated world after every attempt.
                world.displayWorld(player);

            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Allows the player to search for candies and displays
     * the factories that produce the specified candy.
     */
    public void searchCandies() {

        System.out.println("\n========== SEARCH CANDIES ==========");

        System.out.print("Enter candy type: ");
        String candyType = input.nextLine().trim();
    
        System.out.print("Enter candy flavor (leave blank for any flavor): ");
        String candyFlavor = input.nextLine().trim();
    
        boolean found = false;

        for (Factory factory : world.getFactories()) {
    
            boolean matches;
    
            if (candyFlavor.isEmpty()) {
                matches = factory.producesCandy(candyType);
            }
            else {
                matches = factory.producesCandy(candyType, candyFlavor);
            }
    
            if (matches) {
                System.out.println("Found in: " + factory.getName());
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No factories produce the specified candy.");
        }
    
        System.out.println("====================================");
    }

    // ==========================================
    // GENERAL GAME GETTERS
    // ==========================================

    /**
     * Returns the current player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the current world.
     *
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the current day number.
     *
     * @return the current day
     */
    public int getDayNum() {
        return dayNum;
    }

    /**
     * Returns whether the game is currently running.
     *
     * @return true if the game is running; false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }


    // ==========================================
    // GAME EXIT
    // ==========================================

    /**
     * Ends the current game session.
     */
    public void quitGame() {

        isRunning = false;

        System.out.println("\nThank you for playing Candy Courier!");
        System.out.println("Game session ended.");
    }


    // ==========================================
    // MAIN METHOD
    // ==========================================

    /**
     * Main entry point of the Candy Courier game.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Game game = new Game();
        game.startGame();
    }

}
