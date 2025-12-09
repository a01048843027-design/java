package view.panel;

import javax.swing.*;
import java.awt.*;

public class AttendanceContainerPanel extends JPanel {
    private TablePanel tablePanel;
    private SummaryPanel summaryPanel;
    private ControlPanel controlPanel;
    private JButton btnBack;

    public AttendanceContainerPanel() {
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        btnBack = new JButton("◀ 목록으로");
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);

        JLabel title = new JLabel("  강의 출석부 상세");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        topPanel.add(btnBack);
        topPanel.add(title);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 테이블
        tablePanel = new TablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // 하단 패널
        JPanel bottomContainer = new JPanel(new BorderLayout());
        summaryPanel = new SummaryPanel();
        controlPanel = new ControlPanel();

        bottomContainer.add(summaryPanel, BorderLayout.NORTH);
        bottomContainer.add(controlPanel, BorderLayout.CENTER);
        add(bottomContainer, BorderLayout.SOUTH);
    }

    public TablePanel getTablePanel() { return tablePanel; }
    public SummaryPanel getSummaryPanel() { return summaryPanel; }
    public ControlPanel getControlPanel() { return controlPanel; }
    public JButton getBtnBack() { return btnBack; }
}