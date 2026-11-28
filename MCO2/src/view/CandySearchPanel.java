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
    private final JTextField typeQuery = new JTextField(12);
    private final JTextField flavorQuery = new JTextField(12);
    private final JPanel resultsPanel = new JPanel();
    private final JLabel resultCount = Ui.smallLabel("Search for a candy factory.");

    public CandySearchPanel(GameController c) {
        this.c = c;
        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setOpaque(false);
        header.add(Ui.heading("🏭  Find Candy Factories"), BorderLayout.NORTH);

        JPanel search = new JPanel(new GridBagLayout());
        search.setBackground(Ui.WHITE);
        search.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        search.add(Ui.label("Candy type"), g);
        g.gridx = 1; g.weightx = 1;
        search.add(typeQuery, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        search.add(Ui.label("Flavor"), g);
        g.gridx = 1; g.weightx = 1;
        search.add(flavorQuery, g);

        JButton button = Ui.button("🔍 SEARCH");
        g.gridx = 2; g.gridy = 0; g.gridheight = 2; g.weightx = 0;
        search.add(button, g);
        button.addActionListener(e -> search());

        header.add(search, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Ui.CREAM);

        JScrollPane scroll = new JScrollPane(resultsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(resultCount, BorderLayout.SOUTH);

        typeQuery.addActionListener(e -> search());
        flavorQuery.addActionListener(e -> search());
    }

    private void search() {
        resultsPanel.removeAll();

        String type = typeQuery.getText().trim();
        String flavor = flavorQuery.getText().trim();

        if (type.isEmpty()) {
            resultCount.setText("Enter a candy type first.");
            Ui.message(this, "Please enter a candy type.");
            return;
        }

        java.util.ArrayList<Factory> matches =
            c.getFactoryController().searchCandies(type, flavor);

        if (matches.isEmpty()) {
            resultCount.setText("No matching factories found.");
            resultsPanel.add(Ui.label("No factories produce the requested candy."));
        } else {
            resultCount.setText(matches.size() + " matching factory/factories");

            for (Factory factory : matches) {
                resultsPanel.add(createFactoryCard(factory, type, flavor));
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createFactoryCard(Factory factory, String type, String flavor) {
        JPanel card = Ui.card();
        card.setLayout(new BorderLayout(8, 8));

        JLabel name = Ui.heading("🏭  " + factory.getName());
        JLabel location = Ui.smallLabel("Row " + (factory.getRow() + 1)
            + "  •  Column " + (factory.getColumn() + 1));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(name, BorderLayout.NORTH);
        top.add(location, BorderLayout.SOUTH);
        card.add(top, BorderLayout.NORTH);

        JPanel varieties = new JPanel(new GridLayout(0, 2, 8, 8));
        varieties.setOpaque(false);

        Candy[][] grid = c.getFactoryController().getProductionGrid(factory);
        Set<String> found = new LinkedHashSet<>();

        if (grid != null) {
            for (Candy[] row : grid) {
                if (row == null) continue;
                for (Candy candy : row) {
                    if (candy == null) continue;

                    boolean typeMatch = candy.getCandyType().equalsIgnoreCase(type);
                    boolean flavorMatch = flavor.isEmpty()
                        || candy.getCandyFlavor().equalsIgnoreCase(flavor);

                    if (typeMatch && flavorMatch) {
                        found.add("🍬  " + candy.getCandyType()
                            + " — " + candy.getCandyFlavor());
                    }
                }
            }
        }

        if (found.isEmpty()) {
            varieties.add(Ui.label("No matching varieties available."));
        } else {
            for (String variety : found) {
                JLabel item = Ui.label(variety);
                item.setOpaque(true);
                item.setBackground(Ui.BLUSH);
                item.setBorder(new EmptyBorder(8, 10, 8, 10));
                varieties.add(item);
            }
        }

        card.add(varieties, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {}
}