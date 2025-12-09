package view.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentNoticeDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTitle;
    private JTextArea txtContent;
    private JButton btnClose;

    public StudentNoticeDialog(Frame parent) {
        super(parent, "공지사항 확인", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 1. 좌측: 공지 목록
        String[] cols = {"ID", "제목", "작성일"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);

        // ID 컬럼 숨기기
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // 제목 넓게

        JScrollPane listScroll = new JScrollPane(table);
        listScroll.setPreferredSize(new Dimension(300, 0));

        // 2. 우측: 상세 내용 (읽기 전용)
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(Color.WHITE);

        // 제목 표시줄
        txtTitle = new JTextField();
        txtTitle.setEditable(false); // 수정 불가
        txtTitle.setBackground(Color.WHITE);
        txtTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        txtTitle.setBorder(BorderFactory.createTitledBorder("제목"));

        // 내용 표시줄
        txtContent = new JTextArea();
        txtContent.setEditable(false); // 수정 불가
        txtContent.setBackground(Color.WHITE);
        txtContent.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        txtContent.setLineWrap(true);
        txtContent.setBorder(BorderFactory.createTitledBorder("내용"));

        rightPanel.add(txtTitle, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(txtContent), BorderLayout.CENTER);

        // 3. 하단 닫기 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnClose = new JButton("닫기");
        btnPanel.add(btnClose);

        // 스플릿 뷰로 합치기
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, rightPanel);
        split.setDividerLocation(300);
        add(split, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }

    // 내용 보여주기용 Setter
    public void setViewData(String title, String content) {
        txtTitle.setText(title);
        txtContent.setText(content);
        // 맨 위로 스크롤
        txtContent.setCaretPosition(0);
    }

    public JButton getBtnClose() { return btnClose; }
}