
package view;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.LeaderboardController;
import model.PlayerRecord;

/**
 * Displays the player leaderboard using a graphical table
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class LeaderboardPanel extends JPanel {

    private LeaderboardController leaderboardController;
    private DefaultTableModel tableModel;
    private JTable leaderboardTable;
    private JLabel statusLabel;
    private Runnable onBack;

    public LeaderboardPanel(LeaderboardController leaderboardController) {
        this(leaderboardController, null);
    }

    public LeaderboardPanel(
            LeaderboardController leaderboardController,
            Runnable onBack) {

        if (leaderboardController == null) {
            throw new IllegalArgumentException(
                "LeaderboardController cannot be null."
            );
        }

        this.leaderboardController = leaderboardController;
        this.onBack = onBack;

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
            "CANDY COURIER LEADERBOARD",
            SwingConstants.CENTER
        );

        String[] columns = {
            "Player Name",
            "Star Rank",
            "Days Spent",
            "Total Steps"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        leaderboardTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        statusLabel = new JLabel(" ");

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLeaderboard());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );
        buttonPanel.add(refreshButton);

        if (onBack != null) {
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> onBack.run());
            buttonPanel.add(backButton);
        }

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshLeaderboard();
    }

    public void refreshLeaderboard() {
        leaderboardController.loadLeaderboard();
        tableModel.setRowCount(0);

        ArrayList<PlayerRecord> records =
            leaderboardController.getLeaderboard();

        if (records.isEmpty()) {
            statusLabel.setText("No leaderboard records available.");
            return;
        }

        for (PlayerRecord record : records) {
            Object[] rowData = {
                record.getPlayerName(),
                record.getStarRank() + "-star",
                record.getDaysSpent(),
                record.getTotalSteps()
            };

            tableModel.addRow(rowData);
        }

        statusLabel.setText("Total players: " + records.size());
    }
}
