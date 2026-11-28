package view;

import java.awt.*;
import javax.swing.*;

public class PlayerSetupPanel extends JPanel {
    public PlayerSetupPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Ui.CREAM);

        JPanel card = Ui.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(450, 330));

        JLabel icon = new JLabel("🧑‍🍬");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = Ui.heading("Meet your courier");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel prompt = Ui.label("Choose a name for your adventure.");
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField name = new JTextField();
        name.setFont(Ui.BODY);
        name.setMaximumSize(new Dimension(340, 44));
        name.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Ui.BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JButton begin = Ui.button("LET'S PLAY  →");
        JButton back = Ui.secondaryButton("←  Back to menu");
        begin.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);

        begin.addActionListener(e -> {
            String n = name.getText().trim();
            if (n.isEmpty()) {
                Ui.message(this, "Please enter your courier name.");
                return;
            }
            frame.startGame(n);
        });

        name.addActionListener(e -> begin.doClick());
        back.addActionListener(e -> frame.showMenu());

        card.add(Box.createVerticalGlue());
        card.add(icon);
        card.add(Box.createVerticalStrut(12));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(prompt);
        card.add(Box.createVerticalStrut(18));
        card.add(name);
        card.add(Box.createVerticalStrut(20));
        card.add(begin);
        card.add(Box.createVerticalStrut(9));
        card.add(back);
        card.add(Box.createVerticalGlue());

        add(card);
    }
}