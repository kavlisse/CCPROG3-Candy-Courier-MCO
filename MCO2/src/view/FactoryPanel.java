
package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Factory;
import model.Candy;

public class FactoryPanel {

    private FactoryPanel() {
    }

    public static void showDialog(
            Component parent,
            GameController c,
            Factory factory) {

        Candy[][] candies =
            c.getFactoryController().getProductionGrid(factory);

        if (candies == null || candies.length == 0) {
            Ui.message(parent, "This factory has no production grid.");
            return;
        }

        int rows = candies.length;
        int columns = candies[0].length;

        boolean[][] selected = new boolean[rows][columns];
        JButton[][] candyButtons = new JButton[rows][columns];

        JDialog dialog = new JDialog(
            SwingUtilities.getWindowAncestor(parent),
            factory.getName() + " — Production",
            Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(Ui.CREAM);
        panel.setBorder(new EmptyBorder(22, 24, 20, 24));

        // HEADER
        JLabel title = Ui.heading(factory.getName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitle = Ui.label(
            "Select the candies you want to collect."
        );

        JLabel inventoryStatus = Ui.label("");
        inventoryStatus.setFont(Ui.BODY);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        inventoryStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(12));
        header.add(inventoryStatus);

        panel.add(header, BorderLayout.NORTH);

        // CANDY GRID
        JPanel grid = new JPanel(
            new GridLayout(rows, columns, 8, 8)
        );
        grid.setBackground(Ui.CREAM);

        JLabel status = Ui.smallLabel(
            "Choose the candies you want to collect."
        );

        JButton collectButton = Ui.button("COLLECT SELECTED");
        JButton closeButton = Ui.secondaryButton("CLOSE");

        Runnable updateStatus = () -> {
            int inventoryCount =
                c.getInventoryController().getCandyCount();

            int capacity =
                c.getInventoryController().getCapacity();

            int selectedCount = 0;

            for (int r = 0; r < rows; r++) {
                for (int col = 0; col < columns; col++) {
                    if (selected[r][col]) {
                        selectedCount++;
                    }
                }
            }

            inventoryStatus.setText(
                "Inventory: " + inventoryCount + " / " + capacity
                + "     •     Selected: " + selectedCount
            );

            collectButton.setEnabled(selectedCount > 0);
        };

        for (int r = 0; r < rows; r++) {
            for (int col = 0; col < columns; col++) {

                final int rr = r;
                final int cc = col;

                Candy candy = candies[r][col];

                JButton button = new JButton();

                button.setFont(Ui.BUTTON);
                button.setForeground(Ui.DARK);
                button.setBackground(Ui.BLUSH);
                button.setOpaque(true);
                button.setFocusPainted(false);
                button.setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                );

                button.setPreferredSize(new Dimension(120, 82));

                candyButtons[r][col] = button;

                if (candy == null) {
                    button.setText("EMPTY");
                    button.setForeground(Ui.MUTED);
                    button.setBackground(Ui.CREAM);
                    button.setEnabled(false);
                    button.setBorder(
                        BorderFactory.createLineBorder(Ui.BORDER)
                    );
                } else {
                    button.setText(
                        "<html><center>"
                        + candy.getCandyType()
                        + "<br>"
                        + candy.getCandyFlavor()
                        + "</center></html>"
                    );

                    button.setBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                Ui.BORDER, 1
                            ),
                            new EmptyBorder(8, 6, 8, 6)
                        )
                    );

                    button.addActionListener(e -> {
                        selected[rr][cc] = !selected[rr][cc];

                        button.setBackground(
                            selected[rr][cc]
                                ? Ui.PINK
                                : Ui.BLUSH
                        );

                        button.setBorder(
                            BorderFactory.createLineBorder(
                                selected[rr][cc]
                                    ? Ui.BERRY
                                    : Ui.BORDER,
                                selected[rr][cc] ? 3 : 1
                            )
                        );

                        updateStatus.run();
                    });
                }

                grid.add(button);
            }
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(
            BorderFactory.createLineBorder(Ui.BORDER)
        );
        scrollPane.getViewport().setBackground(Ui.CREAM);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        panel.add(scrollPane, BorderLayout.CENTER);

        // FOOTER
        JPanel footer = new JPanel(new BorderLayout(8, 12));
        footer.setOpaque(false);

        footer.add(status, BorderLayout.NORTH);

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        buttons.setOpaque(false);

        buttons.add(collectButton);
        buttons.add(closeButton);

        footer.add(buttons, BorderLayout.SOUTH);

        panel.add(footer, BorderLayout.SOUTH);

        // COLLECT SELECTED CANDIES
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

                    Candy updatedCandy = updatedGrid[r][col];
                    JButton button = candyButtons[r][col];

                    if (updatedCandy == null) {
                        button.setText("EMPTY");
                        button.setEnabled(false);
                        button.setBackground(Ui.CREAM);
                        button.setForeground(Ui.MUTED);
                    } else {
                        button.setText(
                            "<html><center>"
                            + updatedCandy.getCandyType()
                            + "<br>"
                            + updatedCandy.getCandyFlavor()
                            + "</center></html>"
                        );

                        button.setEnabled(true);
                        button.setBackground(Ui.BLUSH);
                        button.setForeground(Ui.DARK);
                    }

                    button.setBorder(
                        BorderFactory.createLineBorder(Ui.BORDER, 1)
                    );
                }
            }

            status.setText(
                collected + " candy item(s) successfully collected!"
            );

            updateStatus.run();

            grid.revalidate();
            grid.repaint();

            if (collected > 0) {
                Ui.message(
                    dialog,
                    collected + " candy item(s) added to your inventory!"
                );
            } else {
                Ui.message(
                    dialog,
                    "No candies were collected. Check your inventory capacity."
                );
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        updateStatus.run();

        dialog.setContentPane(panel);
        dialog.setSize(850, 680);
        dialog.setMinimumSize(new Dimension(650, 520));
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
