package view.panel;

import util.DBUtil;
import view.component.AttendanceRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AttendancePanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainContainer;

    // 화면 1: 강의 목록
    private JPanel listPanel;
    private DefaultTableModel listModel;
    private JTable listTable;

    // 화면 2: 상세 출석부
    private JPanel detailPanel;
    private DefaultTableModel detailModel;
    private JTable detailTable;
    private JLabel lblTitle;
    private String currentCourseCode; // 현재 보고 있는 강의 코드 저장용

    // 하단 통계 라벨
    private JLabel lblTotal, lblPresent, lblLate, lblAbsent, lblPublic;

    public AttendancePanel() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        createListPanel();
        createDetailPanel();

        mainContainer.add(listPanel, "LIST");
        mainContainer.add(detailPanel, "DETAIL");

        add(mainContainer, BorderLayout.CENTER);

        loadLectureList(); // 시작할 때 목록 불러오기
    }

    // [화면 1] 강의 목록 패널
    private void createListPanel() {
        listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("출석부 관리 - 강의 선택"));

        String[] cols = {"강의코드", "강의명", "교수", "시간", "강의실"};
        listModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        listTable = new JTable(listModel);
        listTable.setRowHeight(30);

        listTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = listTable.getSelectedRow();
                    if (row != -1) {
                        String code = (String) listModel.getValueAt(row, 0);
                        String name = (String) listModel.getValueAt(row, 1);
                        showDetail(code, name); // 상세 화면으로 이동
                    }
                }
            }
        });

        listPanel.add(new JScrollPane(listTable), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("목록 새로고침");
        btnRefresh.addActionListener(e -> loadLectureList());
        listPanel.add(btnRefresh, BorderLayout.SOUTH);
    }

    // [화면 2] 상세 출석부 패널
    private void createDetailPanel() {
        detailPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("◀ 목록으로");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "LIST"));

        lblTitle = new JLabel("강의 출석부", SwingConstants.CENTER);
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(lblTitle, BorderLayout.CENTER);
        detailPanel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"학번", "이름", "학과", "학적", "출석결과", "비고"};
        detailModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return col == 4 || col == 5; }
        };
        detailTable = new JTable(detailModel);
        detailTable.setRowHeight(35);
        detailTable.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        // 렌더러 적용 (색깔)
        AttendanceRenderer renderer = new AttendanceRenderer();
        for (int i = 0; i < detailTable.getColumnCount(); i++) {
            detailTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // 콤보박스
        JComboBox<String> combo = new JComboBox<>(new String[]{"출석", "지각", "결석", "공결", "미처리"});
        detailTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(combo));

        detailModel.addTableModelListener(e -> updateSummary());

        detailPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        // 하단 패널
        JPanel bottomContainer = new JPanel(new BorderLayout());
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        lblTotal = new JLabel("총원: 0명");
        lblPresent = new JLabel("출석: 0"); lblPresent.setForeground(new Color(0, 150, 0));
        lblLate = new JLabel("지각: 0"); lblLate.setForeground(Color.ORANGE.darker());
        lblAbsent = new JLabel("결석: 0"); lblAbsent.setForeground(Color.RED);
        lblPublic = new JLabel("공결: 0"); lblPublic.setForeground(Color.BLUE);

        Font font = new Font("맑은 고딕", Font.BOLD, 14);
        lblTotal.setFont(font); lblPresent.setFont(font); lblLate.setFont(font); lblAbsent.setFont(font); lblPublic.setFont(font);

        summaryPanel.add(lblTotal); summaryPanel.add(new JLabel("|"));
        summaryPanel.add(lblPresent); summaryPanel.add(new JLabel("|"));
        summaryPanel.add(lblLate); summaryPanel.add(new JLabel("|"));
        summaryPanel.add(lblAbsent); summaryPanel.add(new JLabel("|"));
        summaryPanel.add(lblPublic);

        bottomContainer.add(summaryPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton btnBatch = new JButton("일괄 출석 (미처리만)");
        JButton btnReset = new JButton("전체 해제");
        JButton btnSave = new JButton("일괄 저장"); // ★ 진짜 저장 버튼

        btnBatch.addActionListener(e -> batchAttendance());
        btnReset.addActionListener(e -> resetAttendance());
        btnSave.addActionListener(e -> saveAttendanceToDB()); // DB 저장 연결

        controlPanel.add(btnBatch);
        controlPanel.add(btnReset);
        controlPanel.add(btnSave);

        bottomContainer.add(controlPanel, BorderLayout.CENTER);
        detailPanel.add(bottomContainer, BorderLayout.SOUTH);
    }

    // --- DB 연동 메서드 ---

    // 1. 강의 목록 불러오기
    public void loadLectureList() {
        listModel.setRowCount(0);
        String sql = "SELECT * FROM courses";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listModel.addRow(new Object[]{
                        rs.getString("course_code"),
                        rs.getString("subject_name"),
                        rs.getString("professor"),
                        rs.getString("class_time"),
                        rs.getString("room")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 2. [핵심] 실제 수강생 목록 & 출석 정보 불러오기
    private void showDetail(String code, String name) {
        this.currentCourseCode = code;
        lblTitle.setText("[" + code + "] " + name + " 출석부");
        detailModel.setRowCount(0);

        // enrolment(수강신청) + student(학생정보) + attendance(출석기록) 조인 쿼리
        String sql = "SELECT s.id, s.name, s.major, s.status, " +
                "COALESCE(a.attendance_state, '미처리') as att, " +
                "COALESCE(a.note, '') as note " +
                "FROM enrolment e " +
                "JOIN student s ON e.student_id = s.id " +
                "LEFT JOIN attendance a ON e.student_id = a.student_id AND e.course_code = a.course_code " +
                "WHERE e.course_code = ? AND e.status = '승인'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                detailModel.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("major"),
                        rs.getString("status"),
                        rs.getString("att"),
                        rs.getString("note")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "수강생 정보를 불러오지 못했습니다: " + e.getMessage());
        }

        updateSummary();
        cardLayout.show(mainContainer, "DETAIL");
    }

    // 3. [핵심] 변경된 출석 정보 DB에 저장하기
    private void saveAttendanceToDB() {
        if (currentCourseCode == null) return;

        // INSERT ... ON DUPLICATE KEY UPDATE (있으면 수정, 없으면 추가)
        String sql = "INSERT INTO attendance (student_id, course_code, attendance_state, note) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE attendance_state = VALUES(attendance_state), note = VALUES(note)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int count = detailModel.getRowCount();
            for (int i = 0; i < count; i++) {
                String stdId = (String) detailModel.getValueAt(i, 0);
                String state = (String) detailModel.getValueAt(i, 4);
                String note = (String) detailModel.getValueAt(i, 5);

                pstmt.setString(1, stdId);
                pstmt.setString(2, currentCourseCode);
                pstmt.setString(3, state);
                pstmt.setString(4, note);
                pstmt.addBatch(); // 일괄 처리용 담기
            }

            pstmt.executeBatch(); // 한방에 전송
            JOptionPane.showMessageDialog(this, "출석 정보가 DB에 저장되었습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "저장 실패: " + e.getMessage());
        }
    }

    private void updateSummary() {
        int total = detailModel.getRowCount();
        int present = 0, late = 0, absent = 0, pub = 0;

        for (int i = 0; i < total; i++) {
            String status = (String) detailModel.getValueAt(i, 4);
            if ("출석".equals(status)) present++;
            else if ("지각".equals(status)) late++;
            else if ("결석".equals(status)) absent++;
            else if ("공결".equals(status)) pub++;
        }

        lblTotal.setText("총원: " + total + "명");
        lblPresent.setText("출석: " + present);
        lblLate.setText("지각: " + late);
        lblAbsent.setText("결석: " + absent);
        lblPublic.setText("공결: " + pub);
    }

    private void batchAttendance() {
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            String status = (String) detailModel.getValueAt(i, 4);
            String schoolReg = (String) detailModel.getValueAt(i, 3);
            if (!"휴학".equals(schoolReg) && ("미처리".equals(status) || status == null)) {
                detailModel.setValueAt("출석", i, 4);
            }
        }
        updateSummary();
    }

    private void resetAttendance() {
        for (int i = 0; i < detailModel.getRowCount(); i++) {
            detailModel.setValueAt("미처리", i, 4);
        }
        updateSummary();
    }
}