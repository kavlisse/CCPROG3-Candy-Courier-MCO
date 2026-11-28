
package view;

import java.awt.*;
import javax.swing.*;

import controller.*;
import model.*;
import persistence.LeaderboardFileHandler;

public class MainFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final GameController controller;
    private final GamePanel gameplay;
    private final LeaderboardPanel leaderboard;

    public MainFrame() {
        super("Candy Courier");

        LeaderboardController lc = new LeaderboardController(
            new Leaderboard(),
            new LeaderboardFileHandler("leaderboard.txt")
        );

        controller = new GameController(new Game(), lc);

        MainMenuPanel menu = new MainMenuPanel(this);
        PlayerSetupPanel setup = new PlayerSetupPanel(this);

        gameplay = new GamePanel(this, controller);

        leaderboard = new LeaderboardPanel(
            controller.getLeaderboardController(),
            this::showMenu
        );

        root.add(menu, "menu");
        root.add(setup, "setup");
        root.add(gameplay, "game");
        root.add(leaderboard, "leaderboard");

        root.setBackground(Ui.BACKGROUND);

        setContentPane(root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        gameplay.showWorld();
        cards.show(root, "game");
    }

    public void showMessage(String message) {
        Ui.message(this, message);
    }

    public void endSession() {
        controller.quitGame();
        dispose();

        SwingUtilities.invokeLater(() ->
            new MainFrame().setVisible(true)
        );
    }
}
