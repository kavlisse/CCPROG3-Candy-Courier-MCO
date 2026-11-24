package model;

import java.util.ArrayList;

/**
 * Controls the Candy Courier game and coordinates
 * the player's activities and rank-based upgrades.
 *
 * @author Katelyn and Shaira
 * @version 1.3
 */
public class Game {

    /** The player participating in the game. */
    private Player player;

    /** The world in which the player travels. */
    private World world;

    /** The current day number. */
    private int dayNum;

    /** Indicates whether the game is currently running. */
    private boolean isRunning;

    /** The highest rank for which houses have already been initialized. */
    private int highestHouseRankAdded = -1;

    /** Tracks the highest rank for which factories have been introduced. */
    private int highestFactoryRankAdded = -1;


    /**
     * Constructs a new game with the initial world size,
     * day number, and running status.
     */
    public Game() {
        this.world = new World(7);
        this.dayNum = 1;
        this.isRunning = true;
    }


    // ==========================================
    // GAME INITIALIZATION
    // ==========================================

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
    // STAR-RANKING SYSTEM
    // ==========================================

    /**
     * Updates the world, factory grids, and inventory capacity
     * according to the player's current star rank.
     */
    public void updateRankUnlocks() {

        if (player != null) {

            int oldSize = world.getGridSize();
            int newSize = player.getWorldGridSize();

            // Expand the world when the player reaches a higher rank.
            if (newSize > oldSize) {

                int offset = (newSize - oldSize) / 2;

                world.expandWorld(newSize);

                // Adjust the player's coordinates after world expansion.
                player.updatePosition(
                        player.getRow() + offset,
                        player.getColumn() + offset);
            }

            // Expand all existing factory production grids.
            world.expandAllFactoryGrids(player.getFactoryGridSize());

            // Update the player's inventory capacity.
            player.getInventory().setCapacity(
                    player.getInventoryCapacity());

            // TASK 5: Initialize houses unlocked at the current rank.
            initializeNewHouses();

            // Introduce factories unlocked at the current rank.
            initializeFactories();
        }
    }


    /**
     * Awards experience points to the player and updates
     * rank-based unlocks when the player's rank changes.
     *
     * @param xp the experience points earned
     */
    public void awardXP(int xp) {

        int previousRank = player.getStarRank();

        player.addXP(xp);

        if (player.getStarRank() > previousRank) {
            updateRankUnlocks();
        }

        checkGameCompletion();
    }


    /**
     * Attempts to deliver a house order using the player's inventory.
     *
     * @param house the house whose order will be delivered
     * @return true if the delivery is successful; false otherwise
     */
    public boolean deliverHouseOrder(House house) {

        if (house == null || player == null || house.isDelivered()) {
            return false;
        }

        Inventory inventory = player.getInventory();

        if (!house.hasEnoughCandy(inventory)) {
            return false;
        }

        boolean successful = house.deliverOrder(inventory);

        if (successful) {
            awardXP(house.getXPReward());
            return true;
        }

        return false;
    }


    /**
     * Checks whether the player has reached the required end-game condition.
     * The game ends only when the player has 5 stars and all 16 house orders
     * have been delivered.
     */
    private void checkGameCompletion() {

        if (player != null && player.hasReachedMaxRank()
                && areAllHouseOrdersDelivered()) {

            isRunning = false;
        }
    }


    /**
     * Checks whether all 16 houses listed in the program overview
     * have been introduced and their orders have been completed.
     *
     * @return true if every house order has been delivered
     */
    private boolean areAllHouseOrdersDelivered() {

        ArrayList<House> houses = world.getHouses();

        if (houses.size() != 16) {
            return false;
        }

        for (House house : houses) {
            if (!house.isDelivered()) {
                return false;
            }
        }

        return true;
    }


    /**
     * Advances the game to the next day and refreshes
     * all factory production grids.
     */
    public void advanceDay() {

        for (Factory factory : world.getFactories()) {
            factory.refreshProductionGrid();
        }

        dayNum++;
    }


    /**
     * Ends the current game session.
     */
    public void endGame() {
        isRunning = false;
    }


    // ==========================================
    // FACTORY INITIALIZATION
    // ==========================================

    /**
     * Introduces factories according to the player's current star rank.
     * Each factory is created only once.
     */
    public void initializeFactories() {

        int rank = player.getStarRank();

        if (rank >= 0 && highestFactoryRankAdded < 0) {

            // Factory 1: Candy Corn and Lollipop
            placeFactory("Factory 1", 0, 6, new Candy[] {
                new Candy("Candy Corn", "N/A"),
                new Candy("Lollipop", "Ube"),
                new Candy("Lollipop", "Mango"),
                new Candy("Lollipop", "Macapuno"),
                new Candy("Lollipop", "Pandan")
            }, 0);

            // Factory 2: Gummy and Marshmallow
            placeFactory("Factory 2", 6, 0, new Candy[] {
                new Candy("Gummy", "Ube"),
                new Candy("Gummy", "Mango"),
                new Candy("Gummy", "Macapuno"),
                new Candy("Gummy", "Pandan"),
                new Candy("Marshmallow", "Ube"),
                new Candy("Marshmallow", "Mango"),
                new Candy("Marshmallow", "Macapuno"),
                new Candy("Marshmallow", "Pandan")
            }, 0);

            highestFactoryRankAdded = 0;
        }

        if (rank >= 1 && highestFactoryRankAdded < 1) {

            // Factory 3: Pastillas, Yema, and Polvoron
            placeFactory("Factory 3", 0, 0, new Candy[] {
                new Candy("Pastillas", "Ube"),
                new Candy("Pastillas", "Mango"),
                new Candy("Pastillas", "Macapuno"),
                new Candy("Pastillas", "Pandan"),
                new Candy("Yema", "Ube"),
                new Candy("Yema", "Mango"),
                new Candy("Yema", "Macapuno"),
                new Candy("Yema", "Pandan"),
                new Candy("Polvoron", "Ube"),
                new Candy("Polvoron", "Mango"),
                new Candy("Polvoron", "Macapuno"),
                new Candy("Polvoron", "Pandan")
            }, 1);

            highestFactoryRankAdded = 1;
        }

        if (rank >= 2 && highestFactoryRankAdded < 2) {

            // Factory 4: Six candy types with Tablea flavor
            placeFactory("Factory 4", 10, 10, new Candy[] {
                new Candy("Gummy", "Tablea"),
                new Candy("Lollipop", "Tablea"),
                new Candy("Marshmallow", "Tablea"),
                new Candy("Pastillas", "Tablea"),
                new Candy("Yema", "Tablea"),
                new Candy("Polvoron", "Tablea")
            }, 2);

            highestFactoryRankAdded = 2;
        }

        if (rank >= 3 && highestFactoryRankAdded < 3) {

            // Factory 5
            placeFactory("Factory 5", 0, 10, new Candy[] {
                new Candy("Gummy", "Jackfruit"),
                new Candy("Gummy", "Durian"),
                new Candy("Lollipop", "Jackfruit"),
                new Candy("Lollipop", "Durian"),
                new Candy("Marshmallow", "Jackfruit"),
                new Candy("Marshmallow", "Durian")
            }, 3);

            // Factory 6: All Lollipop flavors
            placeFactory("Factory 6", 12, 6, new Candy[] {
                new Candy("Lollipop", "Ube"),
                new Candy("Lollipop", "Mango"),
                new Candy("Lollipop", "Macapuno"),
                new Candy("Lollipop", "Pandan"),
                new Candy("Lollipop", "Tablea"),
                new Candy("Lollipop", "Jackfruit"),
                new Candy("Lollipop", "Durian")
            }, 3);

            // Factory 7: All Gummy flavors
            placeFactory("Factory 7", 5, 12, new Candy[] {
                new Candy("Gummy", "Ube"),
                new Candy("Gummy", "Mango"),
                new Candy("Gummy", "Macapuno"),
                new Candy("Gummy", "Pandan"),
                new Candy("Gummy", "Tablea"),
                new Candy("Gummy", "Jackfruit"),
                new Candy("Gummy", "Durian")
            }, 3);

            // Factory 8: All Marshmallow flavors
            placeFactory("Factory 8", 6, 0, new Candy[] {
                new Candy("Marshmallow", "Ube"),
                new Candy("Marshmallow", "Mango"),
                new Candy("Marshmallow", "Macapuno"),
                new Candy("Marshmallow", "Pandan"),
                new Candy("Marshmallow", "Tablea"),
                new Candy("Marshmallow", "Jackfruit"),
                new Candy("Marshmallow", "Durian")
            }, 3);

            highestFactoryRankAdded = 3;
        }

        if (rank >= 4 && highestFactoryRankAdded < 4) {

            // Factory 9
            placeFactory("Factory 9", 0, 0, new Candy[] {
                new Candy("Candy Corn", "N/A"),
                new Candy("Candy Cane", "N/A"),
                new Candy("Pastillas", "Ube"),
                new Candy("Pastillas", "Mango"),
                new Candy("Yema", "Ube"),
                new Candy("Yema", "Mango"),
                new Candy("Polvoron", "Ube"),
                new Candy("Polvoron", "Mango")
            }, 4);

            // Factory 10 / Factory X
            placeFactory("Factory 10", 14, 2, new Candy[] {
                new Candy("Pastillas", "Macapuno"),
                new Candy("Pastillas", "Pandan"),
                new Candy("Pastillas", "Jackfruit"),
                new Candy("Pastillas", "Durian"),
                new Candy("Yema", "Macapuno"),
                new Candy("Yema", "Pandan"),
                new Candy("Yema", "Jackfruit"),
                new Candy("Yema", "Durian"),
                new Candy("Polvoron", "Macapuno"),
                new Candy("Polvoron", "Pandan"),
                new Candy("Polvoron", "Jackfruit"),
                new Candy("Polvoron", "Durian")
            }, 4);

            highestFactoryRankAdded = 4;
        }
    }


    /**
     * Creates a factory and places it at the specified world coordinates.
     *
     * @param name the factory name
     * @param row the factory's row position
     * @param column the factory's column position
     * @param allowedCandies the candies that the factory can produce
     * @param introducedRank the rank at which the factory first becomes available
     */
    private void placeFactory(String name, int row, int column,
                              Candy[] allowedCandies, int introducedRank) {

        // Adjust the introduction coordinates if several ranks were unlocked
        // before the newly available factories were initialized.
        int introductionGridSize = 7 + (introducedRank * 2);
        int offset = (world.getGridSize() - introductionGridSize) / 2;

        int actualRow = row + offset;
        int actualColumn = column + offset;

        // Create the factory using the production-grid size available
        // when it is introduced. If the player skipped one or more ranks,
        // expand it afterward so newly unlocked slots remain empty until
        // the next day refresh event.
        int factoryIntroductionGridSize = 6 + introducedRank;

        Factory factory = new Factory(
                name,
                actualRow,
                actualColumn,
                allowedCandies,
                factoryIntroductionGridSize
        );

        factory.expandProductionGrid(player.getFactoryGridSize());
        world.placeBuilding(factory, actualRow, actualColumn);
    }


    // ==========================================
    // HOUSE INITIALIZATION
    // ==========================================

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
                // House E
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
                        new Candy("Candy Cane")
                    },
                    new int[] {100},
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

        world.addHouse(house);
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

}