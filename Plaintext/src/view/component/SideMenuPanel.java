package view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SideMenuPanel extends JPanel {
    private JButton btnDashboard;
    private JButton btnCourseMgmt;
    private JButton btnCalendar;
    private JButton btnAccount;
    private JButton btnLogout;

    public SideMenuPanel() {
        setLayout(new GridLayout(10, 1, 0, 10));
        setBackground(new Color(40, 44, 52));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("학사 관리 시스템", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        add(title);

        btnDashboard = createMenuButton("대시보드");
        btnCourseMgmt = createMenuButton("강의 관리");
        btnCalendar = createMenuButton("캘린더");
        btnAccount = createMenuButton("계정 설정");
        btnLogout = createMenuButton("로그아웃");

        add(btnDashboard);
        add(btnCourseMgmt);
        add(btnCalendar);
        add(btnAccount);
        add(btnLogout);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 63, 65));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        return btn;
    }

    public void addDashboardListener(ActionListener l) { btnDashboard.addActionListener(l); }
    public void addCourseMgmtListener(ActionListener l) { btnCourseMgmt.addActionListener(l); }
    public void addLogoutListener(ActionListener l) { btnLogout.addActionListener(l); }
}