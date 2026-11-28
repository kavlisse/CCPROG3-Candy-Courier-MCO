package view;

import java.awt.*;
import javax.swing.*;

import model.*;
import controller.GameController;

public class OrdersPanel extends JPanel {

    private final GameController c;

    private final DefaultListModel<String> model =
        new DefaultListModel<>();

    public OrdersPanel(GameController c) {

        this.c = c;

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Delivery orders"),
            BorderLayout.NORTH
        );

        JList<String> list = new JList<>(model);

        list.setFont(
            new Font("SansSerif", Font.PLAIN, 15)
        );

        add(new JScrollPane(list), BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {

        model.clear();

        for (
            House h :
            c.getHouseController().getHouses()
        ) {

            StringBuilder s = new StringBuilder(
                h.getName() +
                " — " +
                (h.isDelivered() ? "DELIVERED" : "PENDING") +
                " — " +
                h.getXPReward() +
                " XP"
            );

            Candy[] cs = h.getRequiredCandies();
            int[] qs = h.getRequiredQuantities();

            for (int i = 0; i < cs.length; i++) {

                s.append(" | ")
                    .append(cs[i].getCandyType())
                    .append(" × ")
                    .append(qs[i]);
            }

            model.addElement(s.toString());
        }
    }
}