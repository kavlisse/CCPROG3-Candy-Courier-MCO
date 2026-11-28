package view;

import java.awt.*;
import javax.swing.*;
import controller.GameController;
import model.House;
import model.Candy;

public class HousePanel {
    private HousePanel() {}

    public static void showDialog(Component parent, GameController c, House house) {
        if (house.isDelivered()) {
            Ui.message(parent, "This delivery quest is already complete.");
            return;
        }

        Candy[] candies = house.getRequiredCandies();
        int[] qty = house.getRequiredQuantities();

        StringBuilder message = new StringBuilder("Delivery request\n\n");

        for (int i = 0; i < candies.length; i++) {
            message.append("🍬 ").append(candies[i].getCandyType())
                .append(" • ").append(candies[i].getCandyFlavor())
                .append(" × ").append(qty[i]).append('\n');
        }

        message.append("\n✦ Reward: ").append(house.getXPReward()).append(" XP");

        int choice = JOptionPane.showConfirmDialog(parent, message.toString(),
            "🏠 " + house.getName(), JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE);

        if (choice == JOptionPane.OK_OPTION) {
            if (c.deliverHouseOrder(house)) {
                Ui.message(parent, "Quest completed! +"
                    + house.getXPReward() + " XP");
            } else {
                Ui.message(parent, "Not enough candy to complete this delivery.");
            }
        }
    }
}