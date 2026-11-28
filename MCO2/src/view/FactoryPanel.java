
package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Factory;
import model.Candy;

public class FactoryPanel {

    private FactoryPanel() {}

    public static void showDialog(
        Component parent,
        GameController c,
        Factory factory
    ) {
        Candy[][] candies =
            c.getFactoryController().getProductionGrid(factory);

        if (candies == null || candies.length == 0
                || candies[0] == null) {
            Ui.message(parent,
                "This factory has no production grid.");
            return;
        }

        int rows = candies.length;
        int columns = candies[0].length;

        boolean[][] selected = new boolean[rows][columns];
        JButton[][] candyButtons = new JButton[rows][columns];

        JDialog dialog = new JDialog(
            SwingUtilities.getWindowAncestor(parent),
            factory.getName(),
            Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(Ui.CREAM);
        root.setBorder(new EmptyBorder(24, 26, 22, 26));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(
            titleGroup, BoxLayout.Y_AXIS
        ));

        titleGroup.add(Ui.eyebrow("CANDY PRODUCTION"));
        titleGroup.add(Box.createVerticalStrut(5));
        titleGroup.add(Ui.heading(factory.getName()));
        titleGroup.add(Box.createVerticalStrut(5));

        JLabel subtitle = Ui.smallLabel(
            "Select the candies you want to collect."
        );
        titleGroup.add(subtitle);

        JLabel factoryIcon = new JLabel("🏭");
        factoryIcon.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 42
        ));

        header.add(titleGroup, BorderLayout.CENTER);
        header.add(factoryIcon, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 12));
        center.setOpaque(false);

        JPanel inventoryCard = Ui.accentCard();
        inventoryCard.setLayout(new BorderLayout());

        JLabel inventoryStatus = Ui.label("");
        inventoryStatus.setFont(new Font(
            "Segoe UI", Font.BOLD, 13
        ));

        inventoryCard.add(
            inventoryStatus,
            BorderLayout.CENTER
        );

        center.add(inventoryCard, BorderLayout.NORTH);

        JPanel grid = new JPanel(
            new GridLayout(rows, columns, 10, 10)
        );
        grid.setBackground(Ui.CREAM);

        JLabel status = Ui.smallLabel(
            "Select one or more available candies."
        );

        JButton collectButton =
            Ui.button("COLLECT SELECTED  →");

        JButton closeButton =
            Ui.secondaryButton("CLOSE");

        Runnable updateStatus = () -> {
            int inventoryCount =
                c.getInventoryController().getCandyCount();

            int capacity =
                c.getInventoryController().getCapacity();

            int selectedCount = 0;

            for (int r = 0; r < rows; r++) {
                for (int col = 0; col < columns; col++) {
                    if (selected[r][col]) selectedCount++;
                }
            }

            inventoryStatus.setText(
                "INVENTORY   " + inventoryCount
                + " / " + capacity
                + "       •       "
                + selectedCount + " SELECTED"
            );

            collectButton.setEnabled(selectedCount > 0);
        };

        for (int r = 0; r < rows; r++) {
            for (int col = 0; col < columns; col++) {

                final int rr = r;
                final int cc = col;

                Candy candy = candies[r][col];

                JButton button = new JButton();

                button.setLayout(new BorderLayout(3, 5));
                button.setBackground(Color.WHITE);
                button.setOpaque(true);
                button.setFocusPainted(false);
                button.setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
                ));

                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Ui.BORDER),
                    new EmptyBorder(9, 5, 9, 5)
                ));

                button.setPreferredSize(
                    new Dimension(135, 95)
                );

                candyButtons[r][col] = button;

                if (candy == null) {
                    button.setText("EMPTY");
                    button.setFont(Ui.SMALL);
                    button.setForeground(Ui.MUTED);
                    button.setBackground(
                        new Color(242, 237, 241)
                    );
                    button.setEnabled(false);

                } else {
                    JLabel icon = new JLabel(
                        "🍬", SwingConstants.CENTER
                    );
                    icon.setFont(new Font(
                        "Segoe UI Emoji", Font.PLAIN, 25
                    ));

                    JLabel type = new JLabel(
                        candy.getCandyType(),
                        SwingConstants.CENTER
                    );
                    type.setFont(new Font(
                        "Segoe UI", Font.BOLD, 12
                    ));
                    type.setForeground(Ui.DARK);

                    JLabel flavor = new JLabel(
                        candy.getCandyFlavor(),
                        SwingConstants.CENTER
                    );
                    flavor.setFont(new Font(
                        "Segoe UI", Font.PLAIN, 10
                    ));
                    flavor.setForeground(Ui.MUTED);

                    JPanel details = new JPanel();
                    details.setOpaque(false);
                    details.setLayout(new BoxLayout(
                        details, BoxLayout.Y_AXIS
                    ));

                    details.add(icon);
                    details.add(type);
                    details.add(flavor);

                    button.add(details, BorderLayout.CENTER);

                    button.addActionListener(e -> {
                        selected[rr][cc] =
                            !selected[rr][cc];

                        button.setBackground(
                            selected[rr][cc]
                                ? Ui.LAVENDER
                                : Color.WHITE
                        );

                        button.setBorder(
                            BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                    selected[rr][cc]
                                        ? Ui.BERRY
                                        : Ui.BORDER,
                                    selected[rr][cc] ? 2 : 1
                                ),
                                new EmptyBorder(9, 5, 9, 5)
                            )
                        );

                        updateStatus.run();
                    });
                }

                grid.add(button);
            }
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        center.add(scroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(8, 12));
        footer.setOpaque(false);

        footer.add(status, BorderLayout.NORTH);

        JPanel actions = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 9, 0)
        );
        actions.setOpaque(false);

        actions.add(collectButton);
        actions.add(closeButton);

        footer.add(actions, BorderLayout.SOUTH);

        root.add(footer, BorderLayout.SOUTH);

        collectButton.addActionListener(e -> {
            ArrayList<int[]> positions = new ArrayList<>();

            for (int r = 0; r < rows; r++) {
                for (int col = 0; col < columns; col++) {
                    if (selected[r][col]) {
                        positions.add(new int[]{r, col});
                    }
                }
            }

            int[][] selectedPositions =
                positions.toArray(new int[0][]);

            int collected =
                c.getFactoryController().handleMultipleCandyPickup(
                    factory,
                    selectedPositions
                );

            Candy[][] updatedGrid =
                c.getFactoryController().getProductionGrid(factory);

            for (int r = 0; r < rows; r++) {
                for (int col = 0; col < columns; col++) {

                    selected[r][col] = false;

                    Candy updated = updatedGrid[r][col];
                    JButton button = candyButtons[r][col];

                    button.removeAll();

                    if (updated == null) {
                        button.setText("EMPTY");
                        button.setFont(Ui.SMALL);
                        button.setForeground(Ui.MUTED);
                        button.setBackground(
                            new Color(242, 237, 241)
                        );
                        button.setEnabled(false);

                    } else {
                        JLabel icon = new JLabel(
                            "🍬", SwingConstants.CENTER
                        );
                        icon.setFont(new Font(
                            "Segoe UI Emoji", Font.PLAIN, 25
                        ));

                        JLabel type = new JLabel(
                            updated.getCandyType(),
                            SwingConstants.CENTER
                        );
                        type.setFont(new Font(
                            "Segoe UI", Font.BOLD, 12
                        ));

                        JLabel flavor = new JLabel(
                            updated.getCandyFlavor(),
                            SwingConstants.CENTER
                        );
                        flavor.setFont(Ui.SMALL);
                        flavor.setForeground(Ui.MUTED);

                        JPanel details = new JPanel();
                        details.setOpaque(false);
                        details.setLayout(new BoxLayout(
                            details, BoxLayout.Y_AXIS
                        ));

                        details.add(icon);
                        details.add(type);
                        details.add(flavor);

                        button.setText(null);
                        button.add(details, BorderLayout.CENTER);
                        button.setEnabled(true);
                        button.setBackground(Color.WHITE);
                    }

                    button.setBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Ui.BORDER),
                            new EmptyBorder(9, 5, 9, 5)
                        )
                    );
                }
            }

            status.setText(
                collected + " candy item(s) collected!"
            );

            updateStatus.run();

            grid.revalidate();
            grid.repaint();

            Ui.message(
                dialog,
                collected > 0
                    ? collected + " candy item(s) added to your inventory!"
                    : "No candies were collected. Check your inventory capacity."
            );
        });

        closeButton.addActionListener(e -> dialog.dispose());

        updateStatus.run();

        dialog.setContentPane(root);
        dialog.setSize(850, 650);
        dialog.setMinimumSize(new Dimension(650, 500));
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
