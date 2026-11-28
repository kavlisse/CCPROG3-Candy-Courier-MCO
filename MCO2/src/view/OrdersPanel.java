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
        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);
        setBorder(new EmptyBorder(18, 20, 18, 20));

        add(Ui.heading("📜  Delivery Orders"), BorderLayout.NORTH);

        ordersContainer.setLayout(
            new BoxLayout(ordersContainer, BoxLayout.Y_AXIS));
        ordersContainer.setBackground(Ui.CREAM);

        JScrollPane scroll = new JScrollPane(ordersContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        ordersContainer.removeAll();

        for (House house : c.getHouseController().getHouses()) {
            ordersContainer.add(createOrderCard(house));
            ordersContainer.add(Box.createVerticalStrut(12));
        }

        ordersContainer.revalidate();
        ordersContainer.repaint();
    }

    private JPanel createOrderCard(House house) {
        boolean delivered = house.isDelivered();

        JPanel card = Ui.card();
        card.setLayout(new BorderLayout(10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel name = Ui.heading("🏠  " + house.getName());
        JLabel status = Ui.smallLabel(delivered ? "✓ DELIVERED" : "● PENDING");
        status.setForeground(delivered ? new Color(40, 130, 75) : Ui.BERRY);

        top.add(name, BorderLayout.WEST);
        top.add(status, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        JPanel requirements = new JPanel(new GridLayout(0, 2, 8, 8));
        requirements.setOpaque(false);

        Candy[] candies = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        for (int i = 0; i < candies.length; i++) {
            JLabel item = Ui.label("🍬  " + candies[i].getCandyType()
                + "  × " + quantities[i]);
            item.setBorder(new EmptyBorder(7, 9, 7, 9));
            item.setOpaque(true);
            item.setBackground(Ui.BLUSH);
            requirements.add(item);
        }

        card.add(requirements, BorderLayout.CENTER);

        JLabel reward = Ui.label("✦  Reward: +" + house.getXPReward() + " XP");
        reward.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reward.setForeground(Ui.BERRY);
        card.add(reward, BorderLayout.SOUTH);

        return card;
    }
}