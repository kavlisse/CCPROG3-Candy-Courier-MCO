package view;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;

import model.Game;
import model.Leaderboard;
import model.PlayerRecord;

import controller.GameController;
import controller.LeaderboardController;

import persistence.LeaderboardFileHandler;

/**
 * Main entry point of the Candy Courier application
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Initialize leaderboard components
            Leaderboard leaderboard = new Leaderboard();

            String filePath = "leaderboard.txt";

            LeaderboardFileHandler fileHandler =
                new LeaderboardFileHandler(filePath);

            LeaderboardController leaderboardController =
                new LeaderboardController(leaderboard, fileHandler);

            // Initialize the game and its controller
            Game game = new Game();

            GameController gameController =
                new GameController(game, leaderboardController);

            // Create the application window
            JFrame frame = new JFrame("Candy Courier");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 450);
            frame.setLocationRelativeTo(null);

            // Create the leaderboard display
            JLabel title = new JLabel(
                "CANDY COURIER LEADERBOARD",
                JLabel.CENTER
            );

            JTextArea display = new JTextArea();
            display.setEditable(false);

            // Display the loaded leaderboard records
            for (PlayerRecord record :
                    leaderboardController.getLeaderboard()) {

                display.append(
                    record.getPlayerName() + " | " +
                    record.getStarRank() + "-star | " +
                    record.getDaysSpent() + " days | " +
                    record.getTotalSteps() + " steps\n"
                );
            }

            // Add a button to reload the leaderboard
            JButton refreshButton = new JButton("Refresh Leaderboard");

            refreshButton.addActionListener(e -> {

                leaderboardController.loadLeaderboard();

                display.setText("");

                for (PlayerRecord record :
                        leaderboardController.getLeaderboard()) {

                    display.append(
                        record.getPlayerName() + " | " +
                        record.getStarRank() + "-star | " +
                        record.getDaysSpent() + " days | " +
                        record.getTotalSteps() + " steps\n"
                    );
                }
            });

            // Arrange the GUI components
            JPanel panel = new JPanel(new BorderLayout());

            panel.add(title, BorderLayout.NORTH);
            panel.add(new JScrollPane(display), BorderLayout.CENTER);
            panel.add(refreshButton, BorderLayout.SOUTH);

            frame.add(panel);

            frame.setVisible(true);
        });
    }
}
