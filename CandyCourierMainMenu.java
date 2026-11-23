import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Candy Courier - Main Menu / World Overview screen.
 * Static Java Swing interface for Quest 5.
 */
public class CandyCourierMainMenu extends JFrame {

    private final Color darkGreen = new Color(30, 76, 57);
    private final Color green = new Color(66, 133, 97);
    private final Color paleGreen = new Color(231, 242, 232);
    private final Color cream = new Color(250, 248, 240);
    private final Color ink = new Color(43, 54, 47);

    public CandyCourierMainMenu() {
        setTitle("Candy Courier | Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(cream);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkGreen);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("CANDY COURIER");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 27));

        JLabel subtitle = new JLabel("A little adventure, one candy at a time.");
        subtitle.setForeground(new Color(215, 231, 218));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        brand.add(title);
        brand.add(Box.createVerticalStrut(4));
        brand.add(subtitle);

        JPanel playerInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        playerInfo.setOpaque(false);
        JLabel day = new JLabel("DAY 1");
        day.setOpaque(true);
        day.setBackground(new Color(245, 224, 166));
        day.setForeground(darkGreen);
        day.setFont(new Font("SansSerif", Font.BOLD, 13));
        day.setBorder(new EmptyBorder(8, 15, 8, 15));

        JLabel player = new JLabel("PLAYER  |  Katelyn");
        player.setForeground(Color.WHITE);
        player.setFont(new Font("SansSerif", Font.BOLD, 14));
        player.setBorder(new EmptyBorder(0, 8, 0, 0));
        playerInfo.add(day);
        playerInfo.add(player);

        header.add(brand, BorderLayout.WEST);
        header.add(playerInfo, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout(22, 0));
        content.setBackground(cream);
        content.setBorder(new EmptyBorder(26, 30, 28, 30));

        JPanel left = new JPanel(new BorderLayout(0, 14));
        left.setOpaque(false);

        JPanel intro = new JPanel(new BorderLayout());
        intro.setOpaque(false);
        JLabel heading = new JLabel("Your World");
        heading.setForeground(ink);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        JLabel description = new JLabel("Explore the map, visit factories, and deliver candy orders.");
        description.setForeground(new Color(112, 120, 112));
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JPanel introText = new JPanel();
        introText.setOpaque(false);
        introText.setLayout(new BoxLayout(introText, BoxLayout.Y_AXIS));
        introText.add(heading);
        introText.add(Box.createVerticalStrut(5));
        introText.add(description);
        intro.add(introText, BorderLayout.WEST);

        JPanel mapCard = new JPanel(new BorderLayout(0, 14));
        mapCard.setBackground(Color.WHITE);
        mapCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 229, 219)),
                new EmptyBorder(20, 20, 18, 20)));

        JPanel mapTop = new JPanel(new BorderLayout());
        mapTop.setOpaque(false);
        JLabel mapTitle = new JLabel("WORLD GRID");
        mapTitle.setForeground(darkGreen);
        mapTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        JLabel gridSize = new JLabel("7 × 7 MAP");
        gridSize.setForeground(new Color(130, 139, 130));
        gridSize.setFont(new Font("SansSerif", Font.BOLD, 11));
        mapTop.add(mapTitle, BorderLayout.WEST);
        mapTop.add(gridSize, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(7, 7, 7, 7));
        grid.setBackground(Color.WHITE);
        grid.setBorder(new EmptyBorder(5, 10, 5, 10));
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                String symbol = ".";
                String tip = "Empty tile";
                Color tileColor = new Color(246, 248, 242);
                Color textColor = new Color(172, 181, 169);

                if (row == 0 && col == 6) {
                    symbol = "F"; tip = "Factory 1"; tileColor = new Color(225, 239, 226); textColor = darkGreen;
                } else if (row == 6 && col == 0) {
                    symbol = "F"; tip = "Factory 2"; tileColor = new Color(225, 239, 226); textColor = darkGreen;
                } else if ((row == 1 && col == 1) || (row == 3 && col == 6) || (row == 5 && col == 2)) {
                    symbol = "D"; tip = "House"; tileColor = new Color(250, 239, 218); textColor = new Color(151, 102, 38);
                } else if (row == 3 && col == 3) {
                    symbol = "P"; tip = "Katelyn's position / Home"; tileColor = darkGreen; textColor = Color.WHITE;
                }

                JLabel tile = new JLabel(symbol, SwingConstants.CENTER);
                tile.setOpaque(true);
                tile.setBackground(tileColor);
                tile.setForeground(textColor);
                tile.setFont(new Font("SansSerif", Font.BOLD, 20));
                tile.setBorder(BorderFactory.createLineBorder(new Color(232, 235, 227)));
                tile.setToolTipText(tip + " — row " + row + ", column " + col);
                grid.add(tile);
            }
        }

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        legend.setOpaque(false);
        legend.add(legendItem("P", "Player", darkGreen));
        legend.add(legendItem("F", "Factory", green));
        legend.add(legendItem("D", "House", new Color(151, 102, 38)));
        legend.add(legendItem(".", "Empty", new Color(172, 181, 169)));

        mapCard.add(mapTop, BorderLayout.NORTH);
        mapCard.add(grid, BorderLayout.CENTER);
        mapCard.add(legend, BorderLayout.SOUTH);

        left.add(intro, BorderLayout.NORTH);
        left.add(mapCard, BorderLayout.CENTER);

        JPanel right = createMenuPanel();
        content.add(left, BorderLayout.CENTER);
        content.add(right, BorderLayout.EAST);
        return content;
    }

    private JPanel legendItem(String symbol, String label, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);
        JLabel icon = new JLabel(symbol, SwingConstants.CENTER);
        icon.setForeground(color);
        icon.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel text = new JLabel(label);
        text.setForeground(new Color(105, 114, 105));
        text.setFont(new Font("SansSerif", Font.PLAIN, 12));
        item.add(icon);
        item.add(text);
        return item;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 229, 219)),
                new EmptyBorder(22, 20, 20, 20)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel menuTitle = new JLabel("MAIN MENU");
        menuTitle.setForeground(darkGreen);
        menuTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel menuSub = new JLabel("Where would you like to go?");
        menuSub.setForeground(new Color(125, 133, 124));
        menuSub.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panel.add(menuTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(menuSub);
        panel.add(Box.createVerticalStrut(22));

        addMenuButton(panel, "01", "Travel", "Explore the world", true);
        addMenuButton(panel, "02", "View Inventory", "Check your collected candies", false);
        addMenuButton(panel, "03", "Search Candies", "Find which factories make them", false);
        addMenuButton(panel, "04", "View Orders", "See house delivery requests", false);
        addMenuButton(panel, "05", "Player Info", "View your progress and rank", false);

        panel.add(Box.createVerticalGlue());
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(232, 235, 227));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(15));

        JButton quit = new JButton("Quit Game");
        quit.setAlignmentX(Component.LEFT_ALIGNMENT);
        quit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        quit.setFocusPainted(false);
        quit.setBackground(new Color(250, 248, 240));
        quit.setForeground(new Color(139, 83, 72));
        quit.setFont(new Font("SansSerif", Font.BOLD, 13));
        quit.setBorder(BorderFactory.createLineBorder(new Color(235, 225, 216)));
        panel.add(quit);
        return panel;
    }

    private void addMenuButton(JPanel parent, String number, String title,
                               String description, boolean highlighted) {
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        buttonPanel.setPreferredSize(new Dimension(235, 62));
        buttonPanel.setBackground(highlighted ? paleGreen : Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(highlighted ? new Color(190, 216, 194) : new Color(239, 241, 235)),
                new EmptyBorder(9, 10, 9, 10)));

        JLabel numberLabel = new JLabel(number);
        numberLabel.setForeground(highlighted ? darkGreen : new Color(154, 164, 151));
        numberLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ink);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        JLabel descLabel = new JLabel(description);
        descLabel.setForeground(new Color(128, 136, 127));
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);

        buttonPanel.add(numberLabel, BorderLayout.WEST);
        buttonPanel.add(textPanel, BorderLayout.CENTER);
        parent.add(buttonPanel);
        parent.add(Box.createVerticalStrut(9));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CandyCourierMainMenu screen = new CandyCourierMainMenu();
            screen.setVisible(true);
        });
    }
}
