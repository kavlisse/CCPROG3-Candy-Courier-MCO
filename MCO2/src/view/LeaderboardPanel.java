package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.PlayerRecord;

public class LeaderboardPanel extends JPanel {

    private final MainFrame frame;
    private final GameController c;

    private final DefaultListModel<String> model =
        new DefaultListModel<>();

    public LeaderboardPanel(
        MainFrame frame,
        GameController c
    ) {

        this.frame = frame;
        this.c = c;

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Leaderboard"),
            BorderLayout.NORTH
        );

        add(
            new JScrollPane(new JList<>(model)),
            BorderLayout.CENTER
        );

        JButton back = Ui.button("Back to menu");

        back.addActionListener(
            e -> frame.showMenu()
        );

        add(back, BorderLayout.SOUTH);
    }

    public void refresh() {

        model.clear();

        int rank = 1;

        for (
            PlayerRecord r :
            c.getLeaderboardController().getLeaderboard()
        ) {

            model.addElement(
                rank++ +
                ".  " +
                r.getPlayerName() +
                "   ★ " +
                r.getStarRank() +
                "   " +
                r.getDaysSpent() +
                " days   " +
                r.getTotalSteps() +
                " steps"
            );
        }

        if (model.isEmpty()) {

            model.addElement(
                "No completed runs yet."
            );
        }
    }
}