
package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.*;
import model.*;

public class GamePanel extends JPanel {

    private final MainFrame frame;
    private final GameController controller;

    private final CardLayout contentCards = new CardLayout();
    private final JPanel content = new JPanel(contentCards);

    private final PlayerInfoPanel playerInfo =
        new PlayerInfoPanel();

    private final PlayerInfoScreen playerInfoScreen;

    private final WorldPanel worldPanel;
    private final InventoryPanel inventoryPanel;
    private final OrdersPanel ordersPanel;
    private final CandySearchPanel searchPanel;

    private final JLabel status =
        new JLabel("Welcome, courier!");

    public GamePanel(
        MainFrame frame,
        GameController controller
    ) {

        this.frame = frame;
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBackground(Ui.CREAM);

        setBorder(
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        worldPanel = new WorldPanel(this, controller);
        inventoryPanel = new InventoryPanel(controller);
        ordersPanel = new OrdersPanel(controller);
        searchPanel = new CandySearchPanel(controller);

        playerInfoScreen = new PlayerInfoScreen(controller);

        content.add(worldPanel, "world");
        content.add(inventoryPanel, "inventory");
        content.add(ordersPanel, "orders");
        content.add(searchPanel, "search");
        content.add(playerInfoScreen, "player");

        add(playerInfo, BorderLayout.NORTH);
        add(new SidebarPanel(this), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        // Movement controls
        JPanel bottom = new JPanel(new BorderLayout(20, 10));
        bottom.setBackground(Ui.CREAM);
        bottom.setBorder(
            BorderFactory.createEmptyBorder(8, 0, 0, 0)
        );

        JPanel movementArea = new JPanel(new BorderLayout(5, 5));
        movementArea.setBackground(Ui.CREAM);

        JLabel movementTitle = Ui.label("MOVEMENT");
        movementTitle.setFont(
            new Font("Trebuchet MS", Font.BOLD, 12)
        );
        movementTitle.setForeground(Ui.DARK);

        movementArea.add(movementTitle, BorderLayout.NORTH);

        JPanel directions = new JPanel(
            new GridLayout(2, 3, 6, 6)
        );
        directions.setBackground(Ui.CREAM);

        directions.add(new JLabel());

        moveButton(directions, "UP", "up");

        directions.add(new JLabel());

        moveButton(directions, "LEFT", "left");
        moveButton(directions, "DOWN", "down");
        moveButton(directions, "RIGHT", "right");

        movementArea.add(directions, BorderLayout.CENTER);

        bottom.add(movementArea, BorderLayout.WEST);

        // Interaction area
        JPanel interactionArea = new JPanel(new BorderLayout());
        interactionArea.setBackground(Ui.CREAM);
        interactionArea.setBorder(
            BorderFactory.createEmptyBorder(20, 35, 0, 0)
        );

        JButton interact = Ui.button("INTERACT WITH LOCATION");

        interact.setPreferredSize(
            new Dimension(270, 65)
        );

        interact.addActionListener(
            e -> interactCurrentLocation()
        );

        interactionArea.add(interact, BorderLayout.CENTER);

        bottom.add(interactionArea, BorderLayout.CENTER);

        // Status message
        status.setForeground(Ui.DARK);
        status.setFont(
            new Font("Trebuchet MS", Font.BOLD, 13)
        );

        bottom.add(status, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        // Enable keyboard movement
        installMovementKeys();
    }

    private void moveButton(
        JPanel p,
        String label,
        String dir
    ) {

        JButton b = Ui.button(label);

        b.setPreferredSize(new Dimension(75, 48));
        b.setFont(
            new Font("Trebuchet MS", Font.BOLD, 12)
        );

        b.addActionListener(e -> move(dir));

        p.add(b);
    }

    private void move(String direction) {

        if (controller.movePlayer(direction)) {

            status.setText("Moved " + direction + ".");
            refreshAll();

        } else {

            status.setText("You cannot move that way.");
        }
    }

    private void installMovementKeys() {

        InputMap input = getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        ActionMap actions = getActionMap();

        // Arrow keys
        bindKey(input, actions, KeyEvent.VK_UP, "moveUp", "up");
        bindKey(input, actions, KeyEvent.VK_DOWN, "moveDown", "down");
        bindKey(input, actions, KeyEvent.VK_LEFT, "moveLeft", "left");
        bindKey(input, actions, KeyEvent.VK_RIGHT, "moveRight", "right");

        // WASD keys
        bindKey(input, actions, KeyEvent.VK_W, "moveW", "up");
        bindKey(input, actions, KeyEvent.VK_S, "moveS", "down");
        bindKey(input, actions, KeyEvent.VK_A, "moveA", "left");
        bindKey(input, actions, KeyEvent.VK_D, "moveD", "right");
    }

    private void bindKey(
        InputMap input,
        ActionMap actions,
        int key,
        String actionName,
        String direction
    ) {

        input.put(
            KeyStroke.getKeyStroke(key, 0),
            actionName
        );

        actions.put(actionName, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                move(direction);
            }
        });
    }

    public void refreshAll() {

        Player p = controller.getGame().getPlayer();

        playerInfo.refresh(
            p,
            controller.getGame().getDayNum()
        );

        worldPanel.refresh();
        inventoryPanel.refresh();
        ordersPanel.refresh();
        searchPanel.refresh();

        playerInfoScreen.refresh();
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

    public void showPlayerInfo() {
        playerInfoScreen.refresh();
        contentCards.show(content, "player");
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

            FactoryPanel.showDialog(
                frame,
                controller,
                (Factory) b
            );

            refreshAll();

        } else if (b instanceof House) {

            HousePanel.showDialog(
                frame,
                controller,
                (House) b
            );

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
                status.setText(
                    "You rested. A new day begins!"
                );
                refreshAll();
            }

        } else {

            frame.showMessage(
                "There is nothing to interact with here."
            );
        }
    }
}
