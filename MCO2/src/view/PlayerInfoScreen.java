
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import controller.GameController;
import model.Player;

/**
 * Displays the player's courier profile, starting attributes,
 * and current game statistics.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
class PlayerInfoScreen extends JPanel {

    private final GameController controller;

    private final JLabel nameLabel = new JLabel();
    private final JLabel rankLabel = new JLabel();

    private final JLabel xpValue = new JLabel();
    private final JLabel nextRankValue = new JLabel();
    private final JLabel stepsValue = new JLabel();
    private final JLabel inventoryValue = new JLabel();

    private final JLabel worldValue = new JLabel();
    private final JLabel factoryValue = new JLabel();
    private final JLabel movementValue = new JLabel();
    private final JLabel locationValue = new JLabel();

    PlayerInfoScreen(GameController c) {

        controller = c;

        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);

        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(Ui.heading("Courier Profile"), BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Ui.PALE, 2),
                new EmptyBorder(22, 24, 22, 24)
            )
        );

        // PLAYER IDENTITY
        JLabel section = sectionTitle("YOUR COURIER");
        card.add(section);

        nameLabel.setFont(
            new Font("Trebuchet MS", Font.BOLD, 27)
        );
        nameLabel.setForeground(Ui.DARK);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(8));
        card.add(nameLabel);

        rankLabel.setFont(
            new Font("Trebuchet MS", Font.BOLD, 16)
        );
        rankLabel.setForeground(Ui.PINK);
        rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(5));
        card.add(rankLabel);

        card.add(Box.createVerticalStrut(25));

        // GAME PROGRESS
        card.add(sectionTitle("GAME STATISTICS"));

        addStat(card, "Total Experience Points", xpValue);
        addStat(card, "XP Needed for Next Rank", nextRankValue);
        addStat(card, "Total Steps Taken", stepsValue);
        addStat(card, "Inventory", inventoryValue);

        card.add(Box.createVerticalStrut(22));

        // WORLD INFORMATION
        card.add(sectionTitle("WORLD INFORMATION"));

        addStat(card, "World Grid Size", worldValue);
        addStat(card, "Factory Grid Size", factoryValue);
        addStat(card, "Current Location", locationValue);
        addStat(card, "Diagonal Movement", movementValue);

        card.add(Box.createVerticalStrut(22));

        // STARTING ATTRIBUTES
        card.add(sectionTitle("STARTING ATTRIBUTES"));

        JLabel startingInfo = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Every courier begins with 0 XP, 0 stars, "
            + "20 inventory slots, and a 7 × 7 world."
            + "<br>Factory production grids initially measure 6 × 6."
            + "</div></html>"
        );

        startingInfo.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 14)
        );
        startingInfo.setForeground(Ui.INK);
        startingInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(startingInfo);

        add(card, BorderLayout.CENTER);

        refresh();
    }

    private JLabel sectionTitle(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Trebuchet MS", Font.BOLD, 15)
        );

        label.setForeground(Ui.PINK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private void addStat(
        JPanel parent,
        String title,
        JLabel value
    ) {

        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(Color.WHITE);

        row.setBorder(
            BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Ui.PALE),
                new EmptyBorder(9, 0, 9, 0)
            )
        );

        JLabel label = new JLabel(title);

        label.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 14)
        );
        label.setForeground(Ui.INK);

        value.setFont(
            new Font("Trebuchet MS", Font.BOLD, 14)
        );
        value.setForeground(Ui.DARK);
        value.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(label, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);

        parent.add(row);
    }

    public void refresh() {

        Player p = controller.getGame().getPlayer();

        if (p == null) {

            nameLabel.setText("No player yet");
            rankLabel.setText("Start a new game to create your courier.");

            xpValue.setText("-");
            nextRankValue.setText("-");
            stepsValue.setText("-");
            inventoryValue.setText("-");
            worldValue.setText("-");
            factoryValue.setText("-");
            locationValue.setText("-");
            movementValue.setText("-");

            return;
        }

        int gridSize =
            controller.getGame().getWorld().getGridSize();

        nameLabel.setText(p.getName());

        rankLabel.setText(
            "★ " + p.getStarRank() + " STAR RANK"
        );

        xpValue.setText(p.getTotalXP() + " XP");

        nextRankValue.setText(
            p.getXPNeededForNextRank() + " XP"
        );

        stepsValue.setText(
            String.valueOf(p.getTotalSteps())
        );

        inventoryValue.setText(
            p.getInventory().getCandyCount()
            + " / "
            + p.getInventory().getCapacity()
            + " candies"
        );

        worldValue.setText(
            gridSize + " × " + gridSize
        );

        factoryValue.setText(
            p.getFactoryGridSize() + " × "
            + p.getFactoryGridSize()
        );

        locationValue.setText(
            "Row " + (p.getRow() + 1)
            + ", Column " + (p.getColumn() + 1)
        );

        movementValue.setText(
            p.canMoveDiagonally()
                ? "Unlocked"
                : "Locked (3-star requirement)"
        );
    }
}
