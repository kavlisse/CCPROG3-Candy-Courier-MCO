package view;

import java.awt.*;
import javax.swing.*;

import controller.GameController;
import model.*;

public class HousePanel {

    private HousePanel() {
    }

    public static void showDialog(
        Component parent,
        GameController c,
        House house
    ) {

        if (house.isDelivered()) {

            JOptionPane.showMessageDialog(
                parent,
                "This order has already been delivered."
            );

            return;
        }

        Candy[] candies = house.getRequiredCandies();
        int[] qty = house.getRequiredQuantities();

        StringBuilder text = new StringBuilder(
            house.getName() + "\n\nOrder requirements:\n"
        );

        for (int i = 0; i < candies.length; i++) {

            text.append("• ")
                .append(candies[i].getCandyType())
                .append(" / ")
                .append(candies[i].getCandyFlavor())
                .append(" × ")
                .append(qty[i])
                .append('\n');
        }

        text.append("\nReward: ")
            .append(house.getXPReward())
            .append(" XP");

        int choice = JOptionPane.showConfirmDialog(
            parent,
            text.toString(),
            "House delivery",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (choice == JOptionPane.OK_OPTION) {

            if (c.deliverHouseOrder(house)) {

                JOptionPane.showMessageDialog(
                    parent,
                    "Order delivered! You earned " +
                    house.getXPReward() +
                    " XP."
                );

            } else {

                JOptionPane.showMessageDialog(
                    parent,
                    "You do not have enough candy to complete this order."
                );
            }
        }
    }
}