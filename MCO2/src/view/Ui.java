
package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

final class Ui {

    static final Color BACKGROUND = new Color(29, 21, 38);
    static final Color SURFACE = new Color(48, 35, 57);
    static final Color SURFACE_LIGHT = new Color(62, 46, 72);
    static final Color DARK = new Color(42, 27, 51);
    static final Color BERRY = new Color(190, 91, 143);
    static final Color PINK = new Color(239, 177, 204);
    static final Color BLUSH = new Color(255, 237, 244);
    static final Color CREAM = new Color(250, 246, 248);
    static final Color LAVENDER = new Color(218, 207, 242);
    static final Color GREEN = new Color(119, 176, 111);
    static final Color GREEN_DARK = new Color(73, 132, 79);
    static final Color GOLD = new Color(246, 205, 112);
    static final Color INK = new Color(65, 48, 67);
    static final Color MUTED = new Color(151, 133, 153);
    static final Color BORDER = new Color(232, 218, 230);
    static final Color WHITE = Color.WHITE;

    static final Font TITLE =
        new Font("Segoe UI", Font.BOLD, 32);
    static final Font HEADING =
        new Font("Segoe UI", Font.BOLD, 23);
    static final Font SUBHEADING =
        new Font("Segoe UI", Font.BOLD, 16);
    static final Font BODY =
        new Font("Segoe UI", Font.PLAIN, 14);
    static final Font BUTTON =
        new Font("Segoe UI", Font.BOLD, 13);
    static final Font SMALL =
        new Font("Segoe UI", Font.PLAIN, 12);

    private Ui() {}

    static JPanel page() {
        JPanel p = new JPanel(new BorderLayout(18, 18));
        p.setBackground(CREAM);
        p.setBorder(new EmptyBorder(26, 28, 26, 28));
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
        l.setFont(SMALL);
        l.setForeground(MUTED);
        return l;
    }

    static JLabel eyebrow(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(BERRY);
        return l;
    }

    static JPanel card() {
        return new RoundedPanel(WHITE, 18);
    }

    static JPanel darkCard() {
        return new RoundedPanel(SURFACE, 18);
    }

    static JPanel accentCard() {
        return new RoundedPanel(BLUSH, 18);
    }

    static JPanel section() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    static JButton button(String text) {
        return new GameButton(text, BERRY, WHITE);
    }

    static JButton secondaryButton(String text) {
        return new GameButton(text, BLUSH, DARK);
    }

    static JButton darkButton(String text) {
        return new GameButton(text, SURFACE_LIGHT, WHITE);
    }

    static JButton greenButton(String text) {
        return new GameButton(text, GREEN_DARK, WHITE);
    }

    static void styleTextField(JTextField field) {
        field.setFont(BODY);
        field.setForeground(DARK);
        field.setBackground(WHITE);
        field.setCaretColor(BERRY);
        field.setSelectionColor(PINK);
        field.setSelectedTextColor(DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(12, 14, 12, 14)
        ));
    }

    static JProgressBar progressBar(int value, int max) {
        JProgressBar bar = new JProgressBar(0, Math.max(1, max));
        bar.setValue(Math.max(0, Math.min(value, max)));
        bar.setStringPainted(false);
        bar.setBorderPainted(false);
        bar.setBackground(new Color(235, 226, 236));
        bar.setForeground(BERRY);
        bar.setPreferredSize(new Dimension(180, 9));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9));
        return bar;
    }

    static void message(Component parent, String message) {
        showDialog(
            parent,
            "CANDY COURIER",
            message,
            "GOT IT",
            null,
            null
        );
    }

    static boolean confirm(
        Component parent,
        String title,
        String message,
        String yesText,
        String noText
    ) {
        final boolean[] result = {false};

        showDialog(parent, title, message, yesText, () -> {
            result[0] = true;
        }, noText);

        return result[0];
    }

    private static void showDialog(
        Component parent,
        String title,
        String message,
        String yesText,
        Runnable yesAction,
        String noText
    ) {
        Window owner = SwingUtilities.getWindowAncestor(parent);

        JDialog dialog = new JDialog(
            owner,
            title,
            Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(CREAM);
        root.setBorder(new EmptyBorder(22, 24, 20, 24));

        JLabel heading = new JLabel(title);
        heading.setFont(SUBHEADING);
        heading.setForeground(DARK);

        JLabel body = new JLabel(
            "<html><div style='width:310px; line-height:1.5;'>"
            + message.replace("\n", "<br>")
            + "</div></html>"
        );
        body.setFont(BODY);
        body.setForeground(INK);

        root.add(heading, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);

        JPanel actions = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        actions.setOpaque(false);

        if (noText != null) {
            JButton cancel = secondaryButton(noText);
            cancel.addActionListener(e -> dialog.dispose());
            actions.add(cancel);
        }

        JButton accept = button(yesText);
        accept.addActionListener(e -> {
            if (yesAction != null) yesAction.run();
            dialog.dispose();
        });
        actions.add(accept);

        root.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(380, 190));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    static class RoundedPanel extends JPanel {
        private final Color fill;
        private final int radius;

        RoundedPanel(Color fill, int radius) {
            this.fill = fill;
            this.radius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(18, 20, 18, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(30, 20, 35, 13));
            g2.fillRoundRect(
                1, 3, getWidth() - 2, getHeight() - 4,
                radius, radius
            );

            g2.setColor(fill);
            g2.fillRoundRect(
                0, 0, getWidth() - 2, getHeight() - 5,
                radius, radius
            );

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class GameButton extends JButton {
        private final Color base;
        private final Color foreground;
        private boolean hovered;

        GameButton(String text, Color base, Color foreground) {
            super(text);
            this.base = base;
            this.foreground = foreground;

            setFont(BUTTON);
            setForeground(foreground);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            ));
            setBorder(new EmptyBorder(12, 19, 12, 19));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color fill = !isEnabled()
                ? BORDER
                : hovered ? base.brighter() : base;

            g2.setColor(fill);
            g2.fillRoundRect(
                0, 0, getWidth(), getHeight(), 13, 13
            );

            g2.dispose();

            setForeground(isEnabled() ? foreground : MUTED);
            super.paintComponent(g);
        }
    }
}
