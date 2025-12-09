package view.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ApprovalDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnApprove, btnReject, btnClose;
    private JButton btnSelectAll, btnUnselectAll; // 전체선택 버튼 추가

    public ApprovalDialog(Frame parent) {
        super(parent, "수강신청 승인 관리", true);
        setSize(700, 450); // 가로 살짝 늘림
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // [수정됨] 맨 앞에 '선택' 컬럼(Boolean) 추가
        String[] cols = {"선택", "신청ID", "강의명", "학번", "이름", "학과", "상태"};

        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // 0번 컬럼은 체크박스로 표시
                return columnIndex == 0 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                // 체크박스만 클릭 가능
                return column == 0;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        // 체크박스 컬럼 너비 줄이기
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(40);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 버튼 패널
        JPanel btnPanel = new JPanel();

        // [추가됨] 전체 선택/해제 버튼
        btnSelectAll = new JButton("전체 선택");
        btnSelectAll.setBackground(Color.LIGHT_GRAY);
        btnUnselectAll = new JButton("전체 해제");
        btnUnselectAll.setBackground(Color.WHITE);

        btnApprove = new JButton("일괄 승인");
        btnApprove.setBackground(new Color(0, 150, 0));
        btnApprove.setForeground(Color.WHITE);

        btnReject = new JButton("일괄 거절");
        btnReject.setBackground(Color.RED);
        btnReject.setForeground(Color.WHITE);

        btnClose = new JButton("닫기");

        btnPanel.add(btnSelectAll);
        btnPanel.add(btnUnselectAll);
        btnPanel.add(Box.createHorizontalStrut(20)); // 간격 띄우기
        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        btnPanel.add(btnClose);

        add(btnPanel, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }
    public JButton getBtnApprove() { return btnApprove; }
    public JButton getBtnReject() { return btnReject; }
    public JButton getBtnClose() { return btnClose; }

    // 추가된 버튼 Getter
    public JButton getBtnSelectAll() { return btnSelectAll; }
    public JButton getBtnUnselectAll() { return btnUnselectAll; }
}