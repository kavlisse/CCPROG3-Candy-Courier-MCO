
package view;

import java.awt.*;
import javax.swing.*;

public class PlayerSetupPanel extends JPanel {

    public PlayerSetupPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Ui.BACKGROUND);

        JPanel card = Ui.darkCard();
        card.setLayout(new BoxLayout(
            card, BoxLayout.Y_AXIS
        ));
        card.setPreferredSize(new Dimension(480, 440));

        JLabel avatar = new JLabel("🧑‍🍬");
        avatar.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 48
        ));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel eyebrow = Ui.eyebrow(
            "CREATE YOUR CHARACTER"
        );
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        eyebrow.setForeground(Ui.PINK);

        JLabel title = new JLabel("Meet your courier");
        title.setFont(new Font(
            "Segoe UI", Font.BOLD, 27
        ));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel prompt = new JLabel(
            "Choose a name for your adventure."
        );
        prompt.setFont(Ui.BODY);
        prompt.setForeground(new Color(205, 190, 210));
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField name = new JTextField();
        Ui.styleTextField(name);
        name.setMaximumSize(new Dimension(350, 48));
        name.setPreferredSize(new Dimension(350, 48));

        JButton begin = Ui.button("LET'S PLAY  →");
        JButton back = Ui.darkButton("BACK TO MENU");

        for (JButton button :
                new JButton[]{begin, back}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(350, 46));
            button.setPreferredSize(new Dimension(350, 46));
        }

        begin.addActionListener(e -> {
            String n = name.getText().trim();

            if (n.isEmpty()) {
                Ui.message(
                    this,
                    "Please enter your courier name."
                );
                name.requestFocusInWindow();
                return;
            }

            frame.startGame(n);
        });

        name.addActionListener(e -> begin.doClick());
        back.addActionListener(e -> frame.showMenu());

        card.add(Box.createVerticalGlue());
        card.add(avatar);
        card.add(Box.createVerticalStrut(17));
        card.add(eyebrow);
        card.add(Box.createVerticalStrut(7));
        card.add(title);
        card.add(Box.createVerticalStrut(9));
        card.add(prompt);
        card.add(Box.createVerticalStrut(25));
        card.add(name);
        card.add(Box.createVerticalStrut(22));
        card.add(begin);
        card.add(Box.createVerticalStrut(10));
        card.add(back);
        card.add(Box.createVerticalGlue());

        add(card);
    }
}
