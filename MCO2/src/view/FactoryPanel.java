package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.*;

public class FactoryPanel {

    private FactoryPanel() {
    }

    public static void showDialog(
        Component parent,
        GameController c,
        Factory factory
    ) {

        Candy[][] candies = factory.getProductionGrid();

        JPanel panel = new JPanel(
            new GridLayout(
                candies.length,
                candies.length,
                4,
                4
            )
        );

        final int[] picked = {0};

        for (int r = 0; r < candies.length; r++) {

            for (int col = 0; col < candies.length; col++) {

                final int rr = r;
                final int cc = col;

                Candy candy = candies[r][col];

                JButton b = new JButton(
                    candy == null
                        ? "—"
                        : candy.getCandyType() +
                          "\n" +
                          candy.getCandyFlavor()
                );

                b.setPreferredSize(
                    new Dimension(100, 54)
                );

                b.setBackground(
                    candy == null
                        ? Color.LIGHT_GRAY
                        : Ui.PALE
                );

                b.addActionListener(e -> {

                    if (
                        c.getFactoryController()
                            .handleCandyPickup(factory, rr, cc)
                    ) {

                        picked[0]++;

                        b.setText("Empty");
                        b.setEnabled(false);

                    } else {

                        JOptionPane.showMessageDialog(
                            parent,
                            "Could not collect this candy. Check your inventory capacity."
                        );
                    }
                });

                panel.add(b);
            }
        }

        JScrollPane scroll = new JScrollPane(panel);

        scroll.setPreferredSize(
            new Dimension(560, 420)
        );

        JOptionPane.showMessageDialog(
            parent,
            scroll,
            factory.getName() + " — Candy production",
            JOptionPane.PLAIN_MESSAGE
        );

        JOptionPane.showMessageDialog(
            parent,
            "Collected " + picked[0] + " candy item(s)."
        );
    }
}