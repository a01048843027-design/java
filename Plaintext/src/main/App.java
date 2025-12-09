package main;

import controller.LoginController;
import view.LoginFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginView = new LoginFrame();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}