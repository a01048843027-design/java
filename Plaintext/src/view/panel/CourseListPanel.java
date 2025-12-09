package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseListPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAddCourse;

    public CourseListPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("담당 강의 목록");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        btnAddCourse = new JButton("강의 추가");
        btnAddCourse.setBackground(new Color(0, 102, 204));
        btnAddCourse.setForeground(Color.WHITE);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(btnAddCourse, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"강의코드", "강의명", "강의실", "시간", "수강인원", "상태"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        model.addRow(new Object[]{"CS101", "객체지향 프로그래밍", "301호", "월/수 10:00", "30명", "진행중"});
        model.addRow(new Object[]{"CS102", "데이터베이스", "405호", "화/목 14:00", "25명", "진행중"});
        model.addRow(new Object[]{"CS201", "알고리즘", "302호", "금 09:00", "40명", "휴강"});

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public JTable getTable() { return table; }
    public JButton getBtnAddCourse() { return btnAddCourse; }
}