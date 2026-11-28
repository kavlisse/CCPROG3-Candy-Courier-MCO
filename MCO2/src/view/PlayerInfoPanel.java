package view;

import java.awt.*;
import javax.swing.*;

import model.Player;

public class PlayerInfoPanel extends JPanel {

    private final JLabel info = new JLabel();

    public PlayerInfoPanel() {

        setLayout(new BorderLayout());
        setBackground(Ui.PALE);

        setBorder(
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );

        info.setFont(
            new Font("SansSerif", Font.BOLD, 14)
        );

        info.setForeground(Ui.DARK);

        add(info);
    }

    public void refresh(Player p, int day) {

        if (p == null) {
            info.setText("No active player");
        } else {

            info.setText(
                "  " + p.getName() +
                "   ★ " + p.getStarRank() +
                "   XP " + p.getTotalXP() +
                "   Next rank: " +
                p.getXPNeededForNextRank() +
                " XP   Day " + day +
                "   Steps " + p.getTotalSteps()
            );
        }
    }
}