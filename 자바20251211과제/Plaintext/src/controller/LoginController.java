package controller;

import util.DBUtil;
import view.LoginFrame;
import view.MainFrame;
import view.StudentMainFrame;
import controller.StudentMainController; // 학생 컨트롤러가 있다면

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
        // 버튼 클릭 이벤트
        loginView.getBtnLogin().addActionListener(e -> checkLogin());

        // 엔터키 이벤트
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
                        loginView.dispose(); // 로그인 창 닫기
                        new MainController(new MainFrame()); // 교수 메인 실행
                        return;
                    }
                }
            }

            // 2. 학생 테이블 확인
            String stdSql = "SELECT name FROM student WHERE id = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(stdSql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, pw);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        JOptionPane.showMessageDialog(loginView, name + " 학생 환영합니다!");
                        loginView.dispose(); // 로그인 창 닫기
                        // 학생용 메인 실행 (학생 파일이 있다면)
                        new StudentMainController(new StudentMainFrame(), id);
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(loginView, "정보가 일치하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "DB 연결 오류: " + e.getMessage());
        }
    }
}