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

        // '선택' 컬럼 삭제됨
        String[] columns = {"학번", "이름", "학과", "학적상태", "출석결과", "비고(특이사항)"};

        model = new AttendanceTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);

        // 렌더러 적용
        AttendanceRenderer renderer = new AttendanceRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // **중요: 출석결과(4번 열)를 콤보박스로 선택하게 설정 (오타 방지)**
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"출석", "지각", "결석", "공결", "미처리"});
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));

        // 컬럼 너비 조절
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // 학번
        table.getColumnModel().getColumn(5).setPreferredWidth(200); // 비고

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }
}