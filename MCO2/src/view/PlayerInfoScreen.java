package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.GameController;
import model.Player;

class PlayerInfoScreen extends JPanel {
    private final GameController c;
    private final JPanel details = new JPanel();

    PlayerInfoScreen(GameController c) {
        this.c = c;
        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(18, 20, 18, 20));

        add(Ui.heading("Courier Profile"), BorderLayout.NORTH);

        details.setBackground(Ui.WHITE);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(new EmptyBorder(22, 26, 22, 26));
        add(details, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        details.removeAll();
        Player p = c.getGame().getPlayer();

        if (p == null) {
            details.add(Ui.heading("No courier yet"));
        } else {
            addLine("🧑  Courier", p.getName());
            addLine("★  Star rank", String.valueOf(p.getStarRank()));
            addLine("✦  Experience", p.getTotalXP() + " XP");
            addLine("⬆  Next rank", p.getXPNeededForNextRank() + " XP");
            addLine("👣  Steps taken", String.valueOf(p.getTotalSteps()));
            addLine("🎒  Inventory", p.getInventory().getCandyCount()
                + " / " + p.getInventory().getCapacity());
            addLine("🗺  World", c.getGame().getWorld().getGridSize()
                + " × " + c.getGame().getWorld().getGridSize());
            addLine("📍  Location", "Row " + (p.getRow() + 1)
                + ", Column " + (p.getColumn() + 1));
            addLine("↗  Diagonal movement",
                p.canMoveDiagonally() ? "Unlocked" : "Locked");
        }

        details.revalidate();
        details.repaint();
    }

    private void addLine(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 8));
        row.setBackground(Ui.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Ui.BORDER),
            new EmptyBorder(12, 4, 12, 4)));

        JLabel l = Ui.label(label);
        JLabel v = Ui.label(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 14));
        v.setForeground(Ui.DARK);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        details.add(row);
    }
}