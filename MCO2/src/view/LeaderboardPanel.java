
package view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.LeaderboardController;
import model.PlayerRecord;

public class LeaderboardPanel extends JPanel {

    private final LeaderboardController controller;
    private final DefaultTableModel model;

    private final JLabel status =
        Ui.smallLabel("Loading courier rankings...");

    public LeaderboardPanel(
        LeaderboardController controller
    ) {
        this(controller, null);
    }

    public LeaderboardPanel(
        LeaderboardController controller,
        Runnable onBack
    ) {
        if (controller == null) {
            throw new IllegalArgumentException(
                "LeaderboardController cannot be null."
            );
        }

        this.controller = controller;

        setLayout(new BorderLayout(18, 18));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(
            titleGroup, BoxLayout.Y_AXIS
        ));

        titleGroup.add(Ui.eyebrow("COURIER HALL OF FAME"));
        titleGroup.add(Box.createVerticalStrut(5));
        titleGroup.add(Ui.heading("Leaderboard"));

        header.add(titleGroup, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        String[] columns = {
            "PLAYER", "STAR RANK", "DAYS", "TOTAL STEPS"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);

        table.setRowHeight(49);
        table.setFont(Ui.BODY);
        table.setForeground(Ui.DARK);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(Ui.BLUSH);
        table.setSelectionForeground(Ui.DARK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(Ui.BORDER);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.getTableHeader().setFont(new Font(
            "Segoe UI", Font.BOLD, 11
        ));
        table.getTableHeader().setForeground(Ui.MUTED);
        table.getTableHeader().setBackground(Ui.BLUSH);
        table.getTableHeader().setPreferredSize(
            new Dimension(0, 42)
        );
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);

        JButton refresh = Ui.secondaryButton("REFRESH  ↻");
        refresh.addActionListener(e -> refreshLeaderboard());

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        buttons.setOpaque(false);

        if (onBack != null) {
            JButton back = Ui.darkButton("BACK");
            back.addActionListener(e -> onBack.run());
            buttons.add(back);
        }

        buttons.add(refresh);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(status, BorderLayout.WEST);
        footer.add(buttons, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        refreshLeaderboard();
    }

    public void refreshLeaderboard() {
        controller.loadLeaderboard();
        model.setRowCount(0);

        ArrayList<PlayerRecord> records =
            controller.getLeaderboard();

        for (PlayerRecord r : records) {
            model.addRow(new Object[]{
                r.getPlayerName(),
                "★ " + r.getStarRank(),
                r.getDaysSpent(),
                r.getTotalSteps()
            });
        }

        status.setText(
            records.isEmpty()
                ? "No leaderboard records yet."
                : records.size() + " courier record(s)"
        );
    }
}
