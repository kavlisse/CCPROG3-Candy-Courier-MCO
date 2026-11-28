
package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.*;

public class WorldPanel extends JPanel {

    private final GamePanel parent;
    private final GameController controller;
    private final JPanel grid = new JPanel();

    private static final Color GRASS =
        new Color(119, 176, 111);

    private static final Color FACTORY =
        new Color(245, 190, 211);

    private static final Color HOUSE =
        new Color(216, 204, 241);

    private static final Color HOME =
        new Color(248, 219, 169);

    private static final Color PLAYER =
        new Color(81, 47, 96);

    private static final Color BACKGROUND =
        new Color(29, 21, 38);

    private static final Color ICON_DARK =
        new Color(81, 47, 96);

    public WorldPanel(
        GamePanel parent,
        GameController controller
    ) {
        this.parent = parent;
        this.controller = controller;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        setBorder(BorderFactory.createEmptyBorder(
            18, 18, 18, 18
        ));

        // HEADER

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        heading.setBorder(BorderFactory.createEmptyBorder(
            0, 4, 15, 4
        ));

        JLabel title = new JLabel("WORLD MAP");
        title.setFont(new Font(
            "Segoe UI", Font.BOLD, 21
        ));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Explore locations and complete delivery quests."
        );

        subtitle.setFont(Ui.SMALL);
        subtitle.setForeground(
            new Color(184, 166, 190)
        );

        JPanel headingText = new JPanel();
        headingText.setOpaque(false);

        headingText.setLayout(new BoxLayout(
            headingText, BoxLayout.Y_AXIS
        ));

        headingText.add(title);
        headingText.add(Box.createVerticalStrut(4));
        headingText.add(subtitle);

        heading.add(
            headingText,
            BorderLayout.WEST
        );

        // MAP GRID

        grid.setOpaque(false);

        JPanel mapContainer = new JPanel(
            new BorderLayout()
        );

        mapContainer.setBackground(
            new Color(39, 52, 42)
        );

        mapContainer.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(75, 89, 72), 1
                ),
                BorderFactory.createEmptyBorder(
                    7, 7, 7, 7
                )
            )
        );

        mapContainer.add(
            grid,
            BorderLayout.CENTER
        );

        add(heading, BorderLayout.NORTH);
        add(mapContainer, BorderLayout.CENTER);
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

        grid.setLayout(
            new GridLayout(n, n, 5, 5)
        );

        for (int r = 0; r < n; r++) {

            for (int col = 0; col < n; col++) {

                Building building =
                    w.getBuildingAt(r, col);

                boolean isPlayer =
                    p.getRow() == r &&
                    p.getColumn() == col;

                Color tileColor = GRASS;

                int iconType = MapIcon.NONE;

                String name = "";

                // BUILDING TYPES

                // Check PlayerHome first in case
                // it inherits from House.

                if (building instanceof PlayerHome) {

                    tileColor = HOME;
                    iconType = MapIcon.HOME;
                    name = building.getName();

                } else if (building instanceof Factory) {

                    tileColor = FACTORY;
                    iconType = MapIcon.FACTORY;
                    name = building.getName();

                } else if (building instanceof House) {

                    tileColor = HOUSE;
                    iconType = MapIcon.HOUSE;
                    name = building.getName();
                }

                // PLAYER OVERRIDES LOCATION ICON

                if (isPlayer) {

                    tileColor = PLAYER;
                    iconType = MapIcon.PLAYER;
                    name = p.getName();
                }

                // CREATE TILE

                JButton tile = new JButton();

                tile.setLayout(
                    new BorderLayout(2, 4)
                );

                tile.setBackground(tileColor);
                tile.setOpaque(true);

                tile.setFocusPainted(false);
                tile.setContentAreaFilled(true);
                tile.setBorderPainted(true);

                tile.setCursor(
                    Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                    )
                );

                tile.setMargin(
                    new Insets(8, 5, 8, 5)
                );

                tile.setBorder(
                    BorderFactory.createLineBorder(
                        new Color(255, 255, 255, 125),
                        2
                    )
                );

                // ONLY DISPLAY ICONS FOR ACTUAL OBJECTS

                if (iconType != MapIcon.NONE) {

                    MapIcon icon = new MapIcon(iconType);

                    tile.add(
                        icon,
                        BorderLayout.CENTER
                    );

                    JLabel label = new JLabel(
                        name,
                        SwingConstants.CENTER
                    );

                    label.setFont(new Font(
                        "Segoe UI", Font.BOLD, 10
                    ));

                    label.setForeground(
                        isPlayer
                            ? Color.WHITE
                            : ICON_DARK
                    );

                    tile.add(
                        label,
                        BorderLayout.SOUTH
                    );
                }

                // PLAYER HIGHLIGHT

                if (isPlayer) {

                    tile.setBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                Ui.GOLD, 3
                            ),
                            BorderFactory.createEmptyBorder(
                                7, 4, 7, 4
                            )
                        )
                    );
                }

                // INTERACTION

                final int row = r;
                final int column = col;

                tile.addActionListener(e -> {

                    if (p.getRow() == row
                            && p.getColumn() == column) {

                        parent.interactCurrentLocation();
                    }
                });

                grid.add(tile);
            }
        }

        grid.revalidate();
        grid.repaint();
    }

    // ==========================================
    // CUSTOM DRAWN MAP ICONS
    // ==========================================

    private static class MapIcon extends JComponent {

        static final int NONE = 0;
        static final int FACTORY = 1;
        static final int HOUSE = 2;
        static final int HOME = 3;
        static final int PLAYER = 4;

        private final int type;

        MapIcon(int type) {

            this.type = type;

            setPreferredSize(
                new Dimension(44, 44)
            );

            setMinimumSize(
                new Dimension(35, 35)
            );

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            switch (type) {

                case FACTORY:
                    drawFactory(g2, cx, cy);
                    break;

                case HOUSE:
                case HOME:
                    drawHouse(g2, cx, cy);
                    break;

                case PLAYER:
                    drawPlayer(g2, cx, cy);
                    break;
            }

            g2.dispose();
        }

        // FACTORY ICON

        private void drawFactory(
            Graphics2D g2,
            int cx,
            int cy
        ) {

            g2.setColor(ICON_DARK);

            // Main building

            g2.fillRoundRect(
                cx - 15, cy - 1,
                29, 19,
                3, 3
            );

            // Left chimney

            g2.fillRect(
                cx - 13, cy - 17,
                6, 18
            );

            // Center chimney

            g2.fillRect(
                cx - 3, cy - 12,
                6, 13
            );

            // Windows

            g2.setColor(Ui.PINK);

            g2.fillRect(
                cx - 10, cy + 5,
                4, 5
            );

            g2.fillRect(
                cx, cy + 5,
                4, 5
            );

            g2.fillRect(
                cx + 8, cy + 5,
                4, 5
            );

            // Chimney detail

            g2.setColor(Ui.GOLD);

            g2.fillRect(
                cx - 11, cy - 14,
                2, 3
            );

            g2.fillRect(
                cx - 1, cy - 9,
                2, 3
            );
        }

        // HOUSE ICON

        private void drawHouse(
            Graphics2D g2,
            int cx,
            int cy
        ) {

            g2.setColor(ICON_DARK);

            // Roof

            Polygon roof = new Polygon();

            roof.addPoint(cx - 19, cy - 1);
            roof.addPoint(cx, cy - 19);
            roof.addPoint(cx + 19, cy - 1);

            g2.fillPolygon(roof);

            // House body

            g2.fillRoundRect(
                cx - 13, cy - 2,
                26, 21,
                3, 3
            );

            // Door

            g2.setColor(Ui.LAVENDER);

            g2.fillRoundRect(
                cx - 4, cy + 7,
                8, 12,
                2, 2
            );
        }

        // PLAYER ICON

        private void drawPlayer(
            Graphics2D g2,
            int cx,
            int cy
        ) {

            // Head

            g2.setColor(
                new Color(255, 218, 157)
            );

            g2.fillOval(
                cx - 9, cy - 19,
                18, 18
            );

            // Body

            g2.setColor(Ui.GOLD);

            g2.fillRoundRect(
                cx - 13, cy,
                26, 19,
                7, 7
            );

            // Shirt detail

            g2.setColor(Color.WHITE);

            g2.fillRoundRect(
                cx - 5, cy + 4,
                10, 9,
                3, 3
            );
        }
    }
}
