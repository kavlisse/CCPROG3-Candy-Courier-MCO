
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

    private final PlayerInfoPanel playerInfo =
        new PlayerInfoPanel();

    private final WorldPanel worldPanel;

    private final InventoryPanel inventoryPanel;
    private final OrdersPanel ordersPanel;
    private final CandySearchPanel searchPanel;
    private final PlayerInfoScreen profilePanel;

    private final SidebarPanel sidebar;
    private final JLabel status = new JLabel("Explore the world!");

    public GamePanel(MainFrame frame, GameController controller) {
        this.frame = frame;
        this.controller = controller;

        setLayout(new BorderLayout(0, 0));
        setBackground(Ui.BACKGROUND);

        worldPanel = new WorldPanel(this, controller);
        inventoryPanel = new InventoryPanel(controller);
        ordersPanel = new OrdersPanel(controller);
        searchPanel = new CandySearchPanel(controller);
        profilePanel = new PlayerInfoScreen(controller);

        content.setBackground(Ui.CREAM);

        content.add(worldPanel, "world");
        content.add(inventoryPanel, "inventory");
        content.add(ordersPanel, "orders");
        content.add(searchPanel, "search");
        content.add(profilePanel, "profile");

        sidebar = new SidebarPanel(this);

        add(playerInfo, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        add(createBottomBar(), BorderLayout.SOUTH);

        installKeys();
    }

    private JPanel createBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Ui.BACKGROUND);
        bottom.setBorder(BorderFactory.createEmptyBorder(
            12, 20, 12, 20
        ));

        status.setForeground(Color.WHITE);
        status.setFont(new Font(
            "Segoe UI", Font.BOLD, 12
        ));

        JLabel controls = new JLabel(
            "WASD / ARROWS     SPACE / ENTER TO INTERACT"
        );

        controls.setForeground(new Color(173, 155, 181));
        controls.setFont(new Font(
            "Segoe UI", Font.PLAIN, 10
        ));

        bottom.add(status, BorderLayout.WEST);
        bottom.add(controls, BorderLayout.EAST);

        return bottom;
    }

    private void installKeys() {
        InputMap input = getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        ActionMap actions = getActionMap();

        bind(input, actions, KeyEvent.VK_UP, "up", "up");
        bind(input, actions, KeyEvent.VK_W, "w", "up");

        bind(input, actions, KeyEvent.VK_DOWN, "down", "down");
        bind(input, actions, KeyEvent.VK_S, "s", "down");

        bind(input, actions, KeyEvent.VK_LEFT, "left", "left");
        bind(input, actions, KeyEvent.VK_A, "a", "left");

        bind(input, actions, KeyEvent.VK_RIGHT, "right", "right");
        bind(input, actions, KeyEvent.VK_D, "d", "right");

        input.put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "interact"
        );

        input.put(
            KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
            "interact"
        );

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
            status.setText(
                "You moved " + direction.toUpperCase() + "."
            );
            refreshAll();
        } else {
            status.setText("You cannot move that way.");
        }
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
        profilePanel.refresh();
    }

    public void showWorld() {
        worldPanel.refresh();
        contentCards.show(content, "world");
        sidebar.setActive("WORLD");
        status.setText("Explore the world!");
    }

    public void showInventory() {
        inventoryPanel.refresh();
        contentCards.show(content, "inventory");
        sidebar.setActive("INVENTORY");
        status.setText("Manage your collected candies.");
    }

    public void showOrders() {
        ordersPanel.refresh();
        contentCards.show(content, "orders");
        sidebar.setActive("ORDERS");
        status.setText("Check your delivery quests.");
    }

    public void showSearch() {
        searchPanel.refresh();
        contentCards.show(content, "search");
        sidebar.setActive("FIND FACTORIES");
        status.setText("Discover candy-producing factories.");
    }

    public void showProfile() {
        profilePanel.refresh();
        contentCards.show(content, "profile");
        sidebar.setActive("PROFILE");
        status.setText("Your courier profile.");
    }

    public void returnToMenu() {
        boolean confirmed = Ui.confirm(
            this,
            "END ADVENTURE",
            "Return to the main menu? Your current run will end and be recorded.",
            "END GAME",
            "KEEP PLAYING"
        );

        if (confirmed) {
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

            boolean rest = Ui.confirm(
                this,
                "REST AT HOME",
                "Rest at home and advance to the next day?",
                "REST",
                "CANCEL"
            );

            if (rest) {
                controller.handlePlayerHome(true);
                status.setText("A new day begins!");
                refreshAll();
            }

        } else {
            status.setText(
                "There is nothing to interact with here."
            );
        }
    }
}
