package view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StudentSideMenuPanel extends JPanel {
    private JButton btnDashboard, btnEnrolment, btnMyCourse, btnLogout;

    public StudentSideMenuPanel() {
        setLayout(new GridLayout(10, 1, 0, 10));
        setBackground(new Color(60, 63, 65)); // 교수 모드와 약간 다른 색상 (구분용)
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("학생용 시스템", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        add(title);

        btnDashboard = createMenuButton("대시보드");
        btnEnrolment = createMenuButton("수강신청");
        btnMyCourse = createMenuButton("내 강의실");
        btnLogout = createMenuButton("로그아웃");

        add(btnDashboard);
        add(btnEnrolment);
        add(btnMyCourse);
        add(btnLogout);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(80, 80, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        return btn;
    }

    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); }
    public void addEnrolmentListener(ActionListener l) { btnEnrolment.addActionListener(l); }
    public void addMyCourseListener(ActionListener l) { btnMyCourse.addActionListener(l); }
    public void addLogoutListener(ActionListener l) { btnLogout.addActionListener(l); }
}