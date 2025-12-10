package view.component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AttendanceRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String text = (value != null) ? value.toString() : "";
        setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬

        // 기본 색상 설정 (선택됐을 때와 아닐 때)
        if (isSelected) {
            c.setBackground(new Color(220, 240, 255)); // 선택 시 연한 파랑
            c.setForeground(Color.BLACK);
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }

        // 텍스트 내용에 따른 글자색 변경 (여기가 핵심!)
        if ("결석".equals(text)) {
            c.setForeground(Color.RED);
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else if ("지각".equals(text)) {
            c.setForeground(Color.ORANGE.darker());
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else if ("출석".equals(text)) {
            c.setForeground(new Color(0, 150, 0)); // 진한 초록
        } else if ("공결".equals(text)) {
            c.setForeground(Color.BLUE);
        } else if ("휴학".equals(text)) {
            c.setBackground(new Color(240, 240, 240)); // 휴학은 배경 회색
            c.setForeground(Color.GRAY);
        }

        return c;
    }
}