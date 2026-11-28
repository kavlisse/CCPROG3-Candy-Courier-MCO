
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

final class Ui {
    static final Color DARK = new Color(73, 34, 61);
    static final Color BERRY = new Color(164, 65, 111);
    static final Color PINK = new Color(239, 151, 188);
    static final Color BLUSH = new Color(255, 232, 242);
    static final Color CREAM = new Color(255, 249, 246);
    static final Color LAVENDER = new Color(239, 233, 250);
    static final Color INK = new Color(76, 58, 69);
    static final Color MUTED = new Color(145, 122, 135);
    static final Color BORDER = new Color(235, 207, 220);
    static final Color WHITE = Color.WHITE;

    static final Font TITLE = new Font("Segoe UI", Font.BOLD, 30);
    static final Font HEADING = new Font("Segoe UI", Font.BOLD, 23);
    static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    private Ui() {}

    static JPanel page() {
        JPanel p = new JPanel(new BorderLayout(14, 14));
        p.setBackground(CREAM);
        p.setBorder(new EmptyBorder(20, 22, 20, 22));
        return p;
    }

    static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(HEADING);
        l.setForeground(DARK);
        return l;
    }

    static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(BODY);
        l.setForeground(INK);
        return l;
    }

    static JLabel smallLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(MUTED);
        return l;
    }

    static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(16, 18, 16, 18)
        ));
        return p;
    }

    static JButton button(String text) {
        return styleButton(new JButton(text), true);
    }

    static JButton secondaryButton(String text) {
        return styleButton(new JButton(text), false);
    }

    private static JButton styleButton(JButton b, boolean primary) {
        Color base = primary ? BERRY : BLUSH;

        b.setFont(BUTTON);
        b.setForeground(primary ? WHITE : DARK);
        b.setBackground(base);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primary ? BERRY : BORDER),
            new EmptyBorder(11, 16, 11, 16)
        ));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(primary ? DARK : new Color(250, 216, 230));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(base);
            }
        });

        return b;
    }

    static JPanel section() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    static void message(Component parent, String message) {
        JOptionPane.showMessageDialog(
            parent, message, "Candy Courier",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    static boolean confirm(
        Component parent,
        String title,
        String message,
        String yesText,
        String noText
    ) {
        Object[] options = { yesText, noText };

        return JOptionPane.showOptionDialog(
            parent, message, title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[1]
        ) == 0;
    }
}
