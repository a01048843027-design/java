package view.component;

import javax.swing.table.DefaultTableModel;

public class AttendanceTableModel extends DefaultTableModel {

    public AttendanceTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // [수정됨] 3번(학적상태), 4번(출석결과), 5번(비고) 모두 수정 가능하게 변경
        // 단, 휴학/자퇴 상태일 때 '출석결과'나 '비고'를 수정하는 건 막고 싶다면 로직 추가 가능
        // 여기서는 학적상태를 고쳐야 하므로 일단 다 열어둡니다.
        return column == 3 || column == 4 || column == 5;
    }
}