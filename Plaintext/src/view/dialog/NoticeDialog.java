package view.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NoticeDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTitle;
    private JTextArea txtContent;
    private JButton btnAdd, btnDelete, btnClose;

    public NoticeDialog(Frame parent) {
        super(parent, "공지사항 게시판", true);
        setSize(800, 500); // 가로를 좀 더 넓혔습니다.
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 좌측: 공지 목록
        String[] cols = {"ID", "제목", "작성일"}; // ID 컬럼 필수
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);

        // ID 컬럼 숨기기 (선택 사항: 보기에 깔끔하게 하려면 너비를 0으로)
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane listScroll = new JScrollPane(table);
        listScroll.setPreferredSize(new Dimension(350, 0));

        // 우측: 작성/상세 영역
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        txtTitle = new JTextField();
        txtTitle.setBorder(BorderFactory.createTitledBorder("제목"));

        txtContent = new JTextArea();
        txtContent.setBorder(BorderFactory.createTitledBorder("내용"));
        txtContent.setLineWrap(true); // 줄바꿈 자동

        inputPanel.add(txtTitle, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(txtContent), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("신규 등록");
        btnAdd.setBackground(new Color(0, 102, 204));
        btnAdd.setForeground(Color.WHITE);

        btnDelete = new JButton("선택 삭제");
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);

        btnClose = new JButton("닫기");

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClose);

        rightPanel.add(inputPanel, BorderLayout.CENTER);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        // 스플릿 뷰
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, rightPanel);
        split.setDividerLocation(350);
        add(split, BorderLayout.CENTER);
    }

    // Getters & Setters
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return table; }

    public String getTitleText() { return txtTitle.getText(); }
    public String getContentText() { return txtContent.getText(); }

    // [추가된 부분] 컨트롤러에서 화면에 글자를 띄우기 위해 필요
    public void setTitleText(String text) { txtTitle.setText(text); }
    public void setContentText(String text) { txtContent.setText(text); }

    public void clearInput() {
        txtTitle.setText("");
        txtContent.setText("");
        table.clearSelection(); // 테이블 선택도 해제
    }

    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnClose() { return btnClose; }
}