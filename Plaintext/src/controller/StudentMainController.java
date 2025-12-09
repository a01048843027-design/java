package controller;

import util.DBUtil;
import view.StudentMainFrame;
import view.LoginFrame;
import view.panel.StudentDashboardPanel;
import view.panel.StudentEnrolmentPanel;
import view.panel.StudentMyCoursePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentMainController {
    private StudentMainFrame view;
    private String studentId;

    public StudentMainController(StudentMainFrame view, String studentId) {
        this.view = view;
        this.studentId = studentId;

        initController();
        loadDashboardData();

        view.setVisible(true);
    }

    private void initController() {
        // --- 1. 메뉴 이동 ---
        view.getSideMenu().addDashboardListener(e -> {
            view.showCard("DASHBOARD");
            loadDashboardData();
        });
        view.getSideMenu().addEnrolmentListener(e -> {
            view.showCard("ENROLMENT");
            loadEnrolmentList();
        });
        view.getSideMenu().addMyCourseListener(e -> {
            view.showCard("MY_COURSE");
            loadMyCourseList();
        });

        // [★수정된 부분] 로그아웃 로직
        view.getSideMenu().addLogoutListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                view.dispose(); // 현재 학생 창 닫기

                // 1. 로그인 창 생성 (변수에 담아둠)
                LoginFrame loginView = new LoginFrame();
                // 2. 컨트롤러에 연결 (기능 활성화)
                new LoginController(loginView);
                // 3. 화면 표시
                loginView.setVisible(true);
            }
        });

        // --- 2. 수강신청 화면 기능 ---
        StudentEnrolmentPanel enrolPanel = view.getEnrolmentPanel();
        enrolPanel.getBtnRefresh().addActionListener(e -> loadEnrolmentList());
        enrolPanel.getBtnApply().addActionListener(e -> applyCourse());

        // --- 3. 내 강의실 화면 기능 ---
        StudentMyCoursePanel myPanel = view.getMyCoursePanel();
        myPanel.getBtnRefresh().addActionListener(e -> {
            loadMyCourseList();
            JOptionPane.showMessageDialog(view, "최신 정보로 갱신되었습니다.");
        });

        // 대시보드 공지사항 클릭 -> 팝업 오픈
        view.getDashboardPanel().getTxtNotices().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openStudentNoticeDialog();
            }
        });
        view.getDashboardPanel().getTxtNotices().setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    // =========================================================
    //  [기능 1] 대시보드
    // =========================================================
    private void loadDashboardData() {
        StudentDashboardPanel dash = view.getDashboardPanel();

        StringBuilder myCourses = new StringBuilder();
        String sql = "SELECT c.name, c.time, c.room FROM enrolment e JOIN course c ON e.course_code = c.code WHERE e.student_id = ? AND e.status = '승인'";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                myCourses.append("■ ").append(rs.getString("name")).append("\n   ").append(rs.getString("time")).append(" (").append(rs.getString("room")).append(")\n\n");
            }
            if(myCourses.length() == 0) myCourses.append("현재 수강 승인된 강의가 없습니다.");
            dash.getTxtMyCourses().setText(myCourses.toString());
        } catch(Exception e) { e.printStackTrace(); }

        StringBuilder notices = new StringBuilder();
        String sqlNotice = "SELECT title, created_at FROM notice ORDER BY id DESC LIMIT 4";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlNotice)) {
            while(rs.next()) {
                notices.append("• ").append(rs.getString("title")).append("  [").append(rs.getDate("created_at")).append("]\n\n");
            }
            if(notices.length() == 0) notices.append("등록된 공지사항이 없습니다.");
            dash.getTxtNotices().setText(notices.toString());
        } catch(Exception e) { e.printStackTrace(); }
    }

    // =========================================================
    //  [기능 2] 공지사항 팝업
    // =========================================================
    private void openStudentNoticeDialog() {
        view.dialog.StudentNoticeDialog dialog = new view.dialog.StudentNoticeDialog(view);
        DefaultTableModel model = dialog.getModel();

        String sql = "SELECT id, title, created_at FROM notice ORDER BY id DESC";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) model.addRow(new Object[]{rs.getInt("id"), rs.getString("title"), rs.getTimestamp("created_at")});
        } catch(Exception e) { e.printStackTrace(); }

        dialog.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = dialog.getTable().getSelectedRow();
                if(row == -1) return;
                int id = (int) model.getValueAt(row, 0);
                try (Connection conn = DBUtil.getConnection()) {
                    String q = "SELECT title, content FROM notice WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(q);
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next()) dialog.setViewData(rs.getString("title"), rs.getString("content"));
                } catch(Exception ex) { ex.printStackTrace(); }
            }
        });
        dialog.getBtnClose().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // =========================================================
    //  [기능 3] 내 강의실
    // =========================================================
    private void loadMyCourseList() {
        StudentMyCoursePanel panel = view.getMyCoursePanel();
        DefaultTableModel model = panel.getModel();
        model.setRowCount(0);

        String sql = "SELECT c.name, a.attendance_state, a.note FROM attendance a JOIN course c ON a.course_code = c.code WHERE a.student_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String state = rs.getString("attendance_state"); if (state == null) state = "미처리";
                String note = rs.getString("note"); if (note == null) note = "";
                model.addRow(new Object[]{ rs.getString("name"), state, note });
            }
        } catch(SQLException e) { e.printStackTrace(); }
    }

    // =========================================================
    //  [기능 4] 수강신청
    // =========================================================
    private void loadEnrolmentList() {
        StudentEnrolmentPanel panel = view.getEnrolmentPanel();
        DefaultTableModel model = panel.getModel();
        model.setRowCount(0);

        String sql = "SELECT c.code, c.name, c.time, c.room, c.capacity, (SELECT status FROM enrolment e WHERE e.course_code = c.code AND e.student_id = ?) as my_status FROM course c";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String status = rs.getString("my_status"); if (status == null) status = "신청가능";
                model.addRow(new Object[]{ rs.getString("code"), rs.getString("name"), rs.getString("time"), rs.getString("room"), rs.getInt("capacity") + "명", status });
            }
        } catch(SQLException e) { e.printStackTrace(); }
    }

    private void applyCourse() {
        StudentEnrolmentPanel panel = view.getEnrolmentPanel();
        int row = panel.getTable().getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(view, "강의를 선택하세요."); return; }

        String code = (String) panel.getModel().getValueAt(row, 0);
        String status = (String) panel.getModel().getValueAt(row, 5);

        if (!"신청가능".equals(status)) { JOptionPane.showMessageDialog(view, "이미 신청했거나 처리된 강의입니다."); return; }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO enrolment (course_code, student_id, status) VALUES (?, ?, '대기')");
            pstmt.setString(1, code); pstmt.setString(2, studentId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(view, "신청되었습니다.");
            loadEnrolmentList();
        } catch(SQLException e) { JOptionPane.showMessageDialog(view, "신청 실패: " + e.getMessage()); }
    }
}