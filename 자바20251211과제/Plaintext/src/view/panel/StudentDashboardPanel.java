package view.panel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentDashboardPanel extends JPanel {

    private JTextArea txtNotices;
    private JTable timeTable;
    private JTextArea txtMyCourses;
    private JLabel lblAttendanceSummary;
    private JLabel lblNextClassTimer;
    private JButton btnRefresh;

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // 배경: 아주 연한 회색 (눈 편안)
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 상단 헤더 (환영 문구 + 새로고침)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("안녕하세요, 학생님! 오늘도 활기찬 하루 되세요.");
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80)); // 진한 남색

        btnRefresh = new JButton("새로고침");
        styleButton(btnRefresh);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. 중앙 콘텐츠 (시간표 + 정보 패널)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(new Color(245, 247, 250));

        // [왼쪽] 시간표 카드
        JPanel scheduleCard = createCardPanel("이번 주 시간표");
        String[] columns = {"시간", "월", "화", "수", "목", "금"};
        DefaultTableModel model = new DefaultTableModel(columns, 9);
        timeTable = new JTable(model);
        styleTable(timeTable);

        // 시간 채우기 (1교시~9교시)
        for(int i=0; i<9; i++) model.setValueAt((i+1)+"교시", i, 0);

        JScrollPane scrollTable = new JScrollPane(timeTable);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());
        scrollTable.getViewport().setBackground(Color.WHITE);
        scheduleCard.add(scrollTable, BorderLayout.CENTER);

        // [오른쪽] 정보 카드 (내 강의 + 공지 + 출석)
        JPanel infoCard = new JPanel(new GridLayout(2, 1, 0, 20));
        infoCard.setBackground(new Color(245, 247, 250));

        // 내 강의 목록
        JPanel myCoursePanel = createCardPanel("내 수강 목록");
        txtMyCourses = new JTextArea();
        txtMyCourses.setEditable(false);
        txtMyCourses.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        myCoursePanel.add(new JScrollPane(txtMyCourses), BorderLayout.CENTER);

        // 하단 (공지 + 출석)
        JPanel bottomInfoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomInfoPanel.setBackground(new Color(245, 247, 250));

        JPanel noticePanel = createCardPanel("공지사항");
        txtNotices = new JTextArea("등록된 공지사항이 없습니다.");
        txtNotices.setEditable(false);
        noticePanel.add(new JScrollPane(txtNotices), BorderLayout.CENTER);

        JPanel attendancePanel = createCardPanel("오늘의 출석");
        lblAttendanceSummary = new JLabel("출석 확인 중...", SwingConstants.CENTER);
        lblAttendanceSummary.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        lblAttendanceSummary.setForeground(new Color(39, 174, 96)); // 녹색
        attendancePanel.add(lblAttendanceSummary, BorderLayout.CENTER);

        // (필수) 컨트롤러 연결용 타이머 라벨 (보이지 않게 숨겨둠 or 작게 배치)
        lblNextClassTimer = new JLabel("");

        bottomInfoPanel.add(noticePanel);
        bottomInfoPanel.add(attendancePanel);

        infoCard.add(myCoursePanel);
        infoCard.add(bottomInfoPanel);

        centerPanel.add(scheduleCard);
        centerPanel.add(infoCard);

        add(centerPanel, BorderLayout.CENTER);
    }

    // UI 헬퍼 메서드: 카드 스타일 패널 생성
    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true), // 둥근 테두리 느낌
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        table.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(52, 152, 219)); // 밝은 파랑
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    // --- Getter (Controller 연동용) ---
    public JTextArea getTxtNotices() { return txtNotices; }
    public JTable getTimeTable() { return timeTable; }
    public JTextArea getTxtMyCourses() { return txtMyCourses; }
    public JLabel getLblAttendanceSummary() { return lblAttendanceSummary; }
    public JLabel getLblNextClassTimer() { return lblNextClassTimer; }
    public JButton getBtnRefresh() { return btnRefresh; }
}