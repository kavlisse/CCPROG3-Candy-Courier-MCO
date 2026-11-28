
package view;

import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Ui.BACKGROUND);

        JPanel card = Ui.darkCard();
        card.setLayout(new BoxLayout(
            card, BoxLayout.Y_AXIS
        ));
        card.setPreferredSize(new Dimension(570, 590));

        JLabel decoration = new JLabel(
            "🍬     ✦     🍓     ✦     🍬"
        );
        decoration.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 31
        ));
        decoration.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel eyebrow = Ui.eyebrow(
            "A LITTLE WORLD OF SWEET ADVENTURES"
        );
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        eyebrow.setForeground(Ui.PINK);

        JLabel title = new JLabel("CANDY COURIER");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font(
            "Segoe UI", Font.BOLD, 37
        ));

        JLabel subtitle = new JLabel(
            "Collect sweets. Complete orders. Explore."
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(202, 185, 207));
        subtitle.setFont(Ui.BODY);

        JLabel tagline = new JLabel(
            "Your next sweet adventure starts here."
        );
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setForeground(Ui.PINK);
        tagline.setFont(Ui.SMALL);

        JButton start = Ui.button("START ADVENTURE  →");
        JButton scores = Ui.secondaryButton("LEADERBOARD");
        JButton exit = Ui.darkButton("EXIT GAME");

        for (JButton button :
                new JButton[]{start, scores, exit}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(340, 48));
            button.setPreferredSize(new Dimension(340, 48));
        }

        card.add(Box.createVerticalGlue());
        card.add(decoration);
        card.add(Box.createVerticalStrut(25));
        card.add(eyebrow);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(8));
        card.add(tagline);
        card.add(Box.createVerticalStrut(42));
        card.add(start);
        card.add(Box.createVerticalStrut(13));
        card.add(scores);
        card.add(Box.createVerticalStrut(13));
        card.add(exit);
        card.add(Box.createVerticalGlue());

        start.addActionListener(e -> frame.showSetup());
        scores.addActionListener(e -> frame.showLeaderboard());

        exit.addActionListener(e -> {
            boolean confirmed = Ui.confirm(
                this,
                "EXIT GAME",
                "Are you sure you want to leave Candy Courier?",
                "EXIT",
                "STAY"
            );

            if (confirmed) {
                System.exit(0);
            }
        });

        add(card);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(new Color(190, 91, 143, 30));
        g2.fillOval(-100, -100, 340, 340);

        g2.setColor(new Color(183, 161, 230, 25));
        g2.fillOval(
            getWidth() - 220,
            getHeight() - 230,
            380, 380
        );

        g2.dispose();
    }
}
