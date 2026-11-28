package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import controller.GameController;
import model.Candy;

public class InventoryPanel extends JPanel {

    private final GameController c;

    private final DefaultListModel<String> model =
        new DefaultListModel<>();

    private final JList<String> list =
        new JList<>(model);

    public InventoryPanel(GameController c) {

        this.c = c;

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Candy inventory"),
            BorderLayout.NORTH
        );

        list.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        list.setFont(
            new Font("SansSerif", Font.PLAIN, 15)
        );

        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton discard = Ui.button(
            "Discard selected candy"
        );

        discard.addActionListener(e -> {

            int[] selected = list.getSelectedIndices();

            ArrayList<Integer> slots = new ArrayList<>();

            for (int i : selected) {
                slots.add(i + 1);
            }

            int removed =
                c.getInventoryController()
                    .discardMultipleCandies(slots);

            JOptionPane.showMessageDialog(
                this,
                removed + " candy item(s) discarded."
            );

            refresh();
        });

        add(discard, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {

        model.clear();

        ArrayList<Candy> candies =
            c.getInventoryController().getCandies();

        for (int i = 0; i < candies.size(); i++) {

            Candy x = candies.get(i);

            model.addElement(
                "Slot " + (i + 1) +
                " — " + x.getCandyType() +
                " / " + x.getCandyFlavor()
            );
        }

        setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
}