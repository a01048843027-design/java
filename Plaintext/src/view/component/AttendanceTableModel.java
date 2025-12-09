package view.component;

import javax.swing.table.DefaultTableModel;

public class AttendanceTableModel extends DefaultTableModel {

    public AttendanceTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // 모든 열을 문자열로 처리 (체크박스 Boolean 삭제됨)
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 학적 상태 가져오기 (이제 인덱스 3번이 학적상태)
        String status = (String) getValueAt(row, 3);

        // 휴학이나 자퇴생은 아예 수정 불가
        if ("휴학".equals(status) || "자퇴".equals(status)) {
            return false;
        }

        // 4번(출석결과)과 5번(비고) 열만 수정 가능
        return column == 4 || column == 5;
    }
}