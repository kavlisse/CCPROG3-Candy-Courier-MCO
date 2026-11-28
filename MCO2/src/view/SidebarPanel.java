package view;

import java.awt.*;
import javax.swing.*;

public class SidebarPanel extends JPanel {

    public SidebarPanel(GamePanel game) {

        setLayout(new GridLayout(0, 1, 6, 6));
        setBackground(Ui.CREAM);

        setBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        addNav("World", () -> game.showWorld());

        addNav(
            "Inventory",
            () -> game.showInventory()
        );

        addNav(
            "Orders",
            () -> game.showOrders()
        );

        addNav(
            "Search candies",
            () -> game.showSearch()
        );

        addNav(
            "Player info",
            () -> game.showPlayerInfo()
        );

        addNav(
            "Main menu",
            () -> game.returnToMenu()
        );
    }

    private void addNav(
        String text,
        Runnable action
    ) {

        JButton b = Ui.button(text);
        b.addActionListener(e -> action.run());

        add(b);
    }
}