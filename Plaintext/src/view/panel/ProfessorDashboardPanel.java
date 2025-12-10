package view.panel;

import manager.CourseManager;
import model.Course;
import util.DBUtil;
import view.dialog.NoticeDialog; // ★ 가지고 계신 파일 import

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProfessorDashboardPanel extends JPanel {

    private JTextArea txtNoticePreview;

    public ProfessorDashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("교수 대시보드");
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(new Color(245, 247, 250));

        // --- 1. 상단 통계 카드 ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setPreferredSize(new Dimension(0, 100));

        statsPanel.add(createStatCard("담당 강의", "DB 확인중...", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("학생 수", "DB 확인중...", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("학기 진행", "8주차 (중간)", new Color(155, 89, 182)));

        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // --- 2. 하단 영역 (왼쪽: 강의목록 / 오른쪽: 공지사항) ---
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBackground(new Color(245, 247, 250));

        // [왼쪽] 담당 강의 목록
        JPanel courseCard = new JPanel(new BorderLayout());
        courseCard.setBackground(Color.WHITE);
        courseCard.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));

        JLabel lblCourse = new JLabel("  담당 강의 목록");
        lblCourse.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblCourse.setPreferredSize(new Dimension(0, 40));
        courseCard.add(lblCourse, BorderLayout.NORTH);

        String[] headers = {"코드", "강의명", "시간", "강의실"};
        DefaultTableModel courseModel = new DefaultTableModel(headers, 0);
        JTable courseTable = new JTable(courseModel);
        courseTable.setRowHeight(25);
        courseTable.setEnabled(false); // 보기 전용

        loadCourses(courseModel); // DB 로드

        courseCard.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        // [오른쪽] ★ 공지사항 (복구된 부분)
        JPanel noticeCard = new JPanel(new BorderLayout());
        noticeCard.setBackground(Color.WHITE);
        noticeCard.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));

        // 제목 + 관리 버튼 패널
        JPanel noticeHeader = new JPanel(new BorderLayout());
        noticeHeader.setBackground(Color.WHITE);

        JLabel lblNotice = new JLabel("  최신 공지사항");
        lblNotice.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblNotice.setPreferredSize(new Dimension(0, 40));

        // ★ 공지사항 관리 버튼
        JButton btnManageNotice = new JButton("공지 관리 (추가/삭제)");
        btnManageNotice.setBackground(new Color(240, 240, 240));
        btnManageNotice.setFocusPainted(false);
        btnManageNotice.addActionListener(e -> openNoticeDialog()); // 클릭 시 팝업

        noticeHeader.add(lblNotice, BorderLayout.WEST);
        noticeHeader.add(btnManageNotice, BorderLayout.EAST);

        noticeCard.add(noticeHeader, BorderLayout.NORTH);

        // 공지 내용 미리보기 영역
        txtNoticePreview = new JTextArea();
        txtNoticePreview.setEditable(false);
        txtNoticePreview.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        txtNoticePreview.setLineWrap(true);
        txtNoticePreview.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loadNoticePreview(); // DB 로드

        noticeCard.add(new JScrollPane(txtNoticePreview), BorderLayout.CENTER);

        bottomPanel.add(courseCard);
        bottomPanel.add(noticeCard);

        contentPanel.add(bottomPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    // --- 기능 메서드 ---

    // 1. 공지사항 관리 팝업 열기 (가지고 계신 NoticeDialog 연결)
    private void openNoticeDialog() {
        // 현재 창의 부모 프레임을 찾아서 다이얼로그의 부모로 설정
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow instanceof Frame) {
            NoticeDialog dialog = new NoticeDialog((Frame) parentWindow);
            dialog.setVisible(true);

            // 팝업이 닫히면 미리보기 갱신 (추가/삭제된 내용 반영)
            loadNoticePreview();
        }
    }

    // 2. 대시보드용 공지사항 미리보기 (최신 3개만)
    private void loadNoticePreview() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT title, created_at FROM notice ORDER BY id DESC LIMIT 3";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean hasData = false;
            while(rs.next()) {
                hasData = true;
                String date = rs.getString("created_at").substring(0, 10); // 날짜만 자르기
                sb.append("📢 ").append(rs.getString("title"))
                        .append("  (").append(date).append(")\n\n");
            }

            if (!hasData) sb.append("\n  등록된 공지사항이 없습니다.\n  [공지 관리] 버튼을 눌러 추가하세요.");

        } catch (Exception e) {
            e.printStackTrace();
            sb.append("DB 연결 오류");
        }
        txtNoticePreview.setText(sb.toString());
    }

    // 3. 강의 목록 로드
    private void loadCourses(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT course_code, subject_name, class_time, room FROM courses";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("course_code"),
                        rs.getString("subject_name"),
                        rs.getString("class_time"),
                        rs.getString("room")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 통계 카드 생성 헬퍼
    private JPanel createStatCard(String title, String value, Color pointColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, pointColor));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        lblValue.setForeground(new Color(50, 50, 50));
        lblValue.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 0));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
}