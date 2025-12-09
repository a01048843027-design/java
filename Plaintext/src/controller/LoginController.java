package controller;

import view.LoginFrame;
import view.MainFrame;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginController {
    private LoginFrame loginView;

    public LoginController(LoginFrame loginView) {
        this.loginView = loginView;
        initController();
    }

    private void initController() {
        loginView.getBtnLogin().addActionListener(e -> checkLogin());

        loginView.getTxtPw().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkLogin();
                }
            }
        });
    }

    private void checkLogin() {
        String id = loginView.getId();
        String pw = loginView.getPw();

        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "아이디와 비밀번호를 입력하세요.");
            return;
        }

        if ("prof".equals(id) && "1234".equals(pw)) {
            JOptionPane.showMessageDialog(loginView, "교수 모드로 로그인합니다.");

            loginView.dispose();

            MainFrame mainView = new MainFrame();
            new MainController(mainView);
            mainView.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(loginView, "아이디 또는 비밀번호가 잘못되었습니다.\n(Test ID: prof / PW: 1234)", "로그인 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
}