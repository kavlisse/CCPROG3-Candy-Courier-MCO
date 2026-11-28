package view;

import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel(String summary, Runnable returnAction) {
        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);
        setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));

        add(Ui.heading("🏁  Adventure complete!"), BorderLayout.NORTH);

        JLabel details = new JLabel("<html><div style='width:420px;'>"
            + summary.replace("\n", "<br>") + "</div></html>");
        details.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        details.setForeground(Ui.INK);
        add(details, BorderLayout.CENTER);

        JButton back = Ui.button("RETURN TO MENU  →");
        back.addActionListener(e -> returnAction.run());
        add(back, BorderLayout.SOUTH);
    }
}