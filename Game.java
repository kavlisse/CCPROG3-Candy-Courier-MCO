import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controls the Candy Courier game and coordinates
 * the player's activities and rank-based upgrades.
 *
 * @author Katelyn & Shaira
 * @version 1.2
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

    /** The highest rank for which houses have already been initialized. */
    private int highestHouseRankAdded = -1;

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
        System.out.println("3. View Orders");
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
        else if (choice == 3) {
            viewOrders();
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
    /**
     * Displays all accessible house orders, including their
     * coordinates, required candies, quantities, and XP rewards.
     */
    public void viewOrders() {
    
        System.out.println("\n========== VIEW ORDERS ==========");
    
        ArrayList<House> houses = world.getHouses();
    
        if (houses.isEmpty()) {
            System.out.println("There are currently no available house orders.");
        }
        else {
    
            for (House house : houses) {
    
                System.out.println("\n" + house.getName());
    
                System.out.println("Coordinates: (" +
                        house.getRow() + ", " + house.getColumn() + ")");
    
                System.out.println("Required Candies:");
    
                Candy[] requiredCandies = house.getRequiredCandies();
                int[] requiredQuantities = house.getRequiredQuantities();
    
                for (int i = 0; i < requiredCandies.length; i++) {
    
                    Candy candy = requiredCandies[i];
    
                    String candyName = candy.getCandyType();
                    String flavor = candy.getCandyFlavor();
    
                    if ("Any".equalsIgnoreCase(flavor)) {
                        candyName += " (any flavor)";
                    }
                    else if (flavor != null &&
                            !flavor.equalsIgnoreCase("N/A")) {
                        candyName += " (" + flavor + ")";
                    }
    
                    System.out.println("- " + requiredQuantities[i] +
                            "x " + candyName);
                }
    
                System.out.println("XP Reward: " + house.getXPReward() + " XP");
                System.out.println("Delivery Status: " +
                        (house.isDelivered() ? "Completed" : "Pending"));
            }
        }
    
        System.out.println("=================================");
    }

    /**
     * Initializes houses according to the player's current star rank.
     * Prevents houses from being placed more than once.
     */
    private void initializeNewHouses() {
    
        int currentRank = Math.min(player.getStarRank(), 4);
    
        for (int rank = highestHouseRankAdded + 1;
             rank <= currentRank; rank++) {
    
            placeHousesForRank(rank);
    
            highestHouseRankAdded = rank;
        }
    }

    /**
     * Places the houses introduced at the specified rank.
     *
     * @param rank the rank at which the houses are introduced
     */
    private void placeHousesForRank(int rank) {
    
        int introductionGridSize = 7 + (rank * 2);
    
        int offset = (world.getGridSize() - introductionGridSize) / 2;
    
        switch (rank) {
    
            case 0:
                // House A
                placeHouse("House A", 1, 1,
                    new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Gummy", "Any"),
                        new Candy("Lollipop", "Any"),
                        new Candy("Marshmallow", "Any")
                    },
                    new int[] {4, 2, 2, 2},
                    100, offset
                );
            
                // House B
                placeHouse("House B", 5, 2,
                    new Candy[] {
                        new Candy("Candy Corn")
                    },
                    new int[] {15},
                    150, offset
                );
            
                // House C
                placeHouse("House C", 3, 6,
                    new Candy[] {
                        new Candy("Gummy", "Any"),
                        new Candy("Marshmallow", "Any"),
                        new Candy("Pastillas", "Any"),
                        new Candy("Yema", "Any"),
                        new Candy("Polvoron", "Any")
                    },
                    new int[] {5, 5, 5, 5, 5},
                    250, offset
                );
                break;
    
            case 1:
                // House D
                 placeHouse("House D", 8, 6,
                        new Candy[] {
                        new Candy("Candy Corn"),
                        new Candy("Candy Cane"),
                        new Candy("Pastillas", "Any"),
                        new Candy("Yema", "Any"),
                        new Candy("Polvoron", "Any")
                    },
                    new int[] {5, 5, 10, 10, 10},
                    400, offset
                );
                break;
    
            case 2:
                // House E, F, G, and H
                placeHouse("House E", 0, 6,
                    new Candy[] {
                    new Candy("Candy Corn"),
                    new Candy("Yema", "Any"),
                    new Candy("Pastillas", "Any"),
                    new Candy("Lollipop", "Ube"),
                    new Candy("Lollipop", "Macapuno")
                },
                new int[] {10, 5, 5, 5, 5},
                300, offset
            );
        
            // House F
            placeHouse("House F", 10, 1,
                    new Candy[] {
                    new Candy("Gummy", "Tablea"),
                    new Candy("Marshmallow", "Tablea"),
                    new Candy("Pastillas", "Tablea"),
                    new Candy("Polvoron", "Tablea")
                },
                new int[] {7, 7, 8, 8},
                300, offset
            );
        
            // House G
            placeHouse("House G", 4, 0,
                    new Candy[] {
                    new Candy("Pastillas", "Any"),
                    new Candy("Pastillas", "Durian"),
                    new Candy("Pastillas", "Jackfruit")
                },
                new int[] {20, 10, 10},
                400, offset
            );
        
            // House H
            placeHouse("House H", 6, 10,
                    new Candy[] {
                    new Candy("Polvoron", "Any"),
                    new Candy("Yema", "Any"),
                    new Candy("Pastillas", "Any"),
                    new Candy("Gummy", "Mango"),
                    new Candy("Marshmallow", "Pandan")
                },
                new int[] {10, 5, 5, 5, 5},
                300, offset
            );
                break;
    
            case 3:
                // House I, J, K, L, and M
                 // House I
            placeHouse("House I", 0, 3,
                    new Candy[] {
                    new Candy("Lollipop", "Jackfruit"),
                    new Candy("Lollipop", "Durian"),
                    new Candy("Lollipop", "Pandan"),
                    new Candy("Lollipop", "Ube"),
                    new Candy("Lollipop", "Macapuno")
                },
                new int[] {9, 9, 9, 9, 9},
                450, offset
            );
        
            // House J
            placeHouse("House J", 3, 0,
                    new Candy[] {
                    new Candy("Candy Corn"),
                    new Candy("Marshmallow", "Mango"),
                    new Candy("Yema", "Mango"),
                    new Candy("Pastillas", "Mango"),
                    new Candy("Gummy", "Mango")
                },
                new int[] {5, 6, 7, 8, 9},
                350, offset
            );
        
            // House K
            placeHouse("House K", 9, 0,
                    new Candy[] {
                    new Candy("Gummy", "Ube"),
                    new Candy("Gummy", "Mango"),
                    new Candy("Gummy", "Macapuno"),
                    new Candy("Gummy", "Pandan")
                },
                new int[] {10, 10, 10, 10},
                400, offset
            );
        
            // House L
            placeHouse("House L", 12, 5,
                    new Candy[] {
                    new Candy("Pastillas", "Ube"),
                    new Candy("Yema", "Ube"),
                    new Candy("Polvoron", "Ube")
                },
                new int[] {10, 10, 10},
                300, offset
            );
        
            // House M
            placeHouse("House M", 12, 12,
                    new Candy[] {
                    new Candy("Pastillas", "Pandan"),
                    new Candy("Yema", "Pandan"),
                    new Candy("Polvoron", "Pandan")
                },
                new int[] {10, 10, 10},
                300, offset
            );
                break;
    
            case 4:
                // House N, O, and P
                // House N
            placeHouse("House N", 3, 14,
                    new Candy[] {
                    new Candy("Candy Cane"),
                    new Candy("Polvoron", "Jackfruit"),
                    new Candy("Polvoron", "Durian"),
                    new Candy("Lollipop", "Tablea")
                },
                new int[] {16, 3, 3, 3},
                250, offset
            );
        
            // House O
            placeHouse("House O", 14, 0,
                    new Candy[] {
                    new Candy("Candy Cane"),
                    new Candy("Candy Corn")
                },
                new int[] {100, 10},
                1000, offset
            );
        
            // House P
            placeHouse("House P", 14, 10,
                    new Candy[] {
                    new Candy("Candy Corn"),
                    new Candy("Candy Cane"),
                    new Candy("Pastillas", "Macapuno"),
                    new Candy("Yema", "Macapuno"),
                    new Candy("Polvoron", "Macapuno")
                },
                new int[] {5, 5, 5, 5, 5},
                250, offset
            );
                break;
        }
    }
    
    /**
     * Creates and places a house in the world.
     *
     * @param name the name of the house
     * @param row the introduction row coordinate
     * @param column the introduction column coordinate
     * @param candies the candies required by the house
     * @param quantities the required quantity of each candy
     * @param xp the XP reward for completing the order
     * @param offset the coordinate adjustment caused by world expansion
     */
    private void placeHouse(String name, int row, int column,
                            Candy[] candies, int[] quantities,
                            int xp, int offset) {
    
        int actualRow = row + offset;
        int actualColumn = column + offset;
    
        House house = new House(
            name,
            actualRow,
            actualColumn,
            candies,
            quantities,
            xp
        );
    
        world.placeBuilding(house, actualRow, actualColumn);
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
