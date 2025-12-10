package view.panel;

import util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CoursePanel extends JPanel {

    private DefaultTableModel lectureModel;
    private DefaultTableModel approvalModel;
    private JTable lectureTable;
    private JTable approvalTable;

    private JTextField txtName, txtProf, txtTime, txtRoom, txtMax, txtCode;

    public CoursePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 상단 입력 패널
        JPanel topContainer = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("강의 정보 입력"));

        inputPanel.add(new JLabel("코드:"));
        txtCode = new JTextField(6);
        inputPanel.add(txtCode);

        inputPanel.add(new JLabel("강의명:"));
        txtName = new JTextField(8);
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("교수:"));
        txtProf = new JTextField("김교수", 5);
        inputPanel.add(txtProf);

        inputPanel.add(new JLabel("시간(예:월1):"));
        txtTime = new JTextField(5);
        inputPanel.add(txtTime);

        inputPanel.add(new JLabel("강의실:"));
        txtRoom = new JTextField(5);
        inputPanel.add(txtRoom);

        inputPanel.add(new JLabel("정원:"));
        txtMax = new JTextField("30", 3);
        inputPanel.add(txtMax);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("추가");
        JButton btnEdit = new JButton("수정");
        JButton btnDel = new JButton("삭제");

        btnAdd.setBackground(new Color(70, 130, 180)); btnAdd.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(60, 179, 113)); btnEdit.setForeground(Color.WHITE);
        btnDel.setBackground(new Color(220, 20, 60)); btnDel.setForeground(Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);

        topContainer.add(inputPanel, BorderLayout.CENTER);
        topContainer.add(btnPanel, BorderLayout.EAST);
        add(topContainer, BorderLayout.NORTH);

        // 중앙 테이블 영역
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // 왼쪽: 강의 목록
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("개설된 강의 목록"));

        String[] lectureCols = {"코드", "강의명", "교수", "시간", "강의실", "정원"};
        lectureModel = new DefaultTableModel(lectureCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        lectureTable = new JTable(lectureModel);
        leftPanel.add(new JScrollPane(lectureTable), BorderLayout.CENTER);

        // 테이블 클릭 시 입력창에 값 채우기
        lectureTable.getSelectionModel().addListSelectionListener(e -> {
            int row = lectureTable.getSelectedRow();
            if (row != -1) {
                txtCode.setText((String) lectureModel.getValueAt(row, 0));
                txtName.setText((String) lectureModel.getValueAt(row, 1));
                txtProf.setText((String) lectureModel.getValueAt(row, 2));
                txtTime.setText((String) lectureModel.getValueAt(row, 3));
                txtRoom.setText((String) lectureModel.getValueAt(row, 4));
                txtMax.setText(lectureModel.getValueAt(row, 5).toString());
            }
        });

        // 오른쪽: 승인 대기 목록
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("학생 수강 신청 승인"));

        String[] appCols = {"학생명", "신청강의", "상태"};
        approvalModel = new DefaultTableModel(appCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        approvalTable = new JTable(approvalModel);
        rightPanel.add(new JScrollPane(approvalTable), BorderLayout.CENTER);

        JButton btnApprove = new JButton("선택 승인 처리");
        rightPanel.add(btnApprove, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // 버튼 기능 연결
        btnAdd.addActionListener(e -> addLecture());
        btnEdit.addActionListener(e -> editLecture());
        btnDel.addActionListener(e -> deleteLecture());
        btnApprove.addActionListener(e -> approveRequest());

        loadData();
    }

    private void addLecture() {
        if (txtCode.getText().isEmpty() || txtName.getText().isEmpty()) return;

        // [수정] 실제 DB 컬럼명 적용 (course_code, subject_name, class_time 등)
        String sql = "INSERT INTO courses (course_code, subject_name, professor, class_time, room, max_students) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, txtCode.getText());
            pstmt.setString(2, txtName.getText());
            pstmt.setString(3, txtProf.getText());
            pstmt.setString(4, txtTime.getText());
            pstmt.setString(5, txtRoom.getText());
            pstmt.setInt(6, Integer.parseInt(txtMax.getText()));
            pstmt.executeUpdate();

            loadData();
            clearFields();
        } catch (SQLException e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, "DB 오류: " + e.getMessage()); }
    }

    private void editLecture() {
        if (txtCode.getText().isEmpty()) return;

        // [수정] 실제 DB 컬럼명 적용
        String sql = "UPDATE courses SET subject_name=?, professor=?, class_time=?, room=?, max_students=? WHERE course_code=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtProf.getText());
            pstmt.setString(3, txtTime.getText());
            pstmt.setString(4, txtRoom.getText());
            pstmt.setInt(5, Integer.parseInt(txtMax.getText()));
            pstmt.setString(6, txtCode.getText());
            pstmt.executeUpdate();

            loadData();
            clearFields();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void deleteLecture() {
        String code = txtCode.getText();
        if (code.isEmpty()) return;

        // [수정] 테이블명 courses
        String sql = "DELETE FROM courses WHERE course_code=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();

            loadData();
            clearFields();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void approveRequest() {
        JOptionPane.showMessageDialog(this, "승인 기능은 현재 준비 중입니다.");
    }

    private void clearFields() {
        txtCode.setText(""); txtName.setText(""); txtTime.setText("");
        txtRoom.setText(""); lectureTable.clearSelection();
    }

    private void loadData() {
        lectureModel.setRowCount(0);
        // [수정] 테이블명 courses 사용
        String sql = "SELECT * FROM courses";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // [수정] 실제 컬럼명으로 데이터 가져오기
                lectureModel.addRow(new Object[]{
                        rs.getString("course_code"),
                        rs.getString("subject_name"),
                        rs.getString("professor"),
                        rs.getString("class_time"),
                        rs.getString("room"),
                        rs.getInt("max_students")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 발생 시 메시지 출력 (디버깅용)
            System.out.println("데이터 로드 실패: " + e.getMessage());
        }
    }
}