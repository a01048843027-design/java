package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseListPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAddCourse;
    private JButton btnDeleteCourse; // 삭제 버튼 추가

    public CourseListPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // 상단 타이틀 및 버튼 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("담당 강의 목록");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        // 버튼들을 담을 패널 (FlowLayout)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        btnAddCourse = new JButton("강의 추가");
        btnAddCourse.setBackground(new Color(0, 102, 204));
        btnAddCourse.setForeground(Color.WHITE);

        btnDeleteCourse = new JButton("선택 삭제"); // 삭제 버튼 생성
        btnDeleteCourse.setBackground(new Color(255, 100, 100)); // 빨간색
        btnDeleteCourse.setForeground(Color.WHITE);

        buttonPanel.add(btnAddCourse);
        buttonPanel.add(btnDeleteCourse);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 테이블
        String[] columns = {"강의코드", "강의명", "강의실", "시간", "수강인원", "상태"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public JTable getTable() { return table; }
    public JButton getBtnAddCourse() { return btnAddCourse; }
    public JButton getBtnDeleteCourse() { return btnDeleteCourse; } // Getter 추가
}