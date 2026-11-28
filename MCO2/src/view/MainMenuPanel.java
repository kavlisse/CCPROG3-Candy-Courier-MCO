
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

        // Decorative title
        JLabel decoration = new JLabel("SWEET ADVENTURES AWAIT");
        decoration.setAlignmentX(Component.CENTER_ALIGNMENT);
        decoration.setFont(
            new Font("Trebuchet MS", Font.BOLD, 13)
        );
        decoration.setForeground(Ui.BERRY);

        // Game title
        JLabel title = new JLabel("CANDY COURIER");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(
            new Font("Trebuchet MS", Font.BOLD, 52)
        );
        title.setForeground(Ui.DARK);

        // Subtitle
        JLabel subtitle = new JLabel(
            "Collect sweets. Complete orders. Grow your little world."
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(
            new Font("Segoe UI", Font.PLAIN, 15)
        );
        subtitle.setForeground(Ui.INK);

        // Menu buttons
        JButton start = Ui.button("START NEW GAME");
        JButton scores = Ui.secondaryButton("LEADERBOARD");
        JButton exit = Ui.secondaryButton("EXIT GAME");

        Dimension buttonSize = new Dimension(290, 52);

        for (JButton b : new JButton[]{start, scores, exit}) {

            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(buttonSize);
            b.setPreferredSize(buttonSize);
            b.setMinimumSize(buttonSize);
        }

        content.add(decoration);
        content.add(Box.createVerticalStrut(12));
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(42));

        content.add(start);
        content.add(Box.createVerticalStrut(14));
        content.add(scores);
        content.add(Box.createVerticalStrut(14));
        content.add(exit);

        // Menu actions
        start.addActionListener(e -> frame.showSetup());

        scores.addActionListener(
            e -> frame.showLeaderboard()
        );

        exit.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to leave Candy Courier?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        add(content);
    }
}
