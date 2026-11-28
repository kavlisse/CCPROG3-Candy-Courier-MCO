
package view;

import java.awt.*;
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

    private final WorldPanel worldPanel;
    private final InventoryPanel inventoryPanel;
    private final OrdersPanel ordersPanel;
    private final CandySearchPanel searchPanel;
    private final PlayerInfoScreen playerScreen;

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
        playerScreen = new PlayerInfoScreen(controller);

        content.add(worldPanel, "world");
        content.add(inventoryPanel, "inventory");
        content.add(ordersPanel, "orders");
        content.add(searchPanel, "search");
        content.add(playerScreen, "player");

        add(playerInfo, BorderLayout.NORTH);
        add(new SidebarPanel(this), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JPanel arrows = new JPanel(
            new GridLayout(2, 3, 5, 5)
        );
        arrows.setOpaque(false);

        arrows.add(new JLabel());
        moveButton(arrows, "↑", "up");
        arrows.add(new JLabel());

        moveButton(arrows, "←", "left");
        moveButton(arrows, "↓", "down");
        moveButton(arrows, "→", "right");

        bottom.add(arrows, BorderLayout.WEST);

        JButton interact = Ui.button("INTERACT HERE");

        interact.addActionListener(
            e -> interactCurrentLocation()
        );

        bottom.add(interact, BorderLayout.CENTER);

        status.setForeground(Ui.DARK);
        bottom.add(status, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
    }

    private void moveButton(
        JPanel p,
        String label,
        String dir
    ) {

        JButton b = Ui.button(label);

        b.addActionListener(e -> {
            if (controller.movePlayer(dir)) {
                status.setText("Moved " + dir + ".");
                refreshAll();
            } else {
                status.setText(
                    "You cannot move that way."
                );
            }
        });

        p.add(b);
    }

    public void refreshAll() {

        Player p = controller.getGame().getPlayer();

        playerInfo.refresh(
            p,
            controller.getGame().getDayNum()
        );

        playerScreen.refresh();

        worldPanel.refresh();
        inventoryPanel.refresh();
        ordersPanel.refresh();
        searchPanel.refresh();
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
        contentCards.show(content, "search");
    }

    public void showPlayerInfo() {
        playerScreen.refresh();
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

        } else if (b instanceof House) {

            HousePanel.showDialog(
                frame,
                controller,
                (House) b
            );

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
