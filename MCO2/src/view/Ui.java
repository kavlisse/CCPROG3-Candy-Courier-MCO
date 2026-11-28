package view;

import java.awt.*;
import javax.swing.*;

final class Ui {

    static final Color DARK = new Color(30, 76, 57);
    static final Color GREEN = new Color(66, 133, 97);
    static final Color PALE = new Color(231, 242, 232);
    static final Color CREAM = new Color(250, 248, 240);
    static final Color INK = new Color(43, 54, 47);

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
        l.setFont(new Font("SansSerif", Font.BOLD, 25));
        l.setForeground(DARK);
        return l;
    }

    static JButton button(String s) {
        JButton b = new JButton(s);
        b.setFocusPainted(false);
        b.setBackground(GREEN);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        );
        return b;
    }

    static JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setForeground(INK);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return l;
    }

    static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(218, 226, 216)
                ),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
            )
        );
        return p;
    }
}