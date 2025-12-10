package view.panel;

import view.component.StudentTableRenderer; // 렌더러 임포트

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

        // 상단
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("수강신청");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));

        btnRefresh = new JButton("목록 갱신");
        btnRefresh.setBackground(new Color(245, 245, 245));
        btnRefresh.setFocusPainted(false);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 테이블
        String[] cols = {"강의코드", "강의명", "시간", "강의실", "신청현황", "내 상태"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        // [디자인 개선 1] 헤더 스타일
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setReorderingAllowed(false);

        // [디자인 개선 2] 렌더러 적용
        StudentTableRenderer renderer = new StudentTableRenderer();
        for(int i=0; i<table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // 너비 조절
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 하단 버튼 (크게 강조)
        btnApply = new JButton("선택한 강의 수강신청");
        btnApply.setBackground(new Color(52, 152, 219)); // 밝은 파랑
        btnApply.setForeground(Color.WHITE);
        btnApply.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        btnApply.setPreferredSize(new Dimension(0, 50));
        btnApply.setFocusPainted(false);

        add(btnApply, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }
    public JButton getBtnApply() { return btnApply; }
    public JButton getBtnRefresh() { return btnRefresh; }
}