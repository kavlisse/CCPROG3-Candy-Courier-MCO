package view;

import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Ui.CREAM);

        JPanel card = Ui.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(520, 470));

        JLabel emblem = new JLabel("🍬  ✦  🍓  ✦  🍬");
        emblem.setAlignmentX(Component.CENTER_ALIGNMENT);
        emblem.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JLabel eyebrow = new JLabel("A LITTLE WORLD OF SWEET ADVENTURES");
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        eyebrow.setForeground(Ui.BERRY);
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JLabel title = new JLabel("CANDY COURIER");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Ui.DARK);
        title.setFont(Ui.TITLE);

        JLabel subtitle = new JLabel("Collect sweets. Complete orders. Explore.");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(Ui.MUTED);
        subtitle.setFont(Ui.BODY);

        JButton start = Ui.button("▶   START ADVENTURE");
        JButton scores = Ui.secondaryButton("🏆   LEADERBOARD");
        JButton exit = Ui.secondaryButton("✕   EXIT GAME");

        for (JButton b : new JButton[]{start, scores, exit}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(310, 48));
            b.setPreferredSize(new Dimension(310, 48));
        }

        card.add(Box.createVerticalGlue());
        card.add(emblem);
        card.add(Box.createVerticalStrut(18));
        card.add(eyebrow);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));
        card.add(start);
        card.add(Box.createVerticalStrut(12));
        card.add(scores);
        card.add(Box.createVerticalStrut(12));
        card.add(exit);
        card.add(Box.createVerticalGlue());

        start.addActionListener(e -> frame.showSetup());
        scores.addActionListener(e -> frame.showLeaderboard());

        exit.addActionListener(e -> {
            if (Ui.confirm(this, "Exit Candy Courier?",
                    "Are you sure you want to leave the game?",
                    "Exit", "Stay")) {
                System.exit(0);
            }
        });

        add(card);
    }
}