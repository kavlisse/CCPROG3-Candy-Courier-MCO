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

    /** Controls leaderboard operations */
    private LeaderboardController leaderboardController;

    /** Stores the leaderboard table data */
    private DefaultTableModel tableModel;

    /** Displays the leaderboard records */
    private JTable leaderboardTable;

    /** Displays the leaderboard status */
    private JLabel statusLabel;

    /** Action to perform when the Back button is clicked */
    private Runnable onBack;


    /**
     * Constructs a LeaderboardPanel
     *
     * @param leaderboardController the controller handling leaderboard data
     */
    public LeaderboardPanel(LeaderboardController leaderboardController) {
        this(leaderboardController, null);
    }


    /**
     * Constructs a LeaderboardPanel with a back action
     *
     * @param leaderboardController the controller handling leaderboard data
     * @param onBack the action to perform when returning to the previous screen
     */
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

        // Create the leaderboard title
        JLabel title = new JLabel(
            "CANDY COURIER LEADERBOARD",
            SwingConstants.CENTER
        );

        // Define the leaderboard table columns
        String[] columns = {
            "Player Name",
            "Star Rank",
            "Days Spent",
            "Total Steps"
        };

        // Create a table model that prevents editing
        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the leaderboard table
        leaderboardTable = new JTable(tableModel);

        leaderboardTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        leaderboardTable.setFillsViewportHeight(true);

        // Add scrolling support
        JScrollPane scrollPane =
            new JScrollPane(leaderboardTable);

        // Create the status label
        statusLabel = new JLabel(" ");

        // Create the Refresh button
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> refreshLeaderboard());

        // Create the bottom button panel
        JPanel bottomPanel = new JPanel(
            new BorderLayout()
        );

        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        buttonPanel.add(refreshButton);

        // Add a Back button if a back action is provided
        if (onBack != null) {

            JButton backButton = new JButton("Back");

            backButton.addActionListener(e -> onBack.run());

            buttonPanel.add(backButton);
        }

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Arrange the components
        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load the leaderboard records
        refreshLeaderboard();
    }


    /**
     * Loads and displays the leaderboard records
     * Records are displayed according to the sorting
     * order maintained by the Leaderboard model
     */
    public void refreshLeaderboard() {

        // Reload the records from the text file
        leaderboardController.loadLeaderboard();

        // Clear the existing table rows
        tableModel.setRowCount(0);

        // Retrieve the sorted leaderboard records
        ArrayList<PlayerRecord> records =
            leaderboardController.getLeaderboard();

        // Display an appropriate message if no records exist
        if (records.isEmpty()) {

            statusLabel.setText("No leaderboard records available.");

            return;
        }

        // Add each player record to the table
        for (PlayerRecord record : records) {

            Object[] rowData = {
                record.getPlayerName(),
                record.getStarRank() + "-star",
                record.getDaysSpent(),
                record.getTotalSteps()
            };

            tableModel.addRow(rowData);
        }

        statusLabel.setText(
            "Total players: " + records.size()
        );
    }

}
