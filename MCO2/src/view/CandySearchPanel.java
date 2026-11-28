
package view;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Candy;
import model.Factory;

public class CandySearchPanel extends JPanel {

    private final GameController c;

    private final JTextField typeQuery = new JTextField(14);
    private final JTextField flavorQuery = new JTextField(14);

    private final JPanel resultsPanel = new JPanel();

    private final JLabel resultCount =
        Ui.smallLabel("Search for a candy factory.");

    public CandySearchPanel(GameController c) {
        this.c = c;

        setLayout(new BorderLayout(18, 18));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(
            header, BoxLayout.Y_AXIS
        ));

        header.add(Ui.eyebrow("WORLD DISCOVERY"));
        header.add(Box.createVerticalStrut(5));
        header.add(Ui.heading("Find Factories"));
        header.add(Box.createVerticalStrut(5));
        header.add(Ui.smallLabel(
            "Search for factories that produce your desired candies."
        ));

        JPanel searchCard = Ui.card();
        searchCard.setLayout(new BorderLayout(15, 12));

        JPanel fields = new JPanel(
            new GridLayout(1, 2, 12, 0)
        );
        fields.setOpaque(false);

        JPanel typeField = createSearchField(
            "CANDY TYPE",
            typeQuery
        );

        JPanel flavorField = createSearchField(
            "FLAVOR",
            flavorQuery
        );

        fields.add(typeField);
        fields.add(flavorField);

        JButton searchButton = Ui.button("SEARCH  →");
        searchButton.addActionListener(e -> search());

        searchCard.add(fields, BorderLayout.CENTER);
        searchCard.add(searchButton, BorderLayout.EAST);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(
            top, BoxLayout.Y_AXIS
        ));

        top.add(header);
        top.add(Box.createVerticalStrut(18));
        top.add(searchCard);

        add(top, BorderLayout.NORTH);

        resultsPanel.setLayout(new BoxLayout(
            resultsPanel, BoxLayout.Y_AXIS
        ));
        resultsPanel.setBackground(Ui.CREAM);

        JScrollPane scroll = new JScrollPane(resultsPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
        add(resultCount, BorderLayout.SOUTH);

        typeQuery.addActionListener(e -> search());
        flavorQuery.addActionListener(e -> search());
    }

    private JPanel createSearchField(
        String label,
        JTextField field
    ) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(
            panel, BoxLayout.Y_AXIS
        ));

        JLabel heading = Ui.eyebrow(label);

        Ui.styleTextField(field);
        field.setMaximumSize(new Dimension(
            Integer.MAX_VALUE, 43
        ));

        panel.add(heading);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);

        return panel;
    }

    private void search() {
        resultsPanel.removeAll();

        String type = typeQuery.getText().trim();
        String flavor = flavorQuery.getText().trim();

        if (type.isEmpty()) {
            resultCount.setText(
                "Enter a candy type to begin searching."
            );
            Ui.message(this, "Please enter a candy type.");
            return;
        }

        java.util.ArrayList<Factory> matches =
            c.getFactoryController().searchCandies(
                type, flavor
            );

        if (matches.isEmpty()) {
            resultCount.setText(
                "No matching factories found."
            );

            JPanel empty = Ui.card();
            empty.setLayout(new BorderLayout());

            JLabel message = Ui.label(
                "No factories produce the requested candy."
            );
            empty.add(message, BorderLayout.CENTER);

            resultsPanel.add(empty);

        } else {
            resultCount.setText(
                matches.size() + " matching factory/factories"
            );

            for (Factory factory : matches) {
                resultsPanel.add(
                    createFactoryCard(factory, type, flavor)
                );
                resultsPanel.add(
                    Box.createVerticalStrut(12)
                );
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createFactoryCard(
        Factory factory,
        String type,
        String flavor
    ) {
        JPanel card = Ui.card();
        card.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel name = Ui.heading(factory.getName());

        JLabel location = Ui.smallLabel(
            "ROW " + (factory.getRow() + 1)
            + "  •  COLUMN " + (factory.getColumn() + 1)
        );

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(
            titleGroup, BoxLayout.Y_AXIS
        ));

        titleGroup.add(name);
        titleGroup.add(Box.createVerticalStrut(5));
        titleGroup.add(location);

        JLabel icon = new JLabel("🏭");
        icon.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 30
        ));

        top.add(titleGroup, BorderLayout.CENTER);
        top.add(icon, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        JPanel varieties = new JPanel(
            new GridLayout(0, 2, 8, 8)
        );
        varieties.setOpaque(false);

        Candy[][] grid =
            c.getFactoryController().getProductionGrid(factory);

        Set<String> found = new LinkedHashSet<>();

        if (grid != null) {
            for (Candy[] row : grid) {
                if (row == null) continue;

                for (Candy candy : row) {
                    if (candy == null) continue;

                    boolean typeMatch =
                        candy.getCandyType().equalsIgnoreCase(type);

                    boolean flavorMatch =
                        flavor.isEmpty()
                        || candy.getCandyFlavor()
                            .equalsIgnoreCase(flavor);

                    if (typeMatch && flavorMatch) {
                        found.add(
                            candy.getCandyType()
                            + " — "
                            + candy.getCandyFlavor()
                        );
                    }
                }
            }
        }

        for (String variety : found) {
            JLabel item = Ui.label("🍬  " + variety);
            item.setOpaque(true);
            item.setBackground(Ui.BLUSH);
            item.setBorder(new EmptyBorder(
                10, 12, 10, 12
            ));

            varieties.add(item);
        }

        if (found.isEmpty()) {
            varieties.add(Ui.label(
                "No matching varieties currently available."
            ));
        }

        card.add(varieties, BorderLayout.CENTER);

        return card;
    }

    public void refresh() {
        // Search results remain available until a new search.
    }
}
