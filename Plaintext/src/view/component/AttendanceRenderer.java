package view.component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AttendanceRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String status = (String) table.getModel().getValueAt(row, 3);
        String attendance = (String) table.getModel().getValueAt(row, 4);

        if ("휴학".equals(status) || "자퇴".equals(status)) {
            c.setBackground(new Color(240, 240, 240));
            c.setForeground(Color.GRAY);
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        if (column == 4) {
            if ("결석".equals(attendance)) {
                c.setForeground(Color.RED);
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else if ("지각".equals(attendance)) {
                c.setForeground(Color.ORANGE.darker());
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else if ("출석".equals(attendance)) {
                c.setForeground(new Color(0, 150, 0));
            } else if ("공결".equals(attendance)) {
                c.setForeground(Color.BLUE);
            }
        }

        if (isSelected) {
            c.setBackground(new Color(220, 240, 255));
        }

        return c;
    }
}