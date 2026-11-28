
package view;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

public class SidebarPanel extends JPanel {

    private final Map<String, JButton> buttons =
        new LinkedHashMap<>();

    public SidebarPanel(GamePanel game) {
        setLayout(new GridLayout(6, 1, 0, 9));
        setBackground(Ui.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(
            14, 12, 14, 12
        ));

        addNav("WORLD", "✦", game::showWorld);
        addNav("INVENTORY", "▦", game::showInventory);
        addNav("ORDERS", "▤", game::showOrders);
        addNav("FIND FACTORIES", "⌕", game::showSearch);
        addNav("PROFILE", "◎", game::showProfile);
        addNav("MAIN MENU", "←", game::returnToMenu);

        setActive("WORLD");
    }

    private void addNav(
        String label,
        String icon,
        Runnable action
    ) {
        JButton button = new JButton(
            "<html><div style='text-align:left;'>"
            + "<span style='font-size:17px;'>"
            + icon
            + "</span>&nbsp;&nbsp;"
            + label
            + "</div></html>"
        );

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font(
            "Segoe UI", Font.BOLD, 11
        ));
        button.setForeground(new Color(218, 207, 225));
        button.setBackground(Ui.SURFACE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(
            14, 15, 14, 10
        ));
        button.setCursor(Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR
        ));

        button.addActionListener(e -> {
            action.run();
            setActive(label);
        });

        buttons.put(label, button);
        add(button);
    }

    public void setActive(String active) {
        for (Map.Entry<String, JButton> entry
                : buttons.entrySet()) {

            boolean selected =
                entry.getKey().equals(active);

            JButton button = entry.getValue();

            button.setBackground(
                selected ? Ui.BERRY : Ui.SURFACE
            );

            button.setForeground(
                selected ? Color.WHITE
                    : new Color(218, 207, 225)
            );
        }
    }
}
