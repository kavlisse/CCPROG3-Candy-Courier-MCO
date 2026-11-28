package view;

import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel {

    public GameOverPanel(
        String summary,
        Runnable returnAction
    ) {

        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Run complete!"),
            BorderLayout.NORTH
        );

        JLabel details = Ui.label(
            "<html>" +
            summary.replace("\n", "<br>") +
            "</html>"
        );

        details.setFont(
            new Font("SansSerif", Font.PLAIN, 17)
        );

        add(details, BorderLayout.CENTER);

        JButton back = Ui.button("Return to menu");

        back.addActionListener(
            e -> returnAction.run()
        );

        add(back, BorderLayout.SOUTH);
    }
}