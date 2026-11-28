
package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.GameController;
import model.*;

public class GamePanel extends JPanel {

    private final MainFrame frame;
    private final GameController controller;

    private final CardLayout contentCards = new CardLayout();
    private final JPanel content = new JPanel(contentCards);

    private final PlayerInfoPanel playerInfo = new PlayerInfoPanel();
    private final WorldPanel worldPanel;

    private final InventoryPanel inventoryPanel;
    private final OrdersPanel ordersPanel;
    private final CandySearchPanel searchPanel;
    private final PlayerInfoScreen profilePanel;

    private final JLabel status = new JLabel("Explore the world!");

    public GamePanel(MainFrame frame, GameController controller) {
        this.frame = frame;
        this.controller = controller;

        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(28, 35, 31));

        worldPanel = new WorldPanel(this, controller);
        inventoryPanel = new InventoryPanel(controller);
        ordersPanel = new OrdersPanel(controller);
        searchPanel = new CandySearchPanel(controller);
        profilePanel = new PlayerInfoScreen(controller);

        content.setBackground(new Color(28, 35, 31));

        content.add(worldPanel, "world");
        content.add(inventoryPanel, "inventory");
        content.add(ordersPanel, "orders");
        content.add(searchPanel, "search");
        content.add(profilePanel, "profile");

        add(playerInfo, BorderLayout.NORTH);
        add(createNavigation(), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        add(createBottomBar(), BorderLayout.SOUTH);

        installKeys();
    }

    private JPanel createNavigation() {
        JPanel nav = new JPanel(new GridLayout(6, 1, 5, 5));
        nav.setBackground(new Color(42, 24, 48));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        addNav(nav, "WORLD", () -> showWorld());
        addNav(nav, "INVENTORY", () -> showInventory());
        addNav(nav, "ORDERS", () -> showOrders());
        addNav(nav, "FIND FACTORIES", () -> showSearch());
        addNav(nav, "PROFILE", () -> showProfile());
        addNav(nav, "MAIN MENU", () -> returnToMenu());

        return nav;
    }

    private void addNav(JPanel nav, String text, Runnable action) {
        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(104, 65, 111));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> action.run());
        nav.add(button);
    }

    private JPanel createBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(42, 24, 48));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        status.setForeground(Color.WHITE);
        status.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel controls = new JLabel(
            "WASD / ARROWS  •  SPACE / ENTER TO INTERACT"
        );

        controls.setForeground(new Color(220, 200, 225));
        controls.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        bottom.add(status, BorderLayout.WEST);
        bottom.add(controls, BorderLayout.EAST);

        return bottom;
    }

    private void installKeys() {
        InputMap input = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actions = getActionMap();

        bind(input, actions, KeyEvent.VK_UP, "up", "up");
        bind(input, actions, KeyEvent.VK_W, "w", "up");

        bind(input, actions, KeyEvent.VK_DOWN, "down", "down");
        bind(input, actions, KeyEvent.VK_S, "s", "down");

        bind(input, actions, KeyEvent.VK_LEFT, "left", "left");
        bind(input, actions, KeyEvent.VK_A, "a", "left");

        bind(input, actions, KeyEvent.VK_RIGHT, "right", "right");
        bind(input, actions, KeyEvent.VK_D, "d", "right");

        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "interact");
        input.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "interact");

        actions.put("interact", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interactCurrentLocation();
            }
        });
    }

    private void bind(
        InputMap input,
        ActionMap actions,
        int key,
        String name,
        String direction
    ) {
        input.put(KeyStroke.getKeyStroke(key, 0), name);

        actions.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move(direction);
            }
        });
    }

    private void move(String direction) {
        if (controller.movePlayer(direction)) {
            status.setText("Moved " + direction.toUpperCase());
            refreshAll();
        } else {
            status.setText("You cannot move that way.");
        }
    }

    private void addMovementButton(
        JPanel panel,
        String text,
        String direction
    ) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusable(false);
        button.setBackground(new Color(255, 232, 242));
        button.setForeground(new Color(73, 34, 61));
        button.setBorder(BorderFactory.createLineBorder(
            new Color(235, 207, 220)
        ));

        button.addActionListener(e -> move(direction));
        panel.add(button);
    }

    public void refreshAll() {
        Player p = controller.getGame().getPlayer();

        playerInfo.refresh(p, controller.getGame().getDayNum());

        worldPanel.refresh();
        inventoryPanel.refresh();
        ordersPanel.refresh();
        searchPanel.refresh();
        profilePanel.refresh();
    }

    public void showWorld() {
        worldPanel.refresh();
        contentCards.show(content, "world");
    }

    public void showInventory() {
        inventoryPanel.refresh();
        contentCards.show(content, "inventory");
    }

    public void showOrders() {
        ordersPanel.refresh();
        contentCards.show(content, "orders");
    }

    public void showSearch() {
        searchPanel.refresh();
        contentCards.show(content, "search");
    }

    public void showProfile() {
        profilePanel.refresh();
        contentCards.show(content, "profile");
    }

    public void returnToMenu() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Return to the main menu? Your current run will end and be recorded.",
            "End game",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            frame.endSession();
        }
    }

    public void interactCurrentLocation() {
        Building b = controller.getCurrentBuilding();

        if (b instanceof Factory) {
            FactoryPanel.showDialog(frame, controller, (Factory) b);
            refreshAll();

        } else if (b instanceof House) {
            HousePanel.showDialog(frame, controller, (House) b);
            refreshAll();

        } else if (b instanceof PlayerHome) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Rest at home and advance to the next day?",
                "Player home",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                controller.handlePlayerHome(true);
                status.setText("A new day begins!");
                refreshAll();
            }

        } else {
            status.setText("Nothing to interact with here.");
        }
    }
}
