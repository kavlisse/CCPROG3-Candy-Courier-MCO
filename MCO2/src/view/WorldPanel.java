
package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.*;

public class WorldPanel extends JPanel {

    private final GamePanel parent;
    private final GameController controller;
    private final JPanel grid = new JPanel();

    private static final Color GRASS = new Color(91, 155, 83);
    private static final Color FACTORY = new Color(238, 180, 202);
    private static final Color HOUSE = new Color(190, 178, 225);
    private static final Color HOME = new Color(239, 205, 145);
    private static final Color PLAYER = new Color(75, 45, 100);
    private static final Color BACKGROUND = new Color(28, 35, 31);

    public WorldPanel(GamePanel parent, GameController controller) {
        this.parent = parent;
        this.controller = controller;

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        grid.setBackground(BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);

        add(grid, gbc);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        grid.revalidate();
    }

    public void refresh() {
        grid.removeAll();

        Player p = controller.getGame().getPlayer();
        World w = controller.getGame().getWorld();

        if (p == null || w == null) {
            grid.revalidate();
            grid.repaint();
            return;
        }

        int n = w.getGridSize();

        grid.setLayout(new GridLayout(n, n, 3, 3));

        for (int r = 0; r < n; r++) {
            for (int col = 0; col < n; col++) {

                Building building = w.getBuildingAt(r, col);

                boolean isPlayer =
                    p.getRow() == r && p.getColumn() == col;

                Color tileColor = GRASS;
                String type = "empty";
                String name = "";

                if (building instanceof Factory) {
                    tileColor = FACTORY;
                    type = "factory";
                    name = building.getName();

                } else if (building instanceof House) {
                    tileColor = HOUSE;
                    type = "house";
                    name = building.getName();

                } else if (building instanceof PlayerHome) {
                    tileColor = HOME;
                    type = "home";
                    name = building.getName();
                }

                if (isPlayer) {
                    tileColor = PLAYER;
                    type = "player";
                    name = p.getName();
                }

                JButton tile = new JButton();
                tile.setLayout(new BorderLayout(2, 2));
                tile.setBackground(tileColor);
                tile.setOpaque(true);
                tile.setFocusPainted(false);
                tile.setMargin(new Insets(2, 2, 2, 2));

                tile.setBorder(BorderFactory.createLineBorder(
                    new Color(225, 225, 225), 2
                ));

                JLabel icon = new JLabel(
                    new TileIcon(type), SwingConstants.CENTER
                );

                JLabel label = new JLabel(name, SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                label.setForeground(
                    isPlayer ? Color.WHITE : new Color(45, 38, 48)
                );

                tile.add(icon, BorderLayout.CENTER);
                tile.add(label, BorderLayout.SOUTH);

                final int row = r;
                final int column = col;

                tile.addActionListener(e -> {
                    if (p.getRow() == row && p.getColumn() == column) {
                        parent.interactCurrentLocation();
                    }
                });

                grid.add(tile);
            }
        }

        grid.revalidate();
        grid.repaint();
    }

    private static class TileIcon implements Icon {

        private final String type;

        TileIcon(String type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return 42;
        }

        @Override
        public int getIconHeight() {
            return 42;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            switch (type) {
                case "factory":
                    g2.setColor(new Color(100, 55, 75));
                    g2.fillRect(x + 7, y + 16, 28, 21);
                    g2.fillPolygon(
                        new int[]{
                            x + 7, x + 16, x + 16,
                            x + 25, x + 25, x + 35
                        },
                        new int[]{
                            y + 16, y + 8, y + 16,
                            y + 8, y + 16, y + 16
                        },
                        6
                    );
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x + 12, y + 24, 5, 7);
                    g2.fillRect(x + 24, y + 24, 5, 7);
                    break;

                case "house":
                case "home":
                    g2.setColor(type.equals("home")
                        ? new Color(115, 76, 35)
                        : new Color(85, 65, 125));

                    g2.fillPolygon(
                        new int[]{x + 5, x + 21, x + 37},
                        new int[]{y + 20, y + 5, y + 20},
                        3
                    );
                    g2.fillRect(x + 10, y + 19, 22, 19);

                    g2.setColor(Color.WHITE);
                    g2.fillRect(x + 18, y + 25, 7, 13);
                    break;

                case "player":
                    g2.setColor(new Color(255, 220, 165));
                    g2.fillOval(x + 13, y + 3, 16, 16);

                    g2.setColor(new Color(245, 190, 75));
                    g2.fillRoundRect(x + 9, y + 19, 24, 20, 8, 8);

                    g2.setColor(Color.WHITE);
                    g2.fillRect(x + 14, y + 25, 14, 5);
                    break;

                default:
                    g2.setColor(new Color(115, 170, 105));
                    g2.fillOval(x + 15, y + 15, 12, 12);
            }

            g2.dispose();
        }
    }
}
