package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.Factory;

public class CandySearchPanel extends JPanel {

    private final GameController c;

    private final JTextField query =
        new JTextField(16);

    private final DefaultListModel<String> results =
        new DefaultListModel<>();

    public CandySearchPanel(GameController c) {

        this.c = c;

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        add(
            Ui.heading("Find candy factories"),
            BorderLayout.NORTH
        );

        JPanel top = new JPanel();
        top.setOpaque(false);

        top.add(Ui.label("Candy type:"));
        top.add(query);

        JButton search = Ui.button("Search");
        top.add(search);

        add(top, BorderLayout.CENTER);

        JList<String> list = new JList<>(results);

        add(new JScrollPane(list), BorderLayout.SOUTH);

        search.addActionListener(e -> search());
    }

    private void search() {

        results.clear();

        String q = query.getText().trim();

        if (q.isEmpty()) {

            results.addElement(
                "Enter a candy type to search."
            );

            return;
        }

        for (
            Factory f :
            c.getFactoryController().searchCandies(q)
        ) {

            results.addElement(
                f.getName() +
                " at row " +
                (f.getRow() + 1) +
                ", column " +
                (f.getColumn() + 1)
            );
        }

        if (results.isEmpty()) {

            results.addElement(
                "No factories found for " + q + "."
            );
        }
    }

    public void refresh() {
    }
}