import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.MainFrame;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            new MainFrame().setVisible(true);
        });
    }
}