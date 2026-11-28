
package view;

import java.awt.*;
import javax.swing.*;

public class SidebarPanel extends JPanel {

    public SidebarPanel(GamePanel game) {
        setLayout(new GridLayout(0, 1, 7, 7));
        setBackground(Ui.CREAM);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        addNav("WORLD", game::showWorld, true);
        addNav("INVENTORY", game::showInventory, false);
        addNav("ORDERS", game::showOrders, false);
        addNav("FACTORIES", game::showSearch, false);
        addNav("PROFILE", game::showProfile, false);
        addNav("MAIN MENU", game::returnToMenu, false);
    }

    private void addNav(String label, Runnable action, boolean primary) {
        JButton button = primary
            ? Ui.button(label)
            : Ui.secondaryButton(label);

        button.setPreferredSize(new Dimension(145, 60));
        button.addActionListener(e -> action.run());
        add(button);
    }
}
