package controller;

import util.DBUtil;
import view.StudentMainFrame;
import view.LoginFrame;
import view.dialog.StudentNoticeDialog;
import view.panel.StudentDashboardPanel;
import view.panel.StudentEnrolmentPanel;
import view.panel.StudentMyCoursePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class StudentMainController {
    private StudentMainFrame view;
    private String studentId;

    public StudentMainController(StudentMainFrame view, String studentId) {
        this.view = view;
        this.studentId = studentId;

        initController();
        loadDashboardData(); // 시작 시 대시보드 로드

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

        // 로그아웃 로직 (컨트롤러 연결 필수)
        view.getSideMenu().addLogoutListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                view.dispose();
                LoginFrame loginView = new LoginFrame();
                new LoginController(loginView); // 컨트롤러 연결
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

        // --- 4. 대시보드 공지사항 클릭 -> 팝업 오픈 ---
        view.getDashboardPanel().getTxtNotices().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openStudentNoticeDialog();
            }
        });
        view.getDashboardPanel().getTxtNotices().setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    // =========================================================
    //  [기능 1] 대시보드 (시간표 + 타이머 + 요약)
    // =========================================================
    private void loadDashboardData() {
        StudentDashboardPanel dash = view.getDashboardPanel();

        // 1. 시간표 초기화 (내용 비우기)
        for(int r=0; r<9; r++) {
            for(int c=1; c<6; c++) {
                dash.getTimeTable().setValueAt("", r, c);
            }
        }

        StringBuilder myCourses = new StringBuilder();
        int totalAtt = 0, myAtt = 0; // 출석률 계산용

        // 수강 승인된 강의 정보 + 출석 통계 조회
        String sql = "SELECT c.name, c.time, c.room, " +
                "(SELECT count(*) FROM attendance a WHERE a.student_id = ? AND a.attendance_state = '출석') as att_cnt, " +
                "(SELECT count(*) FROM attendance a WHERE a.student_id = ?) as total_cnt " +
                "FROM enrolment e " +
                "JOIN course c ON e.course_code = c.code " +
                "WHERE e.student_id = ? AND e.status = '승인'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, studentId);
            pstmt.setString(3, studentId);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                String name = rs.getString("name");
                String timeStr = rs.getString("time"); // 예: "월 09:00 ~ 12:00"
                String room = rs.getString("room");

                // 통계 누적 (첫 번째 행에서만 가져오면 됨 - 전체 통계이므로)
                if (rs.isFirst()) {
                    myAtt = rs.getInt("att_cnt");
                    totalAtt = rs.getInt("total_cnt");
                }

                // 하단 리스트에 추가
                myCourses.append("■ ").append(name).append(" (").append(room).append(")\n");

                // ★ 시간표 파싱 및 배치 로직
                if (timeStr != null && timeStr.length() > 2) {
                    String day = timeStr.substring(0, 1); // "월"
                    String startHourStr = timeStr.substring(2, 4); // "09"

                    int col = -1;
                    switch(day) {
                        case "월": col = 1; break; case "화": col = 2; break;
                        case "수": col = 3; break; case "목": col = 4; break; case "금": col = 5; break;
                    }

                    try {
                        int startHour = Integer.parseInt(startHourStr);
                        int period = startHour - 8; // 9시 -> 1교시

                        if (col != -1 && period >= 1 && period <= 9) {
                            dash.getTimeTable().setValueAt(name, period - 1, col);
                            // 2시간 연강 가정 (다음 교시도 채움)
                            if (period < 9) dash.getTimeTable().setValueAt(name, period, col);
                        }
                    } catch (NumberFormatException e) { /* 시간 포맷 예외 무시 */ }
                }
            }

            dash.getTxtMyCourses().setText(myCourses.length() == 0 ? "수강 승인된 강의 없음" : myCourses.toString());

            // 2. 출석률 요약 표시 (하얀 상자)
            if (totalAtt > 0) {
                int rate = (int)((double)myAtt / totalAtt * 100);
                dash.getLblAttendanceSummary().setText("출석률: " + rate + "% (" + myAtt + "/" + totalAtt + ")");
            } else {
                dash.getLblAttendanceSummary().setText("아직 출석 기록이 없습니다.");
            }

            // 3. 다음 수업 타이머 (노란 상자) - 더미 데이터
            dash.getLblNextClassTimer().setText("D-Day: 중간고사 2주 전");

        } catch(Exception e) { e.printStackTrace(); }

        // 4. 공지사항 로드
        StringBuilder notices = new StringBuilder();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title, created_at FROM notice ORDER BY id DESC LIMIT 2")) {
            while(rs.next()) {
                notices.append("• ").append(rs.getString("title")).append("\n");
            }
            if(notices.length() == 0) notices.append("등록된 공지가 없습니다.");
            dash.getTxtNotices().setText(notices.toString());
        } catch(Exception e) { e.printStackTrace(); }
    }

    // =========================================================
    //  [기능 2] 공지사항 읽기 전용 팝업
    // =========================================================
    private void openStudentNoticeDialog() {
        StudentNoticeDialog dialog = new StudentNoticeDialog(view);
        DefaultTableModel model = dialog.getModel();

        String sql = "SELECT id, title, created_at FROM notice ORDER BY id DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) model.addRow(new Object[]{rs.getInt("id"), rs.getString("title"), rs.getTimestamp("created_at")});
        } catch(Exception e) { e.printStackTrace(); }

        dialog.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = dialog.getTable().getSelectedRow(); if(row == -1) return;
                int id = (int) model.getValueAt(row, 0);
                try (Connection conn = DBUtil.getConnection()) {
                    PreparedStatement pstmt = conn.prepareStatement("SELECT title, content FROM notice WHERE id = ?");
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
    //  [기능 3] 수강신청 목록 (정원 마감 로직 포함)
    // =========================================================
    private void loadEnrolmentList() {
        StudentEnrolmentPanel panel = view.getEnrolmentPanel();
        DefaultTableModel model = panel.getModel();
        model.setRowCount(0);

        String sql = "SELECT c.code, c.name, c.time, c.room, c.capacity, " +
                "(SELECT COUNT(*) FROM enrolment e2 WHERE e2.course_code = c.code AND e2.status = '승인') as current_cnt, " +
                "(SELECT status FROM enrolment e WHERE e.course_code = c.code AND e.student_id = ?) as my_status " +
                "FROM course c";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                int cap = rs.getInt("capacity");
                int current = rs.getInt("current_cnt");
                String myStatus = rs.getString("my_status");

                if (myStatus == null) {
                    if (current >= cap) myStatus = "마감";
                    else myStatus = "신청가능";
                }

                model.addRow(new Object[]{
                        code, name, rs.getString("time"), rs.getString("room"),
                        current + " / " + cap + "명", myStatus
                });
            }
        } catch(SQLException e) { e.printStackTrace(); }
    }

    private void applyCourse() {
        StudentEnrolmentPanel panel = view.getEnrolmentPanel();
        int row = panel.getTable().getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(view, "강의를 선택하세요."); return; }

        String code = (String) panel.getModel().getValueAt(row, 0);
        String status = (String) panel.getModel().getValueAt(row, 5);

        if ("마감".equals(status)) {
            JOptionPane.showMessageDialog(view, "정원이 마감된 강의입니다.", "신청 불가", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!"신청가능".equals(status)) {
            JOptionPane.showMessageDialog(view, "이미 신청했거나 처리된 강의입니다.");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO enrolment (course_code, student_id, status) VALUES (?, ?, '대기')");
            pstmt.setString(1, code); pstmt.setString(2, studentId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(view, "신청되었습니다. (승인 대기)");
            loadEnrolmentList();
        } catch(SQLException e) { JOptionPane.showMessageDialog(view, "신청 실패: " + e.getMessage()); }
    }

    // =========================================================
    //  [기능 4] 내 강의실 (출석 현황)
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
}