package view.panel;

import view.component.AttendanceRenderer;
import view.component.AttendanceTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TablePanel extends JPanel {
    private JTable table;
    private AttendanceTableModel model;

    public TablePanel() {
        setLayout(new BorderLayout());

        // 컬럼 설정
        String[] columns = {"학번", "이름", "학과", "학적상태", "출석결과", "비고(특이사항)"};

        model = new AttendanceTableModel(columns, 0);
        table = new JTable(model);

        // 스타일 설정
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        // 렌더러 적용 (색상 표시)
        AttendanceRenderer renderer = new AttendanceRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // 콤보박스 설정
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"재학", "휴학", "자퇴", "제적"});
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusCombo));

        JComboBox<String> attCombo = new JComboBox<>(new String[]{"출석", "지각", "결석", "공결", "미처리"});
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(attCombo));

        // 너비 조절
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }
}