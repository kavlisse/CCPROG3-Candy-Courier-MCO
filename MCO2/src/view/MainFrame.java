
package view;

import java.awt.*;
import javax.swing.*;

import controller.*;
import model.*;
import persistence.LeaderboardFileHandler;

public class MainFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final Game game = new Game();
    private final GameController controller;

    private final MainMenuPanel menu;
    private final PlayerSetupPanel setup;
    private final GamePanel gameplay;
    private final LeaderboardPanel leaderboard;

    public MainFrame() {
        super("Candy Courier");

        LeaderboardController lc =
            new LeaderboardController(
                new Leaderboard(),
                new LeaderboardFileHandler("leaderboard.txt")
            );

        controller = new GameController(game, lc);

        menu = new MainMenuPanel(this);
        setup = new PlayerSetupPanel(this);
        gameplay = new GamePanel(this, controller);

        leaderboard = new LeaderboardPanel(
            controller.getLeaderboardController(),
            () -> showMenu()
        );

        root.add(menu, "menu");
        root.add(setup, "setup");
        root.add(gameplay, "game");
        root.add(leaderboard, "leaderboard");

        setContentPane(root);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 720));
        setSize(1200, 800);
        setLocationRelativeTo(null);

        showMenu();
    }

    public GameController getController() {
        return controller;
    }

    public void showMenu() {
        cards.show(root, "menu");
    }

    public void showSetup() {
        cards.show(root, "setup");
    }

    public void showLeaderboard() {
        leaderboard.refreshLeaderboard();
        cards.show(root, "leaderboard");
    }

    public void startGame(String name) {
        controller.startGame(name);
        gameplay.refreshAll();
        cards.show(root, "game");
    }

    public void refreshGame() {
        gameplay.refreshAll();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Candy Courier",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void endSession() {
        controller.quitGame();
        dispose();

        SwingUtilities.invokeLater(
            () -> new MainFrame().setVisible(true)
        );
    }
}
