
package view;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Factory;
import model.Candy;

public class CandySearchPanel extends JPanel {

    private final GameController c;

    private final JTextField typeQuery =
        new JTextField(15);

    private final JTextField flavorQuery =
        new JTextField(15);

    private final JPanel resultsPanel =
        new JPanel();

    private final JLabel resultCount =
        new JLabel("Enter your search to find candy factories.");

    public CandySearchPanel(GameController c) {

        this.c = c;

        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);

        setBorder(
            new EmptyBorder(12, 12, 12, 12)
        );

        // Page heading
        JLabel heading = Ui.heading("Find Candy Factories");

        JLabel subtitle = new JLabel(
            "Search for factories based on the candy type and flavor you need."
        );

        subtitle.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 13)
        );

        subtitle.setForeground(Ui.INK);

        JPanel header = new JPanel();
        header.setLayout(
            new BoxLayout(header, BoxLayout.Y_AXIS)
        );
        header.setBackground(Ui.CREAM);

        header.add(heading);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // Search section
        JPanel searchCard = new JPanel(
            new GridBagLayout()
        );

        searchCard.setBackground(Color.WHITE);

        searchCard.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(238, 185, 205)
                ),
                new EmptyBorder(16, 18, 16, 18)
            )
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Candy type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        JLabel typeLabel = new JLabel("Candy type");

        typeLabel.setFont(
            new Font("Trebuchet MS", Font.BOLD, 13)
        );

        searchCard.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        typeQuery.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 14)
        );

        typeQuery.setToolTipText(
            "Example: Candy Corn"
        );

        searchCard.add(typeQuery, gbc);

        // Candy flavor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        JLabel flavorLabel = new JLabel("Candy flavor");

        flavorLabel.setFont(
            new Font("Trebuchet MS", Font.BOLD, 13)
        );

        searchCard.add(flavorLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        flavorQuery.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 14)
        );

        flavorQuery.setToolTipText(
            "Example: Strawberry"
        );

        searchCard.add(flavorQuery, gbc);

        // Search button
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;

        JButton searchButton =
            Ui.button("SEARCH");

        searchButton.setPreferredSize(
            new Dimension(115, 65)
        );

        searchButton.addActionListener(e -> search());

        searchCard.add(searchButton, gbc);

        // Search area and results
        JPanel mainContent = new JPanel(
            new BorderLayout(10, 14)
        );

        mainContent.setBackground(Ui.CREAM);

        mainContent.add(
            searchCard,
            BorderLayout.NORTH
        );

        // Results heading
        JPanel resultsHeader = new JPanel(
            new BorderLayout()
        );

        resultsHeader.setBackground(Ui.CREAM);

        JLabel resultsTitle = new JLabel(
            "SEARCH RESULTS"
        );

        resultsTitle.setFont(
            new Font("Trebuchet MS", Font.BOLD, 14)
        );

        resultsTitle.setForeground(Ui.DARK);

        resultCount.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 12)
        );

        resultCount.setForeground(Ui.INK);

        resultsHeader.add(
            resultsTitle,
            BorderLayout.WEST
        );

        resultsHeader.add(
            resultCount,
            BorderLayout.EAST
        );

        resultsPanel.setLayout(
            new BoxLayout(resultsPanel, BoxLayout.Y_AXIS)
        );

        resultsPanel.setBackground(Ui.CREAM);

        JScrollPane scroll = new JScrollPane(resultsPanel);

        scroll.setBorder(null);
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel resultsArea = new JPanel(
            new BorderLayout(8, 8)
        );

        resultsArea.setBackground(Ui.CREAM);

        resultsArea.add(
            resultsHeader,
            BorderLayout.NORTH
        );

        resultsArea.add(
            scroll,
            BorderLayout.CENTER
        );

        mainContent.add(
            resultsArea,
            BorderLayout.CENTER
        );

        add(mainContent, BorderLayout.CENTER);

        // Allow Enter to search
        typeQuery.addActionListener(e -> search());
        flavorQuery.addActionListener(e -> search());
    }

    private void search() {

        resultsPanel.removeAll();

        String type = typeQuery.getText().trim();
        String flavor = flavorQuery.getText().trim();

        if (type.isEmpty()) {

            resultCount.setText("Candy type is required.");

            showMessage(
                "Please enter a candy type before searching."
            );

            return;
        }

        // Search using candy type and optional flavor
        java.util.ArrayList<Factory> matches =
            c.getFactoryController().searchCandies(
                type,
                flavor
            );

        if (matches.isEmpty()) {

            resultCount.setText("No matching factories found.");

            JLabel empty = new JLabel(
                "No factories produce " + type +
                (flavor.isEmpty() ? "." : " with " + flavor + " flavor.")
            );

            empty.setFont(
                new Font("Trebuchet MS", Font.PLAIN, 14)
            );

            empty.setForeground(Ui.INK);

            resultsPanel.add(empty);

        } else {

            resultCount.setText(
                matches.size() + " matching factory/factories found."
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
        String searchedType,
        String searchedFlavor
    ) {

        JPanel card = new JPanel(
            new BorderLayout(10, 10)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(238, 185, 205)
                ),
                new EmptyBorder(15, 18, 15, 18)
            )
        );

        // Factory information
        JPanel top = new JPanel(
            new BorderLayout()
        );

        top.setBackground(Color.WHITE);

        JLabel name = new JLabel(
            factory.getName()
        );

        name.setFont(
            new Font("Trebuchet MS", Font.BOLD, 19)
        );

        name.setForeground(Ui.DARK);

        JLabel location = new JLabel(
            "Row " + (factory.getRow() + 1) +
            " · Column " + (factory.getColumn() + 1)
        );

        location.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 12)
        );

        location.setForeground(Ui.INK);

        JPanel factoryInfo = new JPanel(
            new GridLayout(2, 1, 0, 4)
        );

        factoryInfo.setBackground(Color.WHITE);

        factoryInfo.add(name);
        factoryInfo.add(location);

        top.add(factoryInfo, BorderLayout.CENTER);

        card.add(top, BorderLayout.NORTH);

        // Candy varieties
        JPanel candySection = new JPanel(
            new BorderLayout(5, 8)
        );

        candySection.setBackground(Color.WHITE);

        JLabel candyHeading = new JLabel(
            "CANDY VARIETIES"
        );

        candyHeading.setFont(
            new Font("Trebuchet MS", Font.BOLD, 12)
        );

        candyHeading.setForeground(Ui.DARK);

        candySection.add(
            candyHeading,
            BorderLayout.NORTH
        );

        JPanel candyGrid = new JPanel(
            new GridLayout(0, 2, 8, 8)
        );

        candyGrid.setBackground(Color.WHITE);

        Candy[][] productionGrid =
            c.getFactoryController().getProductionGrid(factory);

        Set<String> varieties = new LinkedHashSet<>();

        if (productionGrid != null) {

            for (Candy[] row : productionGrid) {

                if (row == null) {
                    continue;
                }

                for (Candy candy : row) {

                    if (candy == null) {
                        continue;
                    }

                    String candyType = candy.getCandyType();
                    String candyFlavor = candy.getCandyFlavor();

                    boolean typeMatches =
                        candyType.equalsIgnoreCase(searchedType);

                    boolean flavorMatches =
                        searchedFlavor.isEmpty() ||
                        candyFlavor.equalsIgnoreCase(searchedFlavor);

                    if (typeMatches && flavorMatches) {

                        varieties.add(
                            candyType + " — " + candyFlavor
                        );
                    }
                }
            }
        }

        if (varieties.isEmpty()) {

            JLabel noVarieties = new JLabel(
                "No matching candy varieties currently available."
            );

            noVarieties.setFont(
                new Font("Trebuchet MS", Font.PLAIN, 12)
            );

            candyGrid.add(noVarieties);

        } else {

            for (String variety : varieties) {

                JLabel candyLabel = new JLabel(
                    variety
                );

                candyLabel.setFont(
                    new Font("Trebuchet MS", Font.PLAIN, 12)
                );

                candyLabel.setOpaque(true);

                candyLabel.setBackground(
                    new Color(255, 241, 247)
                );

                candyLabel.setForeground(Ui.INK);

                candyLabel.setBorder(
                    new EmptyBorder(9, 10, 9, 10)
                );

                candyGrid.add(candyLabel);
            }
        }

        candySection.add(
            candyGrid,
            BorderLayout.CENTER
        );

        card.add(
            candySection,
            BorderLayout.CENTER
        );

        return card;
    }

    private void showMessage(String message) {

        JOptionPane.showMessageDialog(
            this,
            message,
            "Candy Search",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void refresh() {
    }
}
