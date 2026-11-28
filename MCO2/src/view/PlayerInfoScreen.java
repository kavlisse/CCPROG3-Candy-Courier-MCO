
package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.Player;

class PlayerInfoScreen extends JPanel {

    private final GameController controller;
    private final JTextArea info;

    PlayerInfoScreen(GameController c) {

        this.controller = c;

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Courier profile"),
            BorderLayout.NORTH
        );

        info = new JTextArea();
        info.setEditable(false);

        info.setFont(
            new Font("SansSerif", Font.PLAIN, 17)
        );

        info.setBackground(Color.WHITE);

        info.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        add(info, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {

        Player p = controller.getGame().getPlayer();

        if (p == null) {
            info.setText("No player information available yet.");
            return;
        }

        info.setText(
            "Name: " + p.getName() +
            "\nStar rank: " + p.getStarRank() +
            "\nTotal XP: " + p.getTotalXP() +
            "\nXP to next rank: " +
            p.getXPNeededForNextRank() +
            "\nTotal steps: " + p.getTotalSteps() +
            "\nWorld size: " +
            controller.getGame().getWorld().getGridSize() +
            " × " +
            controller.getGame().getWorld().getGridSize() +
            "\nInventory capacity: " +
            p.getInventory().getCapacity()
        );
    }
}
