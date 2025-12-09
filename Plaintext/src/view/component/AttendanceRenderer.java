package view.component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AttendanceRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 인덱스 변경: 4(학적), 5(결과) -> 3(학적), 4(결과)
        String status = (String) table.getModel().getValueAt(row, 3);
        String attendance = (String) table.getModel().getValueAt(row, 4);

        // 1. 기본 행 배경색 (휴학/자퇴는 회색)
        if ("휴학".equals(status) || "자퇴".equals(status)) {
            c.setBackground(new Color(235, 235, 235));
            c.setForeground(Color.GRAY);
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        // 2. 출석 결과 컬럼 색상 처리 (인덱스 5 -> 4)
        if (column == 4) {
            if (attendance != null) {
                switch (attendance) {
                    case "결석":
                        c.setForeground(Color.RED);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        break;
                    case "지각":
                        c.setForeground(Color.ORANGE.darker());
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        break;
                    case "공결":
                        c.setForeground(Color.BLUE);
                        break;
                }
            }
        }

        // 3. 선택된 행 강조
        if (isSelected) {
            c.setBackground(new Color(220, 240, 255));
        }

        return c;
    }
}