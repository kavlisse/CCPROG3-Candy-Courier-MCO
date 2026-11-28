package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.LeaderboardController;
import model.PlayerRecord;

public class LeaderboardPanel extends JPanel {
    private final LeaderboardController controller;
    private final DefaultTableModel model;
    private final JLabel status = Ui.smallLabel(" ");

    public LeaderboardPanel(LeaderboardController controller) {
        this(controller, null);
    }

    public LeaderboardPanel(LeaderboardController controller, Runnable onBack) {
        if (controller == null)
            throw new IllegalArgumentException("LeaderboardController cannot be null.");

        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);
        setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));

        add(Ui.heading("🏆  Courier Leaderboard"), BorderLayout.NORTH);

        String[] columns = {"Player", "Star Rank", "Days", "Steps"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(Ui.BODY);
        table.setForeground(Ui.DARK);
        table.setSelectionBackground(Ui.BLUSH);
        table.getTableHeader().setFont(Ui.BUTTON);
        table.getTableHeader().setBackground(Ui.BLUSH);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Ui.BORDER));
        add(scroll, BorderLayout.CENTER);

        JButton refresh = Ui.secondaryButton("↻ REFRESH");
        refresh.addActionListener(e -> refreshLeaderboard());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(status, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        buttons.add(refresh);

        if (onBack != null) {
            JButton back = Ui.button("← BACK");
            back.addActionListener(e -> onBack.run());
            buttons.add(back);
        }

        bottom.add(buttons, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        refreshLeaderboard();
    }

    public void refreshLeaderboard() {
        controller.loadLeaderboard();
        model.setRowCount(0);

        ArrayList<PlayerRecord> records = controller.getLeaderboard();

        for (PlayerRecord r : records) {
            model.addRow(new Object[]{
                r.getPlayerName(),
                "★ " + r.getStarRank(),
                r.getDaysSpent(),
                r.getTotalSteps()
            });
        }

        status.setText(records.isEmpty()
            ? "No leaderboard records yet."
            : records.size() + " courier record(s)");
    }
}