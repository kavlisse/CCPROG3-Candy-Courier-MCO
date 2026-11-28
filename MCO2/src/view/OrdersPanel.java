
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.*;
import controller.GameController;

public class OrdersPanel extends JPanel {

    private final GameController c;

    private final JPanel ordersContainer =
        new JPanel();

    public OrdersPanel(GameController c) {

        this.c = c;

        setLayout(new BorderLayout(12, 12));
        setBackground(Ui.CREAM);

        setBorder(
            new EmptyBorder(12, 12, 12, 12)
        );

        // Page heading
        JLabel heading = Ui.heading("Delivery Orders");

        JLabel subtitle = new JLabel(
            "Complete house requests to earn experience points."
        );

        subtitle.setFont(
            new Font("Trebuchet MS", Font.PLAIN, 13)
        );
        subtitle.setForeground(Ui.INK);

        JPanel header = new JPanel();
        header.setLayout(
            new BoxLayout(header, BoxLayout.Y_AXIS)
        );
        header.setBackground(Ui.CREAM);

        header.add(heading);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // Orders container
        ordersContainer.setLayout(
            new BoxLayout(ordersContainer, BoxLayout.Y_AXIS)
        );

        ordersContainer.setBackground(Ui.CREAM);

        JScrollPane scroll = new JScrollPane(
            ordersContainer
        );

        scroll.setBorder(null);
        scroll.getViewport().setBackground(Ui.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {

        ordersContainer.removeAll();

        for (
            House h :
            c.getHouseController().getHouses()
        ) {

            ordersContainer.add(createOrderCard(h));

            ordersContainer.add(
                Box.createVerticalStrut(14)
            );
        }

        ordersContainer.revalidate();
        ordersContainer.repaint();
    }

    private JPanel createOrderCard(House house) {

        boolean delivered = house.isDelivered();

        JPanel card = new JPanel(
            new BorderLayout(12, 12)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    delivered
                        ? new Color(174, 210, 183)
                        : new Color(238, 185, 205),
                    1
                ),
                new EmptyBorder(16, 18, 16, 18)
            )
        );

        // Top section: House name and status
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JLabel houseName = new JLabel(
            house.getName().toUpperCase()
        );

        houseName.setFont(
            new Font("Trebuchet MS", Font.BOLD, 19)
        );

        houseName.setForeground(Ui.DARK);

        JLabel status = new JLabel(
            delivered ? "DELIVERED" : "PENDING"
        );

        status.setFont(
            new Font("Trebuchet MS", Font.BOLD, 11)
        );

        status.setOpaque(true);

        status.setForeground(
            delivered
                ? new Color(45, 105, 65)
                : new Color(155, 53, 96)
        );

        status.setBackground(
            delivered
                ? new Color(224, 242, 225)
                : new Color(252, 226, 237)
        );

        status.setBorder(
            new EmptyBorder(6, 12, 6, 12)
        );

        top.add(houseName, BorderLayout.WEST);
        top.add(status, BorderLayout.EAST);

        // XP reward section
        JPanel rewardPanel = new JPanel(
            new BorderLayout()
        );

        rewardPanel.setBackground(
            new Color(252, 237, 243)
        );

        rewardPanel.setBorder(
            new EmptyBorder(10, 12, 10, 12)
        );

        JLabel rewardLabel = new JLabel(
            "DELIVERY REWARD"
        );

        rewardLabel.setFont(
            new Font("Trebuchet MS", Font.BOLD, 10)
        );

        rewardLabel.setForeground(Ui.INK);

        JLabel reward = new JLabel(
            "+" + house.getXPReward() + " XP"
        );

        reward.setFont(
            new Font("Trebuchet MS", Font.BOLD, 20)
        );

        reward.setForeground(
            new Color(176, 67, 119)
        );

        rewardPanel.add(
            rewardLabel,
            BorderLayout.WEST
        );

        rewardPanel.add(
            reward,
            BorderLayout.EAST
        );

        // Candy requirements
        JPanel requirements = new JPanel(
            new BorderLayout(5, 8)
        );

        requirements.setBackground(Color.WHITE);

        JLabel requirementsTitle = new JLabel(
            "REQUIRED CANDIES"
        );

        requirementsTitle.setFont(
            new Font("Trebuchet MS", Font.BOLD, 12)
        );

        requirementsTitle.setForeground(Ui.DARK);

        requirements.add(
            requirementsTitle,
            BorderLayout.NORTH
        );

        JPanel candyGrid = new JPanel(
            new GridLayout(0, 2, 8, 8)
        );

        candyGrid.setBackground(Color.WHITE);

        Candy[] candies = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        for (int i = 0; i < candies.length; i++) {

            JPanel candyItem = new JPanel(
                new BorderLayout()
            );

            candyItem.setBackground(
                new Color(255, 244, 248)
            );

            candyItem.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        new Color(245, 215, 226)
                    ),
                    new EmptyBorder(9, 10, 9, 10)
                )
            );

            JLabel candyName = new JLabel(
                candies[i].getCandyType()
            );

            candyName.setFont(
                new Font("Trebuchet MS", Font.PLAIN, 13)
            );

            candyName.setForeground(Ui.INK);

            JLabel quantity = new JLabel(
                "× " + quantities[i]
            );

            quantity.setFont(
                new Font("Trebuchet MS", Font.BOLD, 14)
            );

            quantity.setForeground(
                new Color(176, 67, 119)
            );

            candyItem.add(
                candyName,
                BorderLayout.CENTER
            );

            candyItem.add(
                quantity,
                BorderLayout.EAST
            );

            candyGrid.add(candyItem);
        }

        requirements.add(
            candyGrid,
            BorderLayout.CENTER
        );

        // Assemble card
        JPanel content = new JPanel(
            new BorderLayout(10, 12)
        );

        content.setBackground(Color.WHITE);

        content.add(top, BorderLayout.NORTH);
        content.add(rewardPanel, BorderLayout.CENTER);
        content.add(requirements, BorderLayout.SOUTH);

        card.add(content, BorderLayout.CENTER);

        return card;
    }
}
