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

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnBack = new JButton("◀ 목록으로");
        btnBack.setBackground(Color.WHITE);

        JLabel title = new JLabel("  [CS101] 객체지향 프로그래밍 출석부");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        topPanel.add(btnBack);
        topPanel.add(title);
        add(topPanel, BorderLayout.NORTH);

        tablePanel = new TablePanel();
        add(tablePanel, BorderLayout.CENTER);

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