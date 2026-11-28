package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.GameController;
import model.Candy;

public class InventoryPanel extends JPanel {
    private final GameController c;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);
    private final JLabel countLabel = Ui.heading("🎒  0 / 0");
    private final JLabel selectionStatus = Ui.smallLabel("Select candies to discard.");

    public InventoryPanel(GameController c) {
        this.c = c;
        setLayout(new BorderLayout(14, 14));
        setBackground(Ui.CREAM);
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(Ui.heading("Your Inventory"), BorderLayout.WEST);
        top.add(countLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setFont(Ui.BODY);
        list.setFixedCellHeight(52);
        list.setBackground(Ui.WHITE);
        list.setForeground(Ui.DARK);
        list.setBorder(new EmptyBorder(8, 8, 8, 8));

        list.setCellRenderer((items, value, index, selected, focus) -> {
            JLabel item = new JLabel(value);
            item.setOpaque(true);
            item.setBorder(new EmptyBorder(10, 14, 10, 14));
            item.setFont(Ui.BODY);
            item.setBackground(selected ? Ui.BERRY
                : index % 2 == 0 ? Ui.WHITE : Ui.BLUSH);
            item.setForeground(selected ? Ui.WHITE : Ui.DARK);
            return item;
        });

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectionStatus.setText(
                    list.getSelectedIndices().length + " item(s) selected");
            }
        });

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(Ui.BORDER));
        add(scroll, BorderLayout.CENTER);

        JButton discard = Ui.button("🗑  DISCARD SELECTED");
        discard.addActionListener(e -> discardSelected());

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setOpaque(false);
        bottom.add(selectionStatus, BorderLayout.NORTH);
        bottom.add(discard, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    private void discardSelected() {
        int[] selected = list.getSelectedIndices();

        if (selected.length == 0) {
            Ui.message(this, "Select at least one candy to discard.");
            return;
        }

        if (!Ui.confirm(this, "Discard candies?",
                "Discard " + selected.length + " selected item(s)?",
                "Discard", "Cancel")) return;

        ArrayList<Integer> slots = new ArrayList<>();
        for (int index : selected) slots.add(index + 1);

        int removed = c.getInventoryController().discardMultipleCandies(slots);
        Ui.message(this, removed + " candy item(s) discarded.");
        refresh();
    }

    public void refresh() {
        model.clear();

        ArrayList<Candy> candies = c.getInventoryController().getCandies();
        int count = c.getInventoryController().getCandyCount();
        int capacity = c.getInventoryController().getCapacity();

        countLabel.setText("🎒  " + count + " / " + capacity);

        for (int i = 0; i < candies.size(); i++) {
            Candy candy = candies.get(i);
            model.addElement("🍬   SLOT " + (i + 1) + "     "
                + candy.getCandyType() + "  —  " + candy.getCandyFlavor());
        }

        if (candies.isEmpty()) model.addElement("Your inventory is empty.");

        selectionStatus.setText("Select candies to discard.");
        list.clearSelection();
    }
}