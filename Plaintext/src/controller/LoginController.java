package controller;

import util.DBUtil;
import view.LoginFrame;
import view.MainFrame;
import view.StudentMainFrame; // 학생용 메인 화면 임포트

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    private LoginFrame loginView;

    public LoginController(LoginFrame loginView) {
        this.loginView = loginView;
        initController();
    }

    private void initController() {
        // 로그인 버튼 클릭 시
        loginView.getBtnLogin().addActionListener(e -> checkLogin());

        // 비밀번호 창에서 엔터 키 누를 시
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

        try (Connection conn = DBUtil.getConnection()) {
            // 1. 교수 테이블 확인
            String profSql = "SELECT name FROM professor WHERE id = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(profSql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, pw);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        JOptionPane.showMessageDialog(loginView, name + " 교수님 환영합니다!");
                        loginView.dispose();
                        new MainController(new MainFrame()); // 교수용 메인 실행
                        return;
                    }
                }
            }

            // 2. 학생 테이블 확인 (교수가 아닐 경우)
            String stdSql = "SELECT name FROM student WHERE id = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(stdSql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, pw);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        JOptionPane.showMessageDialog(loginView, name + " 학생 환영합니다!");
                        loginView.dispose();
                        // 학생용 메인 실행 (로그인한 학번 ID를 넘겨줌)
                        new StudentMainController(new StudentMainFrame(), id);
                        return;
                    }
                }
            }

            // 둘 다 아니면 실패
            JOptionPane.showMessageDialog(loginView, "아이디 또는 비밀번호를 확인하세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "DB 연결 오류: " + e.getMessage());
        }
    }
}