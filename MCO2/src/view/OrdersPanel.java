
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Candy;
import model.House;

public class OrdersPanel extends JPanel {

    private final GameController c;
    private final JPanel ordersContainer = new JPanel();

    public OrdersPanel(GameController c) {
        this.c = c;

        setLayout(new BorderLayout(18, 18));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(
            header, BoxLayout.Y_AXIS
        ));

        header.add(Ui.eyebrow("QUEST BOARD"));
        header.add(Box.createVerticalStrut(5));
        header.add(Ui.heading("Delivery Orders"));
        header.add(Box.createVerticalStrut(5));

        JLabel subtitle = Ui.smallLabel(
            "Complete deliveries to earn experience points."
        );
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        ordersContainer.setLayout(new BoxLayout(
            ordersContainer, BoxLayout.Y_AXIS
        ));
        ordersContainer.setBackground(Ui.CREAM);

        JScrollPane scroll = new JScrollPane(ordersContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        ordersContainer.removeAll();

        for (House house : c.getHouseController().getHouses()) {
            ordersContainer.add(createOrderCard(house));
            ordersContainer.add(Box.createVerticalStrut(15));
        }

        ordersContainer.revalidate();
        ordersContainer.repaint();
    }

    private JPanel createOrderCard(House house) {
        boolean delivered = house.isDelivered();

        JPanel card = Ui.card();
        card.setLayout(new BorderLayout(14, 14));
        card.setMaximumSize(new Dimension(
            Integer.MAX_VALUE, 260
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(
            titleGroup, BoxLayout.Y_AXIS
        ));

        JLabel name = Ui.heading(house.getName());
        JLabel description = Ui.smallLabel(
            "CANDY DELIVERY REQUEST"
        );

        titleGroup.add(name);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(description);

        JLabel status = new JLabel(
            delivered ? "✓  COMPLETED" : "●  PENDING"
        );

        status.setFont(new Font(
            "Segoe UI", Font.BOLD, 10
        ));

        status.setForeground(
            delivered ? Ui.GREEN_DARK : Ui.BERRY
        );

        status.setBorder(new EmptyBorder(
            8, 12, 8, 12
        ));

        status.setOpaque(true);
        status.setBackground(
            delivered
                ? new Color(226, 242, 224)
                : Ui.BLUSH
        );

        header.add(titleGroup, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);

        JPanel requirements = new JPanel(
            new GridLayout(0, 2, 9, 9)
        );
        requirements.setOpaque(false);

        Candy[] candies = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        for (int i = 0; i < candies.length; i++) {
            JPanel item = new JPanel(new BorderLayout(8, 0));
            item.setBackground(Ui.BLUSH);
            item.setBorder(new EmptyBorder(
                10, 12, 10, 12
            ));

            JLabel candy = Ui.label(
                "🍬  " + candies[i].getCandyType()
            );

            JLabel quantity = Ui.label(
                "× " + quantities[i]
            );

            quantity.setFont(new Font(
                "Segoe UI", Font.BOLD, 13
            ));
            quantity.setForeground(Ui.BERRY);

            item.add(candy, BorderLayout.CENTER);
            item.add(quantity, BorderLayout.EAST);

            requirements.add(item);
        }

        card.add(requirements, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel reward = Ui.label(
            "✦  +" + house.getXPReward() + " XP"
        );

        reward.setFont(new Font(
            "Segoe UI", Font.BOLD, 16
        ));
        reward.setForeground(Ui.BERRY);

        footer.add(reward, BorderLayout.WEST);

        if (delivered) {
            JLabel complete = Ui.smallLabel(
                "Delivery completed"
            );
            footer.add(complete, BorderLayout.EAST);
        } else {
            JLabel instruction = Ui.smallLabel(
                "Visit this house on the world map to deliver."
            );
            footer.add(instruction, BorderLayout.EAST);
        }

        card.add(footer, BorderLayout.SOUTH);

        return card;
    }
}
