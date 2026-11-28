package view;

import java.awt.*;
import javax.swing.*;

import model.*;
import controller.GameController;

public class WorldPanel extends JPanel {

    private final GamePanel parent;
    private final GameController controller;

    private final JPanel grid = new JPanel();

    public WorldPanel(
        GamePanel parent,
        GameController controller
    ) {

        this.parent = parent;
        this.controller = controller;

        setLayout(new BorderLayout(8, 8));
        setBackground(Ui.CREAM);

        add(Ui.heading("Your world"), BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }

    public void refresh() {

        grid.removeAll();

        Player p = controller.getGame().getPlayer();
        World w = controller.getGame().getWorld();

        int n = w.getGridSize();

        grid.setLayout(
            new GridLayout(n, n, 4, 4)
        );

        for (int r = 0; r < n; r++) {

            for (int c = 0; c < n; c++) {

                final int rr = r;
                final int cc = c;

                Building b = w.getBuildingAt(r, c);

                String symbol;

                if (p.getRow() == r && p.getColumn() == c) {
                    symbol = "🧑";
                } else if (b == null) {
                    symbol = "·";
                } else {
                    symbol = String.valueOf(b.getSymbol());
                }

                JButton cell = new JButton(symbol);

                cell.setFont(
                    new Font("SansSerif", Font.BOLD, 18)
                );

                cell.setBackground(
                    (r + c) % 2 == 0
                        ? Color.WHITE
                        : Ui.PALE
                );

                cell.setForeground(Ui.DARK);

                cell.setToolTipText(
                    "Row " + (r + 1) +
                    ", Column " + (c + 1) +
                    (b == null ? "" : " — " + b.getName())
                );

                cell.addActionListener(e -> {

                    if (
                        rr == p.getRow() &&
                        cc == p.getColumn()
                    ) {
                        parent.interactCurrentLocation();
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Move one tile at a time using the direction controls."
                        );
                    }
                });

                grid.add(cell);
            }
        }

        grid.revalidate();
        grid.repaint();
    }
}