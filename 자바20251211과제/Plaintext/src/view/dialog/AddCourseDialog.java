package view.dialog;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddCourseDialog extends JDialog {
    private JTextField txtCode, txtName, txtRoom;
    private JSpinner spinnerCap;
    private JButton btnSave, btnCancel, btnAddTime;
    private JPanel timeSlotsPanel; // 시간표 줄들이 쌓일 패널

    public AddCourseDialog(Frame parent) {
        super(parent, "강의 개설", true);
        setSize(650, 600); // 높이를 좀 더 확보
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 메인 폼 패널 (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridx = 0; gbc.gridy = 0;

        // 1. 강의 코드
        gbc.weightx = 0.2;
        formPanel.add(new LabelWithStyle("강의 코드"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        txtCode = new JTextField();
        txtCode.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) return;
                super.insertString(offset, str.toUpperCase(), attr);
            }
        });
        txtCode.setPreferredSize(new Dimension(100, 35));
        formPanel.add(txtCode, gbc);

        // 2. 강의명
        gbc.gridy++;
        gbc.gridx = 0; gbc.weightx = 0.2;
        formPanel.add(new LabelWithStyle("강의명"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(100, 35));
        formPanel.add(txtName, gbc);

        // 3. 강의실
        gbc.gridy++;
        gbc.gridx = 0; gbc.weightx = 0.2;
        formPanel.add(new LabelWithStyle("강의실"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        txtRoom = new JTextField();
        txtRoom.setPreferredSize(new Dimension(100, 35));
        formPanel.add(txtRoom, gbc);

        // 4. 최대 정원 (순서 변경: 시간표가 길어질 수 있어서 정원을 위로 올림)
        gbc.gridy++;
        gbc.gridx = 0; gbc.weightx = 0.2;
        formPanel.add(new LabelWithStyle("최대 정원"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        spinnerCap = new JSpinner(new SpinnerNumberModel(30, 10, 100, 1));
        spinnerCap.setPreferredSize(new Dimension(80, 35));
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        spinnerPanel.setBackground(Color.WHITE);
        spinnerPanel.add(spinnerCap);
        formPanel.add(spinnerPanel, gbc);

        // --- 5. 강의 시간 설정 (동적 추가 방식) ---
        gbc.gridy++;
        gbc.gridx = 0; gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTH; // 라벨을 상단에 고정
        formPanel.add(new LabelWithStyle("강의 시간"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;

        // 시간표 컨테이너 패널
        JPanel timeContainer = new JPanel(new BorderLayout());
        timeContainer.setBackground(Color.WHITE);

        // 헤더 (추가 버튼)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(Color.WHITE);
        btnAddTime = new JButton("+ 시간 추가");
        btnAddTime.setBackground(new Color(230, 240, 255));
        btnAddTime.setForeground(new Color(0, 102, 204));
        btnAddTime.setFocusPainted(false);
        btnAddTime.addActionListener(e -> addTimeSlotRow()); // 클릭 시 줄 추가
        headerPanel.add(btnAddTime);
        timeContainer.add(headerPanel, BorderLayout.NORTH);

        // 스크롤 가능한 목록 영역
        timeSlotsPanel = new JPanel();
        timeSlotsPanel.setLayout(new BoxLayout(timeSlotsPanel, BoxLayout.Y_AXIS)); // 수직 쌓기
        timeSlotsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(timeSlotsPanel);
        scrollPane.setPreferredSize(new Dimension(400, 150)); // 스크롤 영역 크기
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        timeContainer.add(scrollPane, BorderLayout.CENTER);

        formPanel.add(timeContainer, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 하단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnSave = new JButton("등록");
        btnSave.setBackground(new Color(0, 102, 204));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(100, 40));
        btnSave.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        btnCancel = new JButton("취소");
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // 기본적으로 한 줄은 추가해둠
        addTimeSlotRow();
    }

    // [핵심] 시간표 한 줄(Row)을 추가하는 메서드
    private void addTimeSlotRow() {
        TimeSlotRow row = new TimeSlotRow();
        timeSlotsPanel.add(row);
        timeSlotsPanel.revalidate(); // 레이아웃 갱신
        timeSlotsPanel.repaint();    // 화면 다시 그리기

        // 스크롤을 맨 아래로 이동
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = ((JScrollPane)timeSlotsPanel.getParent().getParent()).getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    // 시간표 한 줄을 표현하는 내부 클래스 (JPanel 상속)
    private class TimeSlotRow extends JPanel {
        private JComboBox<String> comboDay, comboStart, comboEnd;
        private JButton btnDelete;

        public TimeSlotRow() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240,240,240))); // 하단 구분선

            String[] days = {"월", "화", "수", "목", "금"};
            String[] times = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};

            comboDay = new JComboBox<>(days);
            comboStart = new JComboBox<>(times);
            comboEnd = new JComboBox<>(times);
            comboEnd.setSelectedIndex(1); // 기본 1시간 뒤

            comboDay.setBackground(Color.WHITE);
            comboStart.setBackground(Color.WHITE);
            comboEnd.setBackground(Color.WHITE);

            btnDelete = new JButton("X");
            btnDelete.setMargin(new Insets(0, 5, 0, 5));
            btnDelete.setBackground(new Color(255, 230, 230));
            btnDelete.setForeground(Color.RED);
            btnDelete.setFocusPainted(false);

            // 삭제 버튼 클릭 시 해당 줄 제거
            btnDelete.addActionListener(e -> {
                timeSlotsPanel.remove(this);
                timeSlotsPanel.revalidate();
                timeSlotsPanel.repaint();
            });

            add(comboDay);
            add(comboStart);
            add(new JLabel("~"));
            add(comboEnd);
            add(btnDelete);
        }

        // "월 10:00 ~ 12:00" 형태의 문자열 반환
        public String toStringFormat() {
            return comboDay.getSelectedItem() + " " +
                    comboStart.getSelectedItem() + "~" +
                    comboEnd.getSelectedItem();
        }
    }

    private class LabelWithStyle extends JLabel {
        public LabelWithStyle(String text) {
            super(text);
            setFont(new Font("맑은 고딕", Font.BOLD, 15));
        }
    }

    // Getters
    public String getCode() { return txtCode.getText(); }
    public String getCourseName() { return txtName.getText(); }
    public String getRoom() { return txtRoom.getText(); }
    public int getCapacity() { return (int) spinnerCap.getValue(); }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }

    // [수정됨] 모든 시간표 줄을 읽어서 하나의 문자열로 합침
    public String getDayTime() {
        StringBuilder sb = new StringBuilder();
        Component[] components = timeSlotsPanel.getComponents();

        for (Component comp : components) {
            if (comp instanceof TimeSlotRow) {
                if (sb.length() > 0) sb.append(" / "); // 구분자
                sb.append(((TimeSlotRow) comp).toStringFormat());
            }
        }

        if (sb.length() == 0) return "시간미정";
        return sb.toString();
    }
}