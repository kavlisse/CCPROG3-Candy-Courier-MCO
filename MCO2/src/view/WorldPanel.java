
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

        setLayout(new BorderLayout());
        setBackground(Ui.CREAM);

        grid.setBackground(Ui.CREAM);
        add(grid, BorderLayout.CENTER);
    }

    public void refresh() {

        grid.removeAll();

        Player p = controller.getGame().getPlayer();
        World w = controller.getGame().getWorld();

        if (p == null || w == null) {
            return;
        }

        int n = w.getGridSize();

        grid.setLayout(
            new GridLayout(n, n, 4, 4)
        );

        for (int r = 0; r < n; r++) {

            for (int c = 0; c < n; c++) {

                final int rr = r;
                final int cc = c;

                Building b = w.getBuildingAt(r, c);

                boolean isPlayer =
                    p.getRow() == r &&
                    p.getColumn() == c;

                String symbol = "";
                String name = "";

                Color background = Ui.WHITE;
                Color foreground = Ui.INK;
                Color borderColor = Ui.BORDER;

                // Factory tile
                if (b instanceof Factory) {

                    symbol = "F";
                    name = b.getName();

                    background = new Color(255, 225, 236);
                    foreground = Ui.DARK;

                }

                // House tile
                else if (b instanceof House) {

                    symbol = "H";
                    name = b.getName();

                    background = Ui.LAVENDER;
                    foreground = new Color(104, 75, 139);

                }

                // Player home tile
                else if (b instanceof PlayerHome) {

                    symbol = "⌂";
                    name = b.getName();

                    background = new Color(255, 239, 218);
                    foreground = new Color(151, 104, 42);

                }

                // Player tile
                if (isPlayer) {

                    symbol = "●";
                    name = "YOU";

                    background = Ui.BERRY;
                    foreground = Ui.WHITE;
                    borderColor = Ui.DARK;
                }

                JLabel icon = new JLabel(
                    symbol.isEmpty() ? " " : symbol,
                    SwingConstants.CENTER
                );

                icon.setFont(
                    new Font("Segoe UI Symbol", Font.BOLD, 27)
                );

                icon.setForeground(foreground);

                JLabel buildingName = new JLabel(
                    name,
                    SwingConstants.CENTER
                );

                buildingName.setFont(
                    new Font("Trebuchet MS", Font.BOLD, 10)
                );

                buildingName.setForeground(foreground);

                JPanel tileContent = new JPanel(
                    new BorderLayout(2, 2)
                );

                tileContent.setOpaque(false);
                tileContent.add(icon, BorderLayout.CENTER);
                tileContent.add(buildingName, BorderLayout.SOUTH);

                JButton cell = new JButton();

                cell.setLayout(new BorderLayout());
                cell.add(tileContent, BorderLayout.CENTER);

                cell.setBackground(background);
                cell.setOpaque(true);
                cell.setFocusPainted(false);
                cell.setContentAreaFilled(true);

                cell.setBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                            borderColor,
                            isPlayer ? 3 : 1
                        ),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                    )
                );

                cell.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
                );

                String coordinates =
                    "Row " + (r + 1) +
                    ", Column " + (c + 1);

                cell.setToolTipText(
                    coordinates +
                    (name.isEmpty() ? " — Empty tile" : " — " + name)
                );

                cell.addActionListener(e -> {

                    if (
                        rr == p.getRow() &&
                        cc == p.getColumn()
                    ) {

                        parent.interactCurrentLocation();

                    } else {

                        // Movement is handled by the arrow keys
                        // or WASD controls.
                        Toolkit.getDefaultToolkit().beep();
                    }
                });

                grid.add(cell);
            }
        }

        grid.revalidate();
        grid.repaint();
    }
}
