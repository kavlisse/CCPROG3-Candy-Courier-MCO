
package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Candy;

public class InventoryPanel extends JPanel {

    private final GameController c;

    private final DefaultListModel<String> model =
        new DefaultListModel<>();

    private final JList<String> list = new JList<>(model);

    private final JLabel countLabel =
        Ui.heading("0 / 0");

    private final JLabel selectionStatus =
        Ui.smallLabel("Select candies to discard.");

    private final JProgressBar capacityBar =
        Ui.progressBar(0, 100);

    public InventoryPanel(GameController c) {
        this.c = c;

        setLayout(new BorderLayout(18, 18));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(
            titleGroup, BoxLayout.Y_AXIS
        ));

        titleGroup.add(Ui.eyebrow("YOUR COLLECTION"));
        titleGroup.add(Box.createVerticalStrut(5));
        titleGroup.add(Ui.heading("Candy Inventory"));

        JPanel capacity = Ui.card();
        capacity.setLayout(new BoxLayout(
            capacity, BoxLayout.Y_AXIS
        ));
        capacity.setPreferredSize(new Dimension(220, 85));

        JLabel capacityTitle = Ui.smallLabel("INVENTORY CAPACITY");
        capacityTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        countLabel.setFont(new Font(
            "Segoe UI", Font.BOLD, 21
        ));

        capacityBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        capacityBar.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 8)
        );

        capacity.add(capacityTitle);
        capacity.add(Box.createVerticalStrut(5));
        capacity.add(countLabel);
        capacity.add(Box.createVerticalStrut(8));
        capacity.add(capacityBar);

        header.add(titleGroup, BorderLayout.WEST);
        header.add(capacity, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        list.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
        list.setFont(Ui.BODY);
        list.setFixedCellHeight(65);
        list.setBackground(Ui.CREAM);
        list.setForeground(Ui.DARK);
        list.setBorder(new EmptyBorder(5, 5, 5, 5));

        list.setCellRenderer((items, value, index,
                              selected, focus) -> {

            JPanel item = new JPanel(new BorderLayout(12, 4));
            item.setOpaque(true);

            item.setBackground(selected
                ? Ui.BLUSH
                : Color.WHITE);

            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    selected ? Ui.BERRY : Ui.BORDER
                ),
                new EmptyBorder(10, 15, 10, 15)
            ));

            JLabel candy = new JLabel("🍬");
            candy.setFont(new Font(
                "Segoe UI Emoji", Font.PLAIN, 25
            ));

            JLabel details = new JLabel(value);
            details.setFont(new Font(
                "Segoe UI", Font.BOLD, 13
            ));
            details.setForeground(Ui.DARK);

            item.add(candy, BorderLayout.WEST);
            item.add(details, BorderLayout.CENTER);

            return item;
        });

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectionStatus.setText(
                    list.getSelectedIndices().length
                    + " candy item(s) selected"
                );
            }
        });

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(15);

        add(scroll, BorderLayout.CENTER);

        JButton discard = Ui.button("DISCARD SELECTED  →");
        discard.addActionListener(e -> discardSelected());

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(selectionStatus, BorderLayout.WEST);
        footer.add(discard, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        refresh();
    }

    private void discardSelected() {
        int[] selected = list.getSelectedIndices();

        if (selected.length == 0) {
            Ui.message(
                this,
                "Select at least one candy to discard."
            );
            return;
        }

        boolean confirmed = Ui.confirm(
            this,
            "DISCARD CANDIES",
            "Discard " + selected.length
                + " selected candy item(s)? This cannot be undone.",
            "DISCARD",
            "CANCEL"
        );

        if (!confirmed) return;

        ArrayList<Integer> slots = new ArrayList<>();

        for (int index : selected) {
            slots.add(index + 1);
        }

        int removed = c.getInventoryController()
            .discardMultipleCandies(slots);

        Ui.message(
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

        countLabel.setText(count + " / " + capacity);

        capacityBar.setMaximum(Math.max(1, capacity));
        capacityBar.setValue(count);

        for (int i = 0; i < candies.size(); i++) {
            Candy candy = candies.get(i);

            model.addElement(
                "SLOT " + (i + 1)
                + "     "
                + candy.getCandyType()
                + "  —  "
                + candy.getCandyFlavor()
            );
        }

        if (candies.isEmpty()) {
            model.addElement("Your inventory is empty.");
        }

        selectionStatus.setText("Select candies to discard.");
        list.clearSelection();
    }
}
