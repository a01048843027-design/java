package main;

import controller.LoginController;
import view.LoginFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // FlatLaf 디자인 설정 제거됨 (기본 디자인으로 실행)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginView = new LoginFrame();
            // 컨트롤러 연결 (기능 작동을 위해 필수)
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}