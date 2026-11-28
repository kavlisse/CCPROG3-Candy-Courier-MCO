
package view;

import java.awt.*;
import javax.swing.*;

import model.Player;

public class PlayerInfoPanel extends JPanel {

    private final JLabel info = new JLabel();

    public PlayerInfoPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(42, 24, 48));
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        info.setFont(new Font("Segoe UI", Font.BOLD, 15));
        info.setForeground(Color.WHITE);

        add(info, BorderLayout.CENTER);
    }

    public void refresh(Player p, int day) {
        if (p == null) {
            info.setText("CANDY COURIER");
            return;
        }

        info.setText(
            "  " + p.getName() +
            "     |     RANK " + p.getStarRank() +
            "     |     " + p.getTotalXP() + " XP" +
            "     |     " + p.getInventory().getCandyCount() +
            "/" + p.getInventory().getCapacity() +
            "     |     DAY " + day +
            "     |     STEPS " + p.getTotalSteps()
        );
    }
}
