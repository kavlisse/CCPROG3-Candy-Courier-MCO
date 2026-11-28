
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameOverPanel extends JPanel {

    public GameOverPanel(
        String summary,
        Runnable returnAction
    ) {
        setLayout(new GridBagLayout());
        setBackground(Ui.BACKGROUND);

        JPanel card = Ui.darkCard();
        card.setLayout(new BoxLayout(
            card, BoxLayout.Y_AXIS
        ));
        card.setPreferredSize(new Dimension(550, 440));

        JLabel icon = new JLabel("🏆");
        icon.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 58
        ));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel eyebrow = Ui.eyebrow(
            "ADVENTURE COMPLETED"
        );
        eyebrow.setForeground(Ui.PINK);
        eyebrow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Well done, Courier!");
        title.setFont(new Font(
            "Segoe UI", Font.BOLD, 29
        ));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(
            "<html><div style='text-align:center; width:390px;'>"
            + summary.replace("\n", "<br>")
            + "</div></html>"
        );

        description.setFont(Ui.BODY);
        description.setForeground(
            new Color(215, 198, 220)
        );
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton back = Ui.button("RETURN TO MENU  →");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(340, 48));

        back.addActionListener(e -> returnAction.run());

        card.add(Box.createVerticalGlue());
        card.add(icon);
        card.add(Box.createVerticalStrut(17));
        card.add(eyebrow);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(23));
        card.add(description);
        card.add(Box.createVerticalStrut(32));
        card.add(back);
        card.add(Box.createVerticalGlue());

        add(card);
    }
}
