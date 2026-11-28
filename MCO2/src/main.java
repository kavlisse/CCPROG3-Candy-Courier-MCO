
import javax.swing.SwingUtilities;

import view.MainFrame;

/**
 * Main entry point of the Candy Courier application
 *
 * @author Katelyn and Shaira
 * @version 1.0
 */
public class main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Create the application window
            MainFrame frame = new MainFrame();

            frame.setVisible(true);
        });
    }
}
