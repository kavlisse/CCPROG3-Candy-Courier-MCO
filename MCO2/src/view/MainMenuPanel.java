package view;

import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(MainFrame frame) {

        setLayout(new GridBagLayout());
        setBackground(Ui.CREAM);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(
            new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        JLabel title = new JLabel("CANDY COURIER");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(
            new Font("SansSerif", Font.BOLD, 46)
        );
        title.setForeground(Ui.DARK);

        JLabel subtitle = new JLabel(
            "Collect sweets. Complete orders. Grow your little world."
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(Ui.INK);

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(42));

        JButton start = Ui.button("START NEW GAME");
        JButton scores = Ui.button("LEADERBOARD");
        JButton exit = Ui.button("EXIT GAME");

        for (JButton b : new JButton[]{start, scores, exit}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(260, 48));

            content.add(b);
            content.add(Box.createVerticalStrut(14));
        }

        start.addActionListener(e -> frame.showSetup());

        scores.addActionListener(
            e -> frame.showLeaderboard()
        );

        exit.addActionListener(e -> System.exit(0));

        add(content);
    }
}