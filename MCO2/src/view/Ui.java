
package view;

import java.awt.*;
import javax.swing.*;

final class Ui {

    // Main color palette
    static final Color DARK = new Color(119, 48, 83);
    static final Color BERRY = new Color(173, 76, 119);
    static final Color PINK = new Color(235, 151, 183);
    static final Color PALE_PINK = new Color(252, 231, 239);
    static final Color PALE = new Color(252, 231, 239);
    static final Color CREAM = new Color(255, 248, 243);
    static final Color LAVENDER = new Color(237, 229, 248);
    static final Color WHITE = Color.WHITE;
    static final Color INK = new Color(76, 54, 65);
    static final Color MUTED = new Color(155, 125, 139);
    static final Color BORDER = new Color(239, 205, 218);

    // Fonts
    static final Font TITLE_FONT =
        new Font("Trebuchet MS", Font.BOLD, 38);

    static final Font HEADING_FONT =
        new Font("Trebuchet MS", Font.BOLD, 25);

    static final Font BODY_FONT =
        new Font("Segoe UI", Font.PLAIN, 14);

    static final Font BUTTON_FONT =
        new Font("Trebuchet MS", Font.BOLD, 14);

    private Ui() {
    }

    static JPanel page() {

        JPanel p = new JPanel(new BorderLayout(14, 14));

        p.setBackground(CREAM);

        p.setBorder(
            BorderFactory.createEmptyBorder(22, 24, 22, 24)
        );

        return p;
    }

    static JLabel heading(String s) {

        JLabel l = new JLabel(s);

        l.setFont(HEADING_FONT);
        l.setForeground(DARK);

        return l;
    }

    static JButton button(String s) {

        JButton b = new JButton(s);

        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.setBackground(BERRY);
        b.setForeground(WHITE);

        b.setFont(BUTTON_FONT);

        b.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BERRY, 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
            )
        );

        b.setOpaque(true);

        b.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(DARK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(BERRY);
            }
        });

        return b;
    }

    static JButton secondaryButton(String s) {

        JButton b = new JButton(s);

        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.setBackground(PALE_PINK);
        b.setForeground(DARK);

        b.setFont(BUTTON_FONT);

        b.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
            )
        );

        b.setOpaque(true);

        return b;
    }

    static JLabel label(String s) {

        JLabel l = new JLabel(s);

        l.setForeground(INK);
        l.setFont(BODY_FONT);

        return l;
    }

    static JPanel card() {

        JPanel p = new JPanel();

        p.setBackground(WHITE);

        p.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
            )
        );

        return p;
    }

    static JLabel smallLabel(String s) {

        JLabel l = new JLabel(s);

        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(MUTED);

        return l;
    }
}
