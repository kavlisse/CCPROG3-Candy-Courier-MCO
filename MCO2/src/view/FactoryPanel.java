
package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import controller.GameController;
import model.Factory;
import model.Candy;

public class FactoryPanel {

    private FactoryPanel() {
    }

    public static void showDialog(
        Component parent,
        GameController c,
        Factory factory
    ) {

        Candy[][] candies =
            c.getFactoryController().getProductionGrid(factory);

        if (candies == null || candies.length == 0) {
            JOptionPane.showMessageDialog(
                parent,
                "This factory has no production grid."
            );
            return;
        }

        int rows = candies.length;
        int columns = candies[0].length;

        // Track selected candy positions.
        boolean[][] selected = new boolean[rows][columns];

        JButton[][] candyButtons =
            new JButton[rows][columns];

        JPanel grid = new JPanel(
            new GridLayout(rows, columns, 5, 5)
        );

        grid.setBackground(Ui.CREAM);

        JLabel status = Ui.label("Select the candies you want to collect.");

        JLabel inventoryStatus = Ui.label("");

        JButton collectButton =
            Ui.button("COLLECT SELECTED");

        JButton closeButton =
            Ui.secondaryButton("CLOSE");

        // Update inventory information.
        Runnable updateStatus = () -> {

            int count =
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
                "Inventory: " + count + " / " + capacity +
                " candies     |     Selected: " + selectedCount
            );

            collectButton.setEnabled(selectedCount > 0);
        };

        // Create the factory grid.
        for (int r = 0; r < rows; r++) {

            for (int col = 0; col < columns; col++) {

                final int rr = r;
                final int cc = col;

                Candy candy = candies[r][col];

                JButton button = new JButton();

                button.setFont(
                    new Font("Trebuchet MS", Font.BOLD, 12)
                );

                button.setFocusPainted(false);
                button.setOpaque(true);

                button.setPreferredSize(
                    new Dimension(105, 65)
                );

                candyButtons[r][col] = button;

                if (candy == null) {

                    button.setText("EMPTY");
                    button.setBackground(Ui.CREAM);
                    button.setForeground(Ui.MUTED);
                    button.setEnabled(false);

                } else {

                    button.setText(
                        "<html><center>" +
                        candy.getCandyType() +
                        "<br>" +
                        candy.getCandyFlavor() +
                        "</center></html>"
                    );

                    button.setBackground(Ui.PALE_PINK);
                    button.setForeground(Ui.DARK);

                    button.setBorder(
                        BorderFactory.createLineBorder(
                            Ui.BORDER, 1
                        )
                    );

                    button.addActionListener(e -> {

                        selected[rr][cc] = !selected[rr][cc];

                        button.setBackground(
                            selected[rr][cc]
                                ? Ui.PINK
                                : Ui.PALE_PINK
                        );

                        button.setBorder(
                            BorderFactory.createLineBorder(
                                selected[rr][cc]
                                    ? Ui.DARK
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

        // Collect selected candies.
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
                c.getFactoryController()
                    .handleMultipleCandyPickup(
                        factory,
                        selectedPositions
                    );

            // Refresh the actual factory grid.
            Candy[][] updatedGrid =
                c.getFactoryController().getProductionGrid(factory);

            for (int r = 0; r < rows; r++) {

                for (int col = 0; col < columns; col++) {

                    if (updatedGrid[r][col] == null) {

                        candyButtons[r][col].setText("EMPTY");
                        candyButtons[r][col].setEnabled(false);
                        candyButtons[r][col].setBackground(Ui.CREAM);

                    } else {

                        candyButtons[r][col].setBackground(
                            Ui.PALE_PINK
                        );
                    }

                    selected[r][col] = false;

                    candyButtons[r][col].setBorder(
                        BorderFactory.createLineBorder(
                            Ui.BORDER, 1
                        )
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
                JOptionPane.showMessageDialog(
                    parent,
                    collected + " candy item(s) added to your inventory!"
                );
            } else {
                JOptionPane.showMessageDialog(
                    parent,
                    "No candies were collected. Check your inventory capacity."
                );
            }
        });

        // Layout
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(Ui.CREAM);
        panel.setBorder(
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        JLabel title = Ui.heading(factory.getName());

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBackground(Ui.CREAM);
        top.add(title, BorderLayout.NORTH);
        top.add(inventoryStatus, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setPreferredSize(
            new Dimension(650, 440)
        );

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(Ui.CREAM);

        bottom.add(status, BorderLayout.NORTH);

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        buttons.setBackground(Ui.CREAM);
        buttons.add(collectButton);
        buttons.add(closeButton);

        bottom.add(buttons, BorderLayout.SOUTH);

        panel.add(bottom, BorderLayout.SOUTH);

        updateStatus.run();

        JDialog dialog = new JDialog(
            SwingUtilities.getWindowAncestor(parent),
            factory.getName() + " — Candy Production",
            Dialog.ModalityType.APPLICATION_MODAL
        );

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(700, 550));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
