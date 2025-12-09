package view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentEnrolmentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnApply, btnRefresh;

    public StudentEnrolmentPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("수강신청");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        btnRefresh = new JButton("새로고침");
        btnRefresh.setBackground(Color.WHITE);
        topPanel.add(btnRefresh, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 테이블: 강의코드, 강의명, 교수님, 시간, 강의실, 정원, 신청상태
        String[] cols = {"강의코드", "강의명", "시간", "강의실", "현재인원", "내 상태"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnApply = new JButton("수강신청 하기");
        btnApply.setBackground(new Color(0, 102, 204));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        btnApply.setPreferredSize(new Dimension(0, 50));

        add(btnApply, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }
    public JButton getBtnApply() { return btnApply; }
    public JButton getBtnRefresh() { return btnRefresh; }
}