package view.panel;

import util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SchedulePanel extends JPanel {

    private JTable scheduleTable;
    private DefaultTableModel tableModel;

    public SchedulePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("나의 강의 시간표 (강의 관리 연동)"));

        // 시간표 모델 (1교시 ~ 9교시)
        String[] days = {"교시", "월", "화", "수", "목", "금"};
        tableModel = new DefaultTableModel(days, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        for (int i = 1; i <= 9; i++) {
            tableModel.addRow(new Object[]{i + "교시", "", "", "", "", ""});
        }

        scheduleTable = new JTable(tableModel);
        scheduleTable.setRowHeight(50);
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        scheduleTable.setGridColor(Color.LIGHT_GRAY);
        scheduleTable.setShowGrid(true);

        // 셀 렌더러 (강의가 있으면 파란색 배경)
        scheduleTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column > 0 && value != null && !value.toString().trim().isEmpty()) {
                    c.setBackground(new Color(173, 216, 230)); // 파란색
                    c.setForeground(Color.BLACK);
                    setFont(new Font("맑은 고딕", Font.BOLD, 12));
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                if (column == 0) c.setBackground(new Color(240, 240, 240)); // 교시 컬럼 회색

                return c;
            }
        });

        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("시간표 새로고침");
        btnRefresh.addActionListener(e -> loadScheduleFromDB());
        add(btnRefresh, BorderLayout.SOUTH);

        loadScheduleFromDB();
    }

    public void loadScheduleFromDB() {
        // 테이블 초기화
        for (int r = 0; r < 9; r++) {
            for (int c = 1; c < 6; c++) tableModel.setValueAt("", r, c);
        }

        // [핵심] courses 테이블에서 시간 정보를 가져옴
        String sql = "SELECT subject_name, class_time, room FROM courses";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("subject_name");
                String timeStr = rs.getString("class_time"); // 예: "월 09:00~12:00"
                String room = rs.getString("room");

                if (timeStr != null) parseAndSetTime(name, timeStr, room);
            }
        } catch (SQLException e) { e.printStackTrace(); }

        scheduleTable.repaint();
    }

    private void parseAndSetTime(String name, String timeStr, String room) {
        if (timeStr.length() < 2) return;

        // 1. 요일 확인
        int col = -1;
        if (timeStr.contains("월")) col = 1;
        else if (timeStr.contains("화")) col = 2;
        else if (timeStr.contains("수")) col = 3;
        else if (timeStr.contains("목")) col = 4;
        else if (timeStr.contains("금")) col = 5;

        if (col == -1) return;

        // 2. 시간 파싱 (숫자 추출)
        // "월 09:00..." -> 09 추출
        // "월1" -> 1 추출
        try {
            String numberOnly = timeStr.replaceAll("[^0-9]", " "); // 숫자 빼고 공백으로
            String[] numbers = numberOnly.trim().split("\\s+");

            if (numbers.length > 0) {
                int startNum = Integer.parseInt(numbers[0]);
                int startPeriod;

                // 시간이 9 이상이면(09:00 등) 시각으로 간주하고 교시로 변환 (9시 -> 1교시)
                if (startNum >= 9) {
                    startPeriod = startNum - 8;
                } else {
                    // 1~8이면 그냥 교시로 간주
                    startPeriod = startNum;
                }

                // 2시간 연강으로 가정하고 표시 (필요 시 로직 수정 가능)
                if (startPeriod >= 1 && startPeriod <= 9) {
                    String display = "<html>" + name + "<br>(" + room + ")</html>";
                    tableModel.setValueAt(display, startPeriod - 1, col);

                    // 다음 교시도 채우기 (범위가 안 넘어가면)
                    if (startPeriod < 9) {
                        tableModel.setValueAt(display, startPeriod, col);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("시간 파싱 오류: " + timeStr);
        }
    }
}