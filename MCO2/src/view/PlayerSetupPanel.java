package view;

import java.awt.*;
import javax.swing.*;

public class PlayerSetupPanel extends JPanel {

    public PlayerSetupPanel(MainFrame frame) {

        setLayout(new GridBagLayout());
        setBackground(Ui.CREAM);

        JPanel card = Ui.card();
        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );
        card.setPreferredSize(new Dimension(430, 260));

        JLabel title = Ui.heading("Meet your courier");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel prompt = Ui.label(
            "What should we call you?"
        );
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField name = new JTextField();
        name.setMaximumSize(new Dimension(330, 42));
        name.setFont(
            new Font("SansSerif", Font.PLAIN, 16)
        );

        JButton begin = Ui.button("LET'S PLAY");
        begin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton back = new JButton("Back to menu");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);

        begin.addActionListener(e -> {
            String n = name.getText().trim();

            if (n.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter your name."
                );
                return;
            }

            frame.startGame(n);
        });

        back.addActionListener(e -> frame.showMenu());

        card.add(title);
        card.add(Box.createVerticalStrut(22));
        card.add(prompt);
        card.add(Box.createVerticalStrut(8));
        card.add(name);
        card.add(Box.createVerticalStrut(22));
        card.add(begin);
        card.add(Box.createVerticalStrut(8));
        card.add(back);

        add(card);
    }
}