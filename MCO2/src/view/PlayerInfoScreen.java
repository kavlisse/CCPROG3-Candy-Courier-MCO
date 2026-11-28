
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

        setLayout(new BorderLayout(18, 18));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(
            header, BoxLayout.Y_AXIS
        ));

        header.add(Ui.eyebrow("YOUR CHARACTER"));
        header.add(Box.createVerticalStrut(5));
        header.add(Ui.heading("Courier Profile"));

        add(header, BorderLayout.NORTH);

        details.setBackground(Ui.CREAM);
        details.setLayout(new GridLayout(0, 2, 14, 14));

        add(details, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        details.removeAll();

        Player p = c.getGame().getPlayer();

        if (p == null) {
            details.add(Ui.heading("No courier yet"));
        } else {
            JPanel identity = Ui.darkCard();
            identity.setLayout(new BoxLayout(
                identity, BoxLayout.Y_AXIS
            ));

            JLabel avatar = new JLabel("🧑‍🍬");
            avatar.setFont(new Font(
                "Segoe UI Emoji", Font.PLAIN, 42
            ));

            JLabel name = new JLabel(p.getName());
            name.setFont(new Font(
                "Segoe UI", Font.BOLD, 24
            ));
            name.setForeground(Color.WHITE);

            JLabel rank = new JLabel(
                "STAR RANK  " + p.getStarRank()
            );
            rank.setFont(new Font(
                "Segoe UI", Font.BOLD, 11
            ));
            rank.setForeground(Ui.PINK);

            identity.add(avatar);
            identity.add(Box.createVerticalStrut(10));
            identity.add(name);
            identity.add(Box.createVerticalStrut(5));
            identity.add(rank);

            details.add(identity);

            addStat("✦", "TOTAL EXPERIENCE",
                p.getTotalXP() + " XP");

            addStat("★", "STAR RANK",
                String.valueOf(p.getStarRank()));

            addStat("⬆", "XP TO NEXT RANK",
                String.valueOf(p.getXPNeededForNextRank()));

            addStat("👣", "TOTAL STEPS",
                String.valueOf(p.getTotalSteps()));

            addStat("🎒", "INVENTORY",
                p.getInventory().getCandyCount()
                + " / " + p.getInventory().getCapacity());

            addStat("🗺", "WORLD SIZE",
                c.getGame().getWorld().getGridSize()
                + " × "
                + c.getGame().getWorld().getGridSize());

            addStat("📍", "CURRENT LOCATION",
                "Row " + (p.getRow() + 1)
                + ", Column " + (p.getColumn() + 1));

            addStat("↗", "DIAGONAL MOVEMENT",
                p.canMoveDiagonally()
                ? "Unlocked"
                : "Locked");
        }

        details.revalidate();
        details.repaint();
    }

    private void addStat(
        String icon,
        String title,
        String value
    ) {
        JPanel card = Ui.card();
        card.setLayout(new BorderLayout(10, 8));

        JLabel symbol = new JLabel(icon);
        symbol.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 25
        ));
        symbol.setForeground(Ui.BERRY);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(
            text, BoxLayout.Y_AXIS
        ));

        JLabel heading = Ui.eyebrow(title);

        JLabel number = new JLabel(value);
        number.setFont(new Font(
            "Segoe UI", Font.BOLD, 19
        ));
        number.setForeground(Ui.DARK);

        text.add(heading);
        text.add(Box.createVerticalStrut(7));
        text.add(number);

        card.add(symbol, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        details.add(card);
    }
}
