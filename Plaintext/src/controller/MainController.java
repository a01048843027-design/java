package controller;

import util.DBUtil;
import view.MainFrame;
import view.LoginFrame;
import view.dialog.AddCourseDialog;
import view.dialog.ApprovalDialog;
import view.dialog.NoticeDialog;
import view.panel.CourseListPanel;
import view.panel.AttendanceContainerPanel;
import view.panel.DashboardPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MainController {
    private MainFrame view;
    private CourseListPanel coursePanel;
    private AttendanceContainerPanel attendancePanel;

    // 현재 작업 중인 강의 코드 저장용
    private String currentCourseCode = "";

    public MainController(MainFrame view) {
        this.view = view;
        coursePanel = new CourseListPanel();
        attendancePanel = new AttendanceContainerPanel();

        view.getMainContainer().add(coursePanel, "COURSE_LIST");
        view.getMainContainer().add(attendancePanel, "ATTENDANCE");

        initController();

        // 프로그램 시작 시 데이터 로드
        loadCourseList();
        loadDashboardData();

        view.setVisible(true);
    }

    private void initController() {
        // --- 1. 메뉴 이동 및 로그아웃 ---
        view.getSideMenu().addDashboardListener(e -> {
            view.showCard("DASHBOARD");
            loadDashboardData();
        });
        view.getSideMenu().addCourseMgmtListener(e -> {
            view.showCard("COURSE_LIST");
            loadCourseList();
        });
        view.getSideMenu().addLogoutListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                view.dispose(); // 현재 창 닫기

                // 1. 로그인 창 생성
                LoginFrame loginView = new LoginFrame();
                // 2. ★컨트롤러 연결★ (이게 있어야 버튼이 작동함)
                new LoginController(loginView);
                // 3. 화면 표시
                loginView.setVisible(true);
            }
        });

        // --- 2. 대시보드 버튼 및 클릭 이벤트 연결 ---
        DashboardPanel dash = view.getDashboardPanel();

        // 승인 대기 버튼
        dash.getBtnApproval().addActionListener(e -> openApprovalDialog());

        // 공지사항 관리 버튼 (하단 버튼)
        dash.getBtnNotice().addActionListener(e -> openNoticeDialog());

        // [★추가된 기능] 공지사항 텍스트 목록을 클릭해도 게시판 열기
        dash.getTxtNoticeList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openNoticeDialog(); // 게시판 팝업 열기
            }
        });

        // 빠른 출석 체크 버튼
        dash.getBtnQuickCheck().addActionListener(e -> {
            view.showCard("COURSE_LIST");
            loadCourseList();
            JOptionPane.showMessageDialog(view, "출석할 강의를 선택해주세요.");
        });

        // --- 3. 강의 관리 기능 ---
        coursePanel.getBtnAddCourse().addActionListener(e -> openAddCourseDialog());
        coursePanel.getBtnDeleteCourse().addActionListener(e -> deleteSelectedCourse());

        coursePanel.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    if(row != -1) {
                        currentCourseCode = (String) target.getValueAt(row, 0);
                        view.showCard("ATTENDANCE");
                        loadAttendanceData(currentCourseCode);
                    }
                }
            }
        });

        // --- 4. 출석부 기능 ---
        attendancePanel.getBtnBack().addActionListener(e -> {
            view.showCard("COURSE_LIST");
            loadCourseList();
        });
        attendancePanel.getControlPanel().addSaveListener(e -> saveAttendanceData());
        attendancePanel.getControlPanel().addBatchListener(e -> processBatchAttendance());
        attendancePanel.getControlPanel().addResetListener(e -> resetAllToUnprocessed());

        // [★추가된 코드] 테이블 값이 바뀌면 실시간으로 통계 업데이트!
        attendancePanel.getTablePanel().getModel().addTableModelListener(e -> {
            updateSummary();
        });
    }

    // =========================================================
    //  [기능 1] 수강신청 승인 (체크박스 다중 처리 적용)
    // =========================================================
    public void openApprovalDialog() {
        ApprovalDialog dialog = new ApprovalDialog(view);
        DefaultTableModel model = dialog.getModel();

        // 대기중인 목록 불러오기 (컬럼 인덱스: 0=체크박스, 1=ID, 2=강의명, 3=학번, 4=이름, 5=학과, 6=상태)
        String sql = "SELECT e.id, c.name, s.id, s.name, s.major, e.status " +
                "FROM enrolment e " +
                "JOIN course c ON e.course_code = c.code " +
                "JOIN student s ON e.student_id = s.id " +
                "WHERE e.status = '대기'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()) {
                // 맨 앞에 'false'(체크박스 미선택) 추가
                model.addRow(new Object[]{
                        false,
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6)
                });
            }
        } catch(SQLException e) { e.printStackTrace(); }

        // [전체 선택] 버튼
        dialog.getBtnSelectAll().addActionListener(e -> {
            for(int i=0; i<model.getRowCount(); i++) model.setValueAt(true, i, 0);
        });

        // [전체 해제] 버튼
        dialog.getBtnUnselectAll().addActionListener(e -> {
            for(int i=0; i<model.getRowCount(); i++) model.setValueAt(false, i, 0);
        });

        // [일괄 승인] 버튼
        dialog.getBtnApprove().addActionListener(e -> {
            int approvedCount = 0;

            try (Connection conn = DBUtil.getConnection()) {
                // 테이블의 모든 행을 거꾸로 검사 (삭제 시 인덱스 문제 방지)
                for (int i = model.getRowCount() - 1; i >= 0; i--) {
                    boolean isChecked = (boolean) model.getValueAt(i, 0); // 0번열 체크여부

                    if (isChecked) {
                        int enrollId = (int) model.getValueAt(i, 1);   // 1번열: 신청ID
                        String stdId = (String) model.getValueAt(i, 3); // 3번열: 학번

                        // 1. 강의 코드 찾기
                        String findCode = "SELECT course_code FROM enrolment WHERE id=?";
                        PreparedStatement p1 = conn.prepareStatement(findCode);
                        p1.setInt(1, enrollId);
                        ResultSet r1 = p1.executeQuery();
                        String cCode = "";
                        if(r1.next()) cCode = r1.getString(1);

                        // 2. 상태 변경 (승인)
                        String upSql = "UPDATE enrolment SET status='승인' WHERE id=?";
                        PreparedStatement p2 = conn.prepareStatement(upSql);
                        p2.setInt(1, enrollId);
                        p2.executeUpdate();

                        // 3. 출석부 추가 (기존 학생 정보 + 미처리 상태)
                        String insSql = "INSERT INTO attendance (course_code, student_id, attendance_state) VALUES (?, ?, '미처리')";
                        PreparedStatement p3 = conn.prepareStatement(insSql);
                        p3.setString(1, cCode);
                        p3.setString(2, stdId);
                        p3.executeUpdate();

                        model.removeRow(i); // 목록에서 제거
                        approvedCount++;
                    }
                }

                if (approvedCount > 0) {
                    JOptionPane.showMessageDialog(dialog, approvedCount + "명이 승인 처리되었습니다.");
                    loadDashboardData(); // 대시보드 대기 인원 갱신
                } else {
                    JOptionPane.showMessageDialog(dialog, "선택된 학생이 없습니다.");
                }

            } catch(SQLException ex) { ex.printStackTrace(); }
        });

        // [일괄 거절] 버튼
        dialog.getBtnReject().addActionListener(e -> {
            int rejectedCount = 0;
            try (Connection conn = DBUtil.getConnection()) {
                for (int i = model.getRowCount() - 1; i >= 0; i--) {
                    boolean isChecked = (boolean) model.getValueAt(i, 0);

                    if (isChecked) {
                        int enrollId = (int) model.getValueAt(i, 1);
                        String sql2 = "UPDATE enrolment SET status='거절' WHERE id=?";
                        PreparedStatement pstmt = conn.prepareStatement(sql2);
                        pstmt.setInt(1, enrollId);
                        pstmt.executeUpdate();
                        model.removeRow(i);
                        rejectedCount++;
                    }
                }
                if (rejectedCount > 0) {
                    JOptionPane.showMessageDialog(dialog, rejectedCount + "명이 거절 처리되었습니다.");
                    loadDashboardData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "선택된 학생이 없습니다.");
                }
            } catch(SQLException ex) { ex.printStackTrace(); }
        });

        dialog.getBtnClose().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // =========================================================
    //  [기능 2] 대시보드 데이터 로드 (공지사항 미리보기 추가됨)
    // =========================================================
    private void loadDashboardData() {
        DashboardPanel dash = view.getDashboardPanel();
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        String today = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        // 1. 오늘의 강의
        StringBuilder todayCourses = new StringBuilder();
        String sqlCourse = "SELECT name, time, room FROM course WHERE time LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlCourse)) {
            pstmt.setString(1, "%" + today + "%");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                todayCourses.append("■ ").append(rs.getString("name")).append("\n   ")
                        .append(rs.getString("time")).append(" (").append(rs.getString("room")).append(")\n\n");
            }
            dash.getTxtTodayCourse().setText(todayCourses.length() == 0 ? "오늘 예정된 강의가 없습니다." : todayCourses.toString());
        } catch(SQLException e) { e.printStackTrace(); }

        // 2. 승인 대기 인원
        String sqlWait = "SELECT count(*) FROM enrolment WHERE status = '대기'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlWait);
             ResultSet rs = pstmt.executeQuery()) {
            if(rs.next()) {
                int count = rs.getInt(1);
                dash.getLblWaitCount().setText("대기 인원: " + count + " 명");
                dash.getLblWaitCount().setForeground(count > 0 ? Color.RED : Color.BLACK);
                dash.getBtnApproval().setText(count > 0 ? "승인하러 가기 (" + count + ")" : "대기 내역 없음");
            }
        } catch(SQLException e) { e.printStackTrace(); }

        // 3. [추가됨] 최신 공지사항 미리보기 (최근 4개만)
        StringBuilder noticeList = new StringBuilder();
        String sqlNotice = "SELECT title, created_at FROM notice ORDER BY id DESC LIMIT 4";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlNotice);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                // 날짜 포맷 (yyyy-MM-dd)
                String date = rs.getTimestamp("created_at").toLocalDateTime().toLocalDate().toString();
                String title = rs.getString("title");

                noticeList.append("• ").append(title)
                        .append("  [").append(date).append("]\n\n");
            }

            if (noticeList.length() == 0) {
                dash.getTxtNoticeList().setText("등록된 공지사항이 없습니다.");
            } else {
                dash.getTxtNoticeList().setText(noticeList.toString());
            }

        } catch(SQLException e) {
            e.printStackTrace();
            dash.getTxtNoticeList().setText("공지사항 로드 실패");
        }
    }

    // =========================================================
    //  [기능 3] 공지사항 관리 (조회, 등록, 상세, 삭제 완벽 구현)
    // =========================================================
    public void openNoticeDialog() {
        NoticeDialog dialog = new NoticeDialog(view);
        DefaultTableModel model = dialog.getModel();

        // 1. 목록 로드 (내부 메서드로 분리하여 재사용)
        loadNoticeList(model);

        // 2. [등록] 버튼 이벤트
        dialog.getBtnAdd().addActionListener(e -> {
            String title = dialog.getTitleText();
            String content = dialog.getContentText();

            if(title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "제목과 내용을 모두 입력해주세요.");
                return;
            }

            try (Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO notice (title, content) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(dialog, "공지사항이 등록되었습니다.");
                dialog.clearInput(); // 입력창 비우기
                loadNoticeList(model); // 목록 새로고침

            } catch(SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "등록 실패: " + ex.getMessage());
            }
        });

        // 3. [테이블 클릭] 상세 내용 보기 이벤트
        dialog.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = dialog.getTable().getSelectedRow();
                if (row == -1) return;

                // 선택된 행의 ID 가져오기 (0번 컬럼)
                int id = (int) model.getValueAt(row, 0);

                // DB에서 상세 내용(content) 가져오기
                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "SELECT title, content FROM notice WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        // 가져온 데이터를 오른쪽 입력창에 뿌려주기
                        dialog.setTitleText(rs.getString("title"));
                        dialog.setContentText(rs.getString("content"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 4. [삭제] 버튼 이벤트
        dialog.getBtnDelete().addActionListener(e -> {
            int row = dialog.getTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "삭제할 공지사항을 목록에서 선택해주세요.");
                return;
            }

            int id = (int) model.getValueAt(row, 0); // ID 가져오기
            int confirm = JOptionPane.showConfirmDialog(dialog, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "DELETE FROM notice WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(dialog, "삭제되었습니다.");
                    dialog.clearInput(); // 내용 비우기
                    loadNoticeList(model); // 목록 새로고침

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "삭제 실패: " + ex.getMessage());
                }
            }
        });

        dialog.getBtnClose().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // 공지사항 목록을 불러오는 도우미 메서드
    private void loadNoticeList(DefaultTableModel model) {
        model.setRowCount(0); // 기존 목록 초기화
        String sql = "SELECT id, title, created_at FROM notice ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at")
                });
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    //  [기능 4] DB CRUD (강의목록, 출석부)
    // =========================================================
    private void loadCourseList() {
        DefaultTableModel model = (DefaultTableModel) coursePanel.getTable().getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM course";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("code"), rs.getString("name"), rs.getString("room"),
                        rs.getString("time"), rs.getInt("capacity") + "명", rs.getString("status")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadAttendanceData(String courseCode) {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        model.setRowCount(0);
        String sql = "SELECT a.student_id, s.name, s.major, s.status, a.attendance_state, a.note " +
                "FROM attendance a JOIN student s ON a.student_id = s.id WHERE a.course_code = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String note = rs.getString("note"); if (note == null) note = "";
                    model.addRow(new Object[]{
                            rs.getString("student_id"), rs.getString("name"), rs.getString("major"),
                            rs.getString("status"), rs.getString("attendance_state"), note
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        updateSummary();
    }

    private void saveAttendanceData() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        String sql = "UPDATE attendance SET attendance_state = ?, note = ? WHERE student_id = ? AND course_code = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int updatedCount = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                pstmt.setString(1, (String) model.getValueAt(i, 4));
                pstmt.setString(2, (String) model.getValueAt(i, 5));
                pstmt.setString(3, (String) model.getValueAt(i, 0));
                pstmt.setString(4, currentCourseCode);
                pstmt.addBatch();
                updatedCount++;
            }
            pstmt.executeBatch();
            JOptionPane.showMessageDialog(view, updatedCount + "명의 정보가 저장되었습니다.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void openAddCourseDialog() {
        AddCourseDialog dialog = new AddCourseDialog(view);
        dialog.getBtnSave().addActionListener(e -> {
            String code = dialog.getCode();
            String name = dialog.getCourseName();
            if (code.isEmpty() || name.isEmpty()) { JOptionPane.showMessageDialog(dialog, "필수 정보를 입력하세요."); return; }
            String sql = "INSERT INTO course (code, name, room, time, capacity, status) VALUES (?, ?, ?, ?, ?, '진행중')";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, code);
                pstmt.setString(2, name);
                pstmt.setString(3, dialog.getRoom());
                pstmt.setString(4, dialog.getDayTime());
                pstmt.setInt(5, dialog.getCapacity());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(view, "강의가 개설되었습니다.");
                loadCourseList();
                dialog.dispose();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(dialog, "실패: " + ex.getMessage()); }
        });
        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void deleteSelectedCourse() {
        JTable table = coursePanel.getTable();
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(view, "삭제할 강의를 선택해주세요."); return; }
        String code = (String) table.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(view, "정말 삭제하시겠습니까? (관련 출석 데이터도 삭제됩니다)", "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DBUtil.getConnection()) {
                conn.prepareStatement("DELETE FROM enrolment WHERE course_code='" + code + "'").executeUpdate();
                conn.prepareStatement("DELETE FROM attendance WHERE course_code='" + code + "'").executeUpdate();
                conn.prepareStatement("DELETE FROM course WHERE code='" + code + "'").executeUpdate();
                loadCourseList();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- 기타 UI 로직 (일괄 처리 등) ---
    private void processBatchAttendance() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if ("재학".equals(model.getValueAt(i, 3)) && "미처리".equals(model.getValueAt(i, 4))) {
                model.setValueAt("출석", i, 4);
            }
        }
        updateSummary();
    }

    private void resetAllToUnprocessed() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 3);
            if (!"휴학".equals(status) && !"자퇴".equals(status)) {
                model.setValueAt("미처리", i, 4);
                model.setValueAt("", i, 5);
            }
        }
        updateSummary();
    }

    private void updateSummary() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        int total = 0, present = 0, late = 0, absent = 0, pub = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if ("재학".equals(model.getValueAt(i, 3))) {
                total++;
                String att = (String) model.getValueAt(i, 4);
                if (att != null) {
                    switch (att) {
                        case "출석": present++; break;
                        case "지각": late++; break;
                        case "결석": absent++; break;
                        case "공결": pub++; break;
                    }
                }
            }
        }
        attendancePanel.getSummaryPanel().updateCounts(total, present, late, absent, pub);
    }
}