package view.panel;

import view.component.StudentTableRenderer; // 렌더러 임포트

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

        // 상단 타이틀
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("내 강의실 (출석 현황)");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));

        btnRefresh = new JButton("새로고침");
        btnRefresh.setBackground(new Color(245, 245, 245));
        btnRefresh.setFocusPainted(false);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 테이블 설정
        String[] cols = {"강의명", "나의 출석 상태", "비고"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35); // 행 높이 시원하게 조절
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        // [디자인 개선 1] 헤더 배경색 진하게
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setReorderingAllowed(false);

        // [디자인 개선 2] 색상 렌더러 적용
        StudentTableRenderer renderer = new StudentTableRenderer();
        for(int i=0; i<table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public DefaultTableModel getModel() { return model; }
    public JButton getBtnRefresh() { return btnRefresh; }
}