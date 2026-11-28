
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.House;
import model.Candy;

public class HousePanel {

    private HousePanel() {}

    public static void showDialog(
        Component parent,
        GameController c,
        House house
    ) {
        if (house.isDelivered()) {
            Ui.message(
                parent,
                "This delivery quest is already complete."
            );
            return;
        }

        Candy[] candies = house.getRequiredCandies();
        int[] quantities = house.getRequiredQuantities();

        JDialog dialog = new JDialog(
            SwingUtilities.getWindowAncestor(parent),
            "Delivery Quest",
            Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(Ui.CREAM);
        root.setBorder(new EmptyBorder(24, 26, 22, 26));

        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(Ui.DARK);
        header.setBorder(new EmptyBorder(
            18, 20, 18, 20
        ));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(
            titleGroup, BoxLayout.Y_AXIS
        ));

        JLabel eyebrow = Ui.eyebrow("NEW DELIVERY REQUEST");
        eyebrow.setForeground(Ui.PINK);

        JLabel title = new JLabel(house.getName());
        title.setFont(new Font(
            "Segoe UI", Font.BOLD, 23
        ));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "A resident needs your help!"
        );
        subtitle.setFont(Ui.SMALL);
        subtitle.setForeground(new Color(216, 198, 220));

        titleGroup.add(eyebrow);
        titleGroup.add(Box.createVerticalStrut(5));
        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(4));
        titleGroup.add(subtitle);

        JLabel icon = new JLabel("🏠");
        icon.setFont(new Font(
            "Segoe UI Emoji", Font.PLAIN, 38
        ));

        header.add(titleGroup, BorderLayout.CENTER);
        header.add(icon, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(12, 15));
        content.setOpaque(false);

        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(
            section, BoxLayout.Y_AXIS
        ));

        JLabel requirementsTitle = Ui.eyebrow(
            "ORDER REQUIREMENTS"
        );

        section.add(requirementsTitle);
        section.add(Box.createVerticalStrut(12));

        JPanel requirements = new JPanel(
            new GridLayout(0, 2, 9, 9)
        );
        requirements.setOpaque(false);

        for (int i = 0; i < candies.length; i++) {
            JPanel item = new JPanel(new BorderLayout(8, 3));
            item.setBackground(Ui.BLUSH);
            item.setBorder(new EmptyBorder(
                12, 12, 12, 12
            ));

            JPanel details = new JPanel();
            details.setOpaque(false);
            details.setLayout(new BoxLayout(
                details, BoxLayout.Y_AXIS
            ));

            JLabel candy = Ui.label(
                candies[i].getCandyType()
            );
            candy.setFont(new Font(
                "Segoe UI", Font.BOLD, 12
            ));

            JLabel flavor = Ui.smallLabel(
                candies[i].getCandyFlavor()
            );

            details.add(candy);
            details.add(Box.createVerticalStrut(4));
            details.add(flavor);

            JLabel quantity = new JLabel(
                "× " + quantities[i]
            );
            quantity.setFont(new Font(
                "Segoe UI", Font.BOLD, 16
            ));
            quantity.setForeground(Ui.BERRY);

            item.add(new JLabel("🍬"), BorderLayout.WEST);
            item.add(details, BorderLayout.CENTER);
            item.add(quantity, BorderLayout.EAST);

            requirements.add(item);
        }

        section.add(requirements);

        JPanel reward = Ui.accentCard();
        reward.setLayout(new BorderLayout());

        JPanel rewardDetails = new JPanel();
        rewardDetails.setOpaque(false);
        rewardDetails.setLayout(new BoxLayout(
            rewardDetails, BoxLayout.Y_AXIS
        ));

        JLabel rewardHeading = Ui.eyebrow("QUEST REWARD");

        JLabel rewardValue = new JLabel(
            "+" + house.getXPReward() + " XP"
        );
        rewardValue.setFont(new Font(
            "Segoe UI", Font.BOLD, 25
        ));
        rewardValue.setForeground(Ui.BERRY);

        rewardDetails.add(rewardHeading);
        rewardDetails.add(Box.createVerticalStrut(5));
        rewardDetails.add(rewardValue);

        reward.add(rewardDetails, BorderLayout.CENTER);

        content.add(section, BorderLayout.CENTER);
        content.add(reward, BorderLayout.SOUTH);

        root.add(content, BorderLayout.CENTER);

        JPanel footer = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 9, 0)
        );
        footer.setOpaque(false);

        JButton deliver = Ui.button("DELIVER ORDER  →");
        JButton cancel = Ui.secondaryButton("CANCEL");

        deliver.addActionListener(e -> {
            if (c.deliverHouseOrder(house)) {
                dialog.dispose();

                Ui.message(
                    parent,
                    "Quest completed! +"
                    + house.getXPReward() + " XP"
                );
            } else {
                Ui.message(
                    dialog,
                    "Not enough candy to complete this delivery."
                );
            }
        });

        cancel.addActionListener(e -> dialog.dispose());

        footer.add(deliver);
        footer.add(cancel);

        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(550, 560);
        dialog.setMinimumSize(new Dimension(480, 500));
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
