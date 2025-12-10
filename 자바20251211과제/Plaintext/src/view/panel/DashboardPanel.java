package view.panel;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JTextArea txtTodayCourse;
    private JLabel lblWaitCount;
    private JButton btnQuickCheck;
    private JButton btnNotice;
    private JButton btnApproval;
    private JTextArea txtNoticeList; // [추가] 공지사항 목록을 보여줄 영역

    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("반갑습니다, 김교수님!");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(new Color(245, 245, 245));

        // 1. 오늘의 강의
        txtTodayCourse = new JTextArea("일정을 불러오는 중...");
        contentPanel.add(createCard("오늘의 강의", txtTodayCourse, new Color(255, 255, 255)));

        // 2. 수강신청 승인 대기
        JPanel approvalCard = new JPanel(new BorderLayout());
        approvalCard.setBackground(new Color(255, 240, 240));
        approvalCard.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JLabel lblAppTitle = new JLabel("  수강신청 승인 대기");
        lblAppTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblAppTitle.setPreferredSize(new Dimension(0, 40));

        lblWaitCount = new JLabel("대기 인원: - 명");
        lblWaitCount.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        lblWaitCount.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnApproval = new JButton("바로가기");
        btnApproval.setBackground(Color.PINK);

        approvalCard.add(lblAppTitle, BorderLayout.NORTH);
        approvalCard.add(lblWaitCount, BorderLayout.CENTER);
        approvalCard.add(btnApproval, BorderLayout.SOUTH);
        contentPanel.add(approvalCard);

        // 3. 빠른 출석 체크
        btnQuickCheck = new JButton("이동");
        btnQuickCheck.setBackground(Color.BLUE);
        btnQuickCheck.setForeground(Color.WHITE);
        contentPanel.add(createActionCard("빠른 출석 체크", "현재 진행중인 강의의\n출석부를 엽니다.", btnQuickCheck));

        // 4. [수정됨] 최신 공지사항
        JPanel noticeCard = new JPanel(new BorderLayout());
        noticeCard.setBackground(Color.WHITE);
        noticeCard.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JLabel lblNoticeTitle = new JLabel("  최신 공지사항");
        lblNoticeTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblNoticeTitle.setPreferredSize(new Dimension(0, 40));

        txtNoticeList = new JTextArea("불러오는 중...");
        txtNoticeList.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        txtNoticeList.setEditable(false);
        txtNoticeList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNoticeList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnNotice = new JButton("전체보기 / 관리");
        btnNotice.setBackground(Color.DARK_GRAY);
        btnNotice.setForeground(Color.WHITE);

        noticeCard.add(lblNoticeTitle, BorderLayout.NORTH);
        noticeCard.add(txtNoticeList, BorderLayout.CENTER); // 여기에 목록 표시
        noticeCard.add(btnNotice, BorderLayout.SOUTH);

        contentPanel.add(noticeCard);

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

    private JPanel createActionCard(String title, String desc, JButton btn) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblTitle.setPreferredSize(new Dimension(0, 40));

        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setEditable(false);
        txtDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtDesc.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(txtDesc, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    public JTextArea getTxtTodayCourse() { return txtTodayCourse; }
    public JLabel getLblWaitCount() { return lblWaitCount; }
    public JButton getBtnQuickCheck() { return btnQuickCheck; }
    public JButton getBtnNotice() { return btnNotice; }
    public JButton getBtnApproval() { return btnApproval; }
    public JTextArea getTxtNoticeList() { return txtNoticeList; } // Getter 추가
}