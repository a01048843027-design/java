package view.dialog;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class AddCourseDialog extends JDialog {
    private JTextField txtCode, txtName, txtRoom;
    private JCheckBox[] chkDays;
    private JComboBox<String> comboStartTime, comboEndTime;
    private JSpinner spinnerCap;
    private JButton btnSave, btnCancel;

    public AddCourseDialog(Frame parent) {
        super(parent, "강의 개설", true);

        // [변경 1] 가로 길이를 550으로 늘려서 요일이 한 줄에 나오게 함
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 20));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("강의 코드"));
        txtCode = new JTextField();
        // [변경 2] 입력된 문자를 무조건 대문자로 변환하는 로직 추가
        txtCode.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) return;
                super.insertString(offset, str.toUpperCase(), attr);
            }
        });
        formPanel.add(txtCode);

        formPanel.add(new JLabel("강의명"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("강의실"));
        txtRoom = new JTextField();
        formPanel.add(txtRoom);

        // 요일 선택 (공간이 넓어져서 한 줄로 나옴)
        formPanel.add(new JLabel("요일 선택"));
        JPanel dayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // 간격도 5->10으로 살짝 넓힘
        dayPanel.setBackground(Color.WHITE);

        String[] days = {"월", "화", "수", "목", "금"};
        chkDays = new JCheckBox[days.length];

        for (int i = 0; i < days.length; i++) {
            chkDays[i] = new JCheckBox(days[i]);
            chkDays[i].setBackground(Color.WHITE);
            chkDays[i].setFocusPainted(false);
            dayPanel.add(chkDays[i]);
        }
        formPanel.add(dayPanel);

        // 시간 선택
        formPanel.add(new JLabel("강의 시간"));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.setBackground(Color.WHITE);

        String[] times = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};
        comboStartTime = new JComboBox<>(times);
        comboEndTime = new JComboBox<>(times);
        comboEndTime.setSelectedIndex(1);

        timePanel.add(comboStartTime);
        timePanel.add(new JLabel("~"));
        timePanel.add(comboEndTime);
        formPanel.add(timePanel);

        formPanel.add(new JLabel("최대 정원"));
        spinnerCap = new JSpinner(new SpinnerNumberModel(30, 10, 100, 1));
        formPanel.add(spinnerCap);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSave = new JButton("등록");
        btnSave.setBackground(new Color(0, 102, 204));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new JButton("취소");
        btnCancel.setBackground(Color.WHITE);

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public String getCode() { return txtCode.getText(); }
    public String getCourseName() { return txtName.getText(); }
    public String getRoom() { return txtRoom.getText(); }

    public String getDayTime() {
        StringBuilder sb = new StringBuilder();
        for (JCheckBox chk : chkDays) {
            if (chk.isSelected()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(chk.getText());
            }
        }

        if (sb.length() == 0) sb.append("요일미정");

        return sb.toString() + " " +
                comboStartTime.getSelectedItem() + " ~ " +
                comboEndTime.getSelectedItem();
    }

    public int getCapacity() { return (int) spinnerCap.getValue(); }

    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
}