package main;

import com.formdev.flatlaf.FlatLightLaf; // 임포트 추가 (빨간줄 뜨면 Alt+Enter)
import controller.LoginController;
import view.LoginFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // [★디자인 적용 코드] 이 줄 하나면 디자인이 확 바뀝니다!
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginView = new LoginFrame();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}