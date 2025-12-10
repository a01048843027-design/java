package view.component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StudentTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String text = (value != null) ? value.toString() : "";

        // 1. 기본 텍스트 정렬 (가운데)
        setHorizontalAlignment(SwingConstants.CENTER);

        // 2. 텍스트 내용에 따른 색상 구분 (직관성 강화)
        if ("출석".equals(text) || "승인".equals(text) || "신청가능".equals(text)) {
            c.setForeground(new Color(0, 150, 0)); // 초록색 (긍정)
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else if ("결석".equals(text) || "거절".equals(text)) {
            c.setForeground(Color.RED); // 빨간색 (부정)
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else if ("지각".equals(text)) {
            c.setForeground(Color.ORANGE.darker()); // 진한 주황색 (주의)
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else if ("대기".equals(text) || "미처리".equals(text)) {
            c.setForeground(Color.GRAY); // 회색 (중립)
        } else if ("마감".equals(text) || "정원초과".equals(text)) {
            c.setForeground(Color.LIGHT_GRAY); // 연한 회색 (비활성)
        } else {
            c.setForeground(Color.BLACK); // 기본 검정
        }

        // 3. 선택된 행 디자인
        if (isSelected) {
            c.setBackground(new Color(230, 240, 255)); // 연한 하늘색
        } else {
            c.setBackground(Color.WHITE);
        }

        return c;
    }
}