package view.panel;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends JPanel {
    private JTextArea txtMyCourses;
    private JTextArea txtNotices;

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("나의 학습 현황");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // 1. 수강 중인 강의
        txtMyCourses = new JTextArea("수강 목록을 불러오는 중...");
        contentPanel.add(createCard("수강 중인 강의", txtMyCourses, new Color(240, 248, 255)));

        // 2. 최신 공지사항
        txtNotices = new JTextArea("공지사항을 불러오는 중...");
        contentPanel.add(createCard("최신 공지사항", txtNotices, new Color(255, 250, 240)));

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JTextArea textArea, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblTitle.setPreferredSize(new Dimension(0, 40));

        textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(bgColor);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(textArea, BorderLayout.CENTER);
        return card;
    }

    public JTextArea getTxtMyCourses() { return txtMyCourses; }
    public JTextArea getTxtNotices() { return txtNotices; }
}