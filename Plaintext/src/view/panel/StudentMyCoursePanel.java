package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentMyCoursePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnRefresh;

    public StudentMyCoursePanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("내 강의실 (출석 현황)");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        btnRefresh = new JButton("새로고침");
        btnRefresh.setBackground(Color.WHITE);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 테이블: 강의명, 출석, 지각, 결석, 공결, 비고
        String[] cols = {"강의명", "나의 출석 상태", "비고"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public DefaultTableModel getModel() { return model; }
    public JButton getBtnRefresh() { return btnRefresh; }
}