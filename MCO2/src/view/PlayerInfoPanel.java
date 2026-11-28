
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Player;

public class PlayerInfoPanel extends JPanel {

    private final JLabel nameLabel = new JLabel("COURIER");
    private final JLabel rankLabel = new JLabel("RANK 0");
    private final JLabel xpLabel = new JLabel("0 XP");
    private final JLabel nextRankLabel = new JLabel("0 XP NEEDED");
    private final JLabel inventoryLabel = new JLabel("0 / 0");
    private final JLabel dayLabel = new JLabel("DAY 1");
    private final JLabel stepsLabel = new JLabel("0 STEPS");

    public PlayerInfoPanel() {
        setLayout(new BorderLayout(20, 0));
        setBackground(Ui.BACKGROUND);
        setBorder(new EmptyBorder(13, 22, 13, 22));

        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(
            identity, BoxLayout.Y_AXIS
        ));

        JLabel eyebrow = Ui.eyebrow("YOUR ADVENTURE");
        eyebrow.setForeground(Ui.PINK);

        nameLabel.setFont(new Font(
            "Segoe UI", Font.BOLD, 16
        ));
        nameLabel.setForeground(Color.WHITE);

        identity.add(eyebrow);
        identity.add(Box.createVerticalStrut(3));
        identity.add(nameLabel);

        JPanel stats = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 20, 0)
        );
        stats.setOpaque(false);

        stats.add(makeStat("RANK", rankLabel));
        stats.add(makeStat("EXPERIENCE", xpLabel));
        stats.add(makeStat("NEXT RANK", nextRankLabel));
        stats.add(makeStat("INVENTORY", inventoryLabel));
        stats.add(makeStat("DAY", dayLabel));
        stats.add(makeStat("STEPS", stepsLabel));

        add(identity, BorderLayout.WEST);
        add(stats, BorderLayout.EAST);
    }

    private JPanel makeStat(String title, JLabel value) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(
            panel, BoxLayout.Y_AXIS
        ));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font(
            "Segoe UI", Font.BOLD, 9
        ));
        heading.setForeground(Ui.MUTED);

        value.setFont(new Font(
            "Segoe UI", Font.BOLD, 12
        ));
        value.setForeground(Color.WHITE);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(5));
        panel.add(value);

        return panel;
    }

    public void refresh(Player p, int day) {
        if (p == null) {
            nameLabel.setText("COURIER");
            rankLabel.setText("RANK 0");
            xpLabel.setText("0 XP");
            nextRankLabel.setText("0 XP NEEDED");
            inventoryLabel.setText("0 / 0");
            dayLabel.setText("DAY " + day);
            stepsLabel.setText("0 STEPS");
            return;
        }

        nameLabel.setText(p.getName());
        rankLabel.setText("RANK " + p.getStarRank());
        xpLabel.setText(p.getTotalXP() + " XP");

        nextRankLabel.setText(
            p.getXPNeededForNextRank() + " XP"
        );

        inventoryLabel.setText(
            p.getInventory().getCandyCount()
            + " / "
            + p.getInventory().getCapacity()
        );

        dayLabel.setText("DAY " + day);
        stepsLabel.setText(p.getTotalSteps() + " STEPS");
    }
}
