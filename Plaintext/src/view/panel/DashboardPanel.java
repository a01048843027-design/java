package view.panel;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("반갑습니다, 김교수님!");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(new Color(245, 245, 245));

        contentPanel.add(createCard("오늘의 강의", "JAVA101 - 10:00\n데이터베이스 - 14:00", new Color(255, 255, 255)));
        contentPanel.add(createCard("수강신청 승인 대기", "승인 대기 인원: 5명\n(바로가기)", new Color(255, 240, 240)));
        contentPanel.add(createActionCard("빠른 출석 체크", "현재 진행중인 강의의\n출석부를 엽니다.", Color.BLUE));
        contentPanel.add(createActionCard("공지사항 작성", "전체 또는 강의별\n공지를 등록합니다.", Color.DARK_GRAY));

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String content, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblTitle.setPreferredSize(new Dimension(0, 40));

        JTextArea txtContent = new JTextArea(content);
        txtContent.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        txtContent.setEditable(false);
        txtContent.setBackground(bgColor);
        txtContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(txtContent, BorderLayout.CENTER);
        return card;
    }

    private JPanel createActionCard(String title, String desc, Color btnColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblTitle.setPreferredSize(new Dimension(0, 40));

        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setEditable(false);
        txtDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnGo = new JButton("이동");
        btnGo.setBackground(btnColor);
        btnGo.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(txtDesc, BorderLayout.CENTER);
        card.add(btnGo, BorderLayout.SOUTH);
        return card;
    }
}