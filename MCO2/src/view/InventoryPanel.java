
package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Candy;

/**
 * Displays the player's candy inventory and allows
 * the player to discard one or multiple candies.
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class InventoryPanel extends JPanel {

    private static final Color PINK = new Color(174, 76, 125);
    private static final Color LIGHT_PINK = new Color(250, 231, 239);
    private static final Color CREAM = new Color(255, 248, 244);
    private static final Color DARK = new Color(105, 42, 75);
    private static final Color WHITE = Color.WHITE;

    private final GameController c;

    private final DefaultListModel<String> model =
        new DefaultListModel<>();

    private final JList<String> list =
        new JList<>(model);

    private final JLabel inventoryStatus =
        new JLabel("0 / 0 CANDIES");

    private final JLabel selectionStatus =
        new JLabel("Select candies to discard.");

    public InventoryPanel(GameController c) {

        this.c = c;

        setLayout(new BorderLayout(15, 15));
        setBackground(CREAM);

        setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Heading and inventory capacity
        JPanel top = new JPanel(new BorderLayout(8, 10));
        top.setBackground(CREAM);

        JLabel title = new JLabel("Candy Inventory");
        title.setFont(
            new Font("Trebuchet MS", Font.BOLD, 28)
        );
        title.setForeground(DARK);

        JLabel subtitle = new JLabel(
            "Manage the candies you have collected."
        );
        subtitle.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 14)
        );
        subtitle.setForeground(PINK);

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(CREAM);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        inventoryStatus.setFont(
            new Font("Trebuchet MS", Font.BOLD, 18)
        );
        inventoryStatus.setForeground(DARK);
        inventoryStatus.setOpaque(true);
        inventoryStatus.setBackground(LIGHT_PINK);
        inventoryStatus.setBorder(
            new EmptyBorder(12, 15, 12, 15)
        );

        top.add(heading, BorderLayout.NORTH);
        top.add(inventoryStatus, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);

        // Candy list
        list.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        list.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 15)
        );

        list.setBackground(WHITE);
        list.setForeground(DARK);
        list.setFixedCellHeight(58);

        list.setBorder(
            new EmptyBorder(8, 8, 8, 8)
        );

        list.setCellRenderer((list, value, index, selected, focus) -> {

            JLabel item = new JLabel(value);

            item.setOpaque(true);
            item.setBorder(
                new EmptyBorder(10, 15, 10, 15)
            );

            item.setFont(
                new Font("Trebuchet MS", Font.BOLD, 15)
            );

            if (selected) {
                item.setBackground(PINK);
                item.setForeground(WHITE);
            } else {
                item.setBackground(
                    index % 2 == 0 ? WHITE : LIGHT_PINK
                );
                item.setForeground(DARK);
            }

            return item;
        });

        list.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                selectionStatus.setText(
                    list.getSelectedIndices().length +
                    " candy item(s) selected."
                );
            }
        });

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(
            BorderFactory.createLineBorder(
                new Color(238, 190, 210)
            )
        );

        add(scroll, BorderLayout.CENTER);

        // Bottom controls
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(CREAM);

        selectionStatus.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 14)
        );
        selectionStatus.setForeground(DARK);

        JButton discard = new JButton("DISCARD SELECTED CANDIES");
        discard.setFont(
            new Font("Trebuchet MS", Font.BOLD, 14)
        );
        discard.setForeground(WHITE);
        discard.setBackground(PINK);
        discard.setFocusPainted(false);
        discard.setBorder(
            new EmptyBorder(14, 20, 14, 20)
        );

        discard.addActionListener(e -> discardSelected());

        bottom.add(selectionStatus, BorderLayout.NORTH);
        bottom.add(discard, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    private void discardSelected() {

        int[] selected = list.getSelectedIndices();

        if (selected.length == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Please select at least one candy to discard."
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to discard " +
            selected.length + " candy item(s)?",
            "Discard Candies",
            JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        ArrayList<Integer> slots = new ArrayList<>();

        for (int index : selected) {
            slots.add(index + 1);
        }

        int removed =
            c.getInventoryController()
                .discardMultipleCandies(slots);

        JOptionPane.showMessageDialog(
            this,
            removed + " candy item(s) discarded."
        );

        refresh();
    }

    public void refresh() {

        model.clear();

        ArrayList<Candy> candies =
            c.getInventoryController().getCandies();

        int count =
            c.getInventoryController().getCandyCount();

        int capacity =
            c.getInventoryController().getCapacity();

        inventoryStatus.setText(
            count + " / " + capacity + " CANDIES"
        );

        for (int i = 0; i < candies.size(); i++) {

            Candy candy = candies.get(i);

            model.addElement(
                "  SLOT " + (i + 1) +
                "     |     " + candy.getCandyType() +
                "     —     " + candy.getCandyFlavor()
            );
        }

        if (candies.isEmpty()) {
            model.addElement("  Your inventory is currently empty.");
        }

        selectionStatus.setText(
            "Select one or more candies to discard."
        );

        list.clearSelection();
    }
}
