import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Regression test for the exact Factory (Appendix C) and House (Appendix D)
 * data in the Candy Courier MCO specifications.
 *
 * Run this file alongside the project's Java source files:
 *   javac --release 21 *.java
 *   java CandyCourierSpecificationTest
 *
 * The test creates a fresh game, advances it directly to 5 stars, and compares
 * every factory and house name, final coordinate, allowed candy type/flavor,
 * order quantity, and XP reward against the specification.
 *
 * @author Test utility
 */
public class GameIntegrationTest {

    private static int passed;
    private static int failed;

    private static class CandySpec {
        String type;
        String flavor;
        CandySpec(String type, String flavor) {
            this.type = type;
            this.flavor = flavor;
        }
        String key() { return type + "|" + flavor; }
    }

    private static class FactorySpec {
        String name;
        int row;
        int column;
        CandySpec[] candies;
        FactorySpec(String name, int row, int column, CandySpec... candies) {
            this.name = name; this.row = row; this.column = column;
            this.candies = candies;
        }
    }

    private static class HouseSpec {
        String name;
        int row;
        int column;
        int xp;
        CandySpec[] candies;
        int[] quantities;
        HouseSpec(String name, int row, int column, int xp,
                  CandySpec[] candies, int[] quantities) {
            this.name = name; this.row = row; this.column = column;
            this.xp = xp; this.candies = candies; this.quantities = quantities;
        }
    }

    private static CandySpec c(String type) { return new CandySpec(type, "N/A"); }
    private static CandySpec c(String type, String flavor) { return new CandySpec(type, flavor); }

    private static final FactorySpec[] FACTORIES = {
        new FactorySpec("Factory 1", 4, 10,
            c("Candy Corn"), c("Lollipop", "Ube"), c("Lollipop", "Mango"),
            c("Lollipop", "Macapuno"), c("Lollipop", "Pandan")),
        new FactorySpec("Factory 2", 10, 4,
            c("Gummy", "Ube"), c("Gummy", "Mango"), c("Gummy", "Macapuno"), c("Gummy", "Pandan"),
            c("Marshmallow", "Ube"), c("Marshmallow", "Mango"), c("Marshmallow", "Macapuno"), c("Marshmallow", "Pandan")),
        new FactorySpec("Factory 3", 3, 3,
            c("Pastillas", "Ube"), c("Pastillas", "Mango"), c("Pastillas", "Macapuno"), c("Pastillas", "Pandan"),
            c("Yema", "Ube"), c("Yema", "Mango"), c("Yema", "Macapuno"), c("Yema", "Pandan"),
            c("Polvoron", "Ube"), c("Polvoron", "Mango"), c("Polvoron", "Macapuno"), c("Polvoron", "Pandan")),
        new FactorySpec("Factory 4", 12, 12,
            c("Gummy", "Tablea"), c("Lollipop", "Tablea"), c("Marshmallow", "Tablea"),
            c("Pastillas", "Tablea"), c("Yema", "Tablea"), c("Polvoron", "Tablea")),
        new FactorySpec("Factory 5", 1, 11,
            c("Gummy", "Jackfruit"), c("Gummy", "Durian"), c("Lollipop", "Jackfruit"),
            c("Lollipop", "Durian"), c("Marshmallow", "Jackfruit"), c("Marshmallow", "Durian")),
        new FactorySpec("Factory 6", 13, 7,
            c("Lollipop", "Ube"), c("Lollipop", "Mango"), c("Lollipop", "Macapuno"),
            c("Lollipop", "Pandan"), c("Lollipop", "Tablea"), c("Lollipop", "Jackfruit"), c("Lollipop", "Durian")),
        new FactorySpec("Factory 7", 6, 13,
            c("Gummy", "Ube"), c("Gummy", "Mango"), c("Gummy", "Macapuno"), c("Gummy", "Pandan"),
            c("Gummy", "Tablea"), c("Gummy", "Jackfruit"), c("Gummy", "Durian")),
        new FactorySpec("Factory 8", 7, 1,
            c("Marshmallow", "Ube"), c("Marshmallow", "Mango"), c("Marshmallow", "Macapuno"),
            c("Marshmallow", "Pandan"), c("Marshmallow", "Tablea"), c("Marshmallow", "Jackfruit"), c("Marshmallow", "Durian")),
        new FactorySpec("Factory 9", 0, 0,
            c("Candy Corn"), c("Candy Cane"), c("Pastillas", "Ube"), c("Pastillas", "Mango"),
            c("Yema", "Ube"), c("Yema", "Mango"), c("Polvoron", "Ube"), c("Polvoron", "Mango")),
        new FactorySpec("Factory 10", 14, 2,
            c("Pastillas", "Macapuno"), c("Pastillas", "Pandan"), c("Pastillas", "Jackfruit"), c("Pastillas", "Durian"),
            c("Yema", "Macapuno"), c("Yema", "Pandan"), c("Yema", "Jackfruit"), c("Yema", "Durian"),
            c("Polvoron", "Macapuno"), c("Polvoron", "Pandan"), c("Polvoron", "Jackfruit"), c("Polvoron", "Durian"))
    };

    private static final HouseSpec[] HOUSES = {
        house("House A", 5, 5, 100, new CandySpec[]{c("Candy Corn"),c("Gummy","Any"),c("Lollipop","Any"),c("Marshmallow","Any")}, new int[]{4,2,2,2}),
        house("House B", 9, 6, 150, new CandySpec[]{c("Candy Corn")}, new int[]{15}),
        house("House C", 7, 10, 250, new CandySpec[]{c("Gummy","Any"),c("Marshmallow","Any"),c("Pastillas","Any"),c("Yema","Any"),c("Polvoron","Any")}, new int[]{5,5,5,5,5}),
        house("House D", 11, 9, 400, new CandySpec[]{c("Candy Corn"),c("Candy Cane"),c("Pastillas","Any"),c("Yema","Any"),c("Polvoron","Any")}, new int[]{5,5,10,10,10}),
        house("House E", 11, 9, 300, new CandySpec[]{c("Candy Corn"),c("Yema","Any"),c("Pastillas","Any"),c("Lollipop","Ube"),c("Lollipop","Macapuno")}, new int[]{10,5,5,5,5}),
        house("House F", 12, 3, 300, new CandySpec[]{c("Gummy","Tablea"),c("Marshmallow","Tablea"),c("Pastillas","Tablea"),c("Polvoron","Tablea")}, new int[]{7,7,8,8}),
        house("House G", 6, 2, 400, new CandySpec[]{c("Pastillas","Any"),c("Pastillas","Durian"),c("Pastillas","Jackfruit")}, new int[]{20,10,10}),
        house("House H", 8, 12, 300, new CandySpec[]{c("Polvoron","Any"),c("Yema","Any"),c("Pastillas","Any"),c("Gummy","Mango"),c("Marshmallow","Pandan")}, new int[]{10,5,5,5,5}),
        house("House I", 1, 4, 450, new CandySpec[]{c("Lollipop","Jackfruit"),c("Lollipop","Durian"),c("Lollipop","Pandan"),c("Lollipop","Ube"),c("Lollipop","Macapuno")}, new int[]{9,9,9,9,9}),
        house("House J", 4, 1, 350, new CandySpec[]{c("Candy Corn"),c("Marshmallow","Mango"),c("Yema","Mango"),c("Pastillas","Mango"),c("Gummy","Mango")}, new int[]{5,6,7,8,9}),
        house("House K", 10, 1, 400, new CandySpec[]{c("Gummy","Ube"),c("Gummy","Mango"),c("Gummy","Macapuno"),c("Gummy","Pandan")}, new int[]{10,10,10,10}),
        house("House L", 13, 6, 300, new CandySpec[]{c("Pastillas","Ube"),c("Yema","Ube"),c("Polvoron","Ube")}, new int[]{10,10,10}),
        house("House M", 13, 13, 300, new CandySpec[]{c("Pastillas","Pandan"),c("Yema","Pandan"),c("Polvoron","Pandan")}, new int[]{10,10,10}),
        house("House N", 3, 14, 250, new CandySpec[]{c("Candy Cane"),c("Polvoron","Jackfruit"),c("Polvoron","Durian"),c("Lollipop","Tablea")}, new int[]{16,3,3,3}),
        house("House O", 14, 0, 1000, new CandySpec[]{c("Candy Cane")}, new int[]{100}),
        house("House P", 14, 10, 250, new CandySpec[]{c("Candy Corn"),c("Candy Cane"),c("Pastillas","Macapuno"),c("Yema","Macapuno"),c("Polvoron","Macapuno")}, new int[]{5,5,5,5,5})
    };

    private static HouseSpec house(String name, int row, int col, int xp, CandySpec[] candies, int[] quantities) {
        return new HouseSpec(name, row, col, xp, candies, quantities);
    }

    private static void check(boolean condition, String description) {
        if (condition) { passed++; System.out.println("[PASS] " + description); }
        else { failed++; System.out.println("[FAIL] " + description); }
    }

    private static Map<String, Integer> candyCounts(Candy[] candies) {
        Map<String, Integer> counts = new HashMap<>();
        for (Candy candy : candies) {
            String flavor = candy.getCandyFlavor();
            if (flavor == null || flavor.equalsIgnoreCase("N/A")) flavor = "N/A";
            String key = candy.getCandyType() + "|" + flavor;
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }

    private static Map<String, Integer> expectedCandyCounts(CandySpec[] candies) {
        Map<String, Integer> counts = new HashMap<>();
        for (CandySpec candy : candies) counts.put(candy.key(), counts.getOrDefault(candy.key(), 0) + 1);
        return counts;
    }

    private static void testFactories(World world) {
        ArrayList<Factory> actual = world.getFactories();
        check(actual.size() == FACTORIES.length, "Exactly 10 factories exist (actual: " + actual.size() + ")");
        for (FactorySpec expected : FACTORIES) {
            Factory found = null;
            for (Factory factory : actual) if (factory.getName().equals(expected.name)) found = factory;
            if (found == null) { check(false, expected.name + " exists"); continue; }
            check(found.getRow() == expected.row && found.getColumn() == expected.column,
                expected.name + " final coordinates expected (" + expected.row + ", " + expected.column + ") actual (" + found.getRow() + ", " + found.getColumn() + ")");
            check(found.getGridSize() == 10, expected.name + " production grid is 10x10 at 5 stars");
            check(candyCounts(found.getAllowedCandies()).equals(expectedCandyCounts(expected.candies)),
                expected.name + " allowed candy types/flavors match Appendix C exactly");
        }
    }

    private static void testHouses(World world) {
        ArrayList<House> actual = world.getHouses();
        check(actual.size() == HOUSES.length, "Exactly 16 houses exist (actual: " + actual.size() + ")");
        for (HouseSpec expected : HOUSES) {
            House found = null;
            for (House house : actual) if (house.getName().equals(expected.name)) found = house;
            if (found == null) { check(false, expected.name + " exists"); continue; }
            check(found.getRow() == expected.row && found.getColumn() == expected.column,
                expected.name + " final coordinates match Appendix D");
            check(found.getXPReward() == expected.xp, expected.name + " XP reward is " + expected.xp);
            Candy[] actualCandies = found.getRequiredCandies();
            int[] actualQuantities = found.getRequiredQuantities();
            boolean same = actualCandies.length == expected.candies.length && actualQuantities.length == expected.quantities.length;
            if (same) {
                for (int i = 0; i < expected.candies.length; i++) {
                    String actualFlavor = actualCandies[i].getCandyFlavor();
                    if (actualFlavor == null) actualFlavor = "N/A";
                    if (!actualCandies[i].getCandyType().equals(expected.candies[i].type)
                        || !actualFlavor.equalsIgnoreCase(expected.candies[i].flavor)
                        || actualQuantities[i] != expected.quantities[i]) same = false;
                }
            }
            check(same, expected.name + " candy types, flavors, quantities, and order sequence match Appendix D");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== CANDY COURIER APPENDIX C & D SPECIFICATION TEST ===");
        Game game = new Game();
        game.initializePlayer("Specification Test");
        game.awardXP(5500);

        check(game.getWorld().getGridSize() == 15, "World expands to 15x15 at 5 stars");
        check(game.getPlayer().getStarRank() == 5, "Player reaches 5-star rank");
        check(game.getPlayer().getInventoryCapacity() == 100, "Inventory capacity is 100 at 5 stars");
        testFactories(game.getWorld());
        testHouses(game.getWorld());

        System.out.println("\n===============================================");
        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);
        System.out.println(failed == 0 ? "RESULT: ALL APPENDIX C & D CHECKS PASSED" : "RESULT: DIFFERENCES FOUND — REVIEW FAILURES ABOVE");
        System.out.println("===============================================");
        if (failed > 0) System.exit(1);
    }
}
