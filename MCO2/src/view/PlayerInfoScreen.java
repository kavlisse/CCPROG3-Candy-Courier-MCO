package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.Player;

class PlayerInfoScreen extends JPanel {

    PlayerInfoScreen(GameController c) {

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Courier profile"),
            BorderLayout.NORTH
        );

        Player p = c.getGame().getPlayer();

        JTextArea a = new JTextArea(
            "Name: " + p.getName() +
            "\nStar rank: " + p.getStarRank() +
            "\nTotal XP: " + p.getTotalXP() +
            "\nXP to next rank: " +
            p.getXPNeededForNextRank() +
            "\nTotal steps: " + p.getTotalSteps() +
            "\nWorld size: " +
            c.getGame().getWorld().getGridSize() +
            " × " +
            c.getGame().getWorld().getGridSize() +
            "\nInventory capacity: " +
            p.getInventory().getCapacity()
        );

        a.setEditable(false);

        a.setFont(
            new Font("SansSerif", Font.PLAIN, 17)
        );

        a.setBackground(Color.WHITE);

        a.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        add(a, BorderLayout.CENTER);
    }
}