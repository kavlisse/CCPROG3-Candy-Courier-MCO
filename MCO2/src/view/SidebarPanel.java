
package view;

import java.awt.*;
import javax.swing.*;

public class SidebarPanel extends JPanel {

    public SidebarPanel(GamePanel game) {

        setLayout(new GridLayout(0, 1, 8, 8));
        setBackground(Ui.CREAM);

        setBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        addNav("WORLD", () -> game.showWorld(), true);

        addNav(
            "INVENTORY",
            () -> game.showInventory(),
            false
        );

        addNav(
            "ORDERS",
            () -> game.showOrders(),
            false
        );

        addNav(
            "CANDY SEARCH",
            () -> game.showSearch(),
            false
        );

        addNav(
            "PLAYER PROFILE",
            () -> game.showPlayerInfo(),
            false
        );

        addNav(
            "MAIN MENU",
            () -> game.returnToMenu(),
            false
        );
    }

    private void addNav(
        String text,
        Runnable action,
        boolean primary
    ) {

        JButton b = primary
            ? Ui.button(text)
            : Ui.secondaryButton(text);

        b.setFont(
            new Font("Trebuchet MS", Font.BOLD, 13)
        );

        b.setPreferredSize(new Dimension(145, 65));
        b.setFocusPainted(false);

        b.addActionListener(e -> action.run());

        add(b);
    }
}
