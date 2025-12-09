package view.panel;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {
    private JLabel lblTotal, lblPresent, lblLate, lblAbsent, lblPublic;

    public SummaryPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        lblTotal = createLabel("총원: 0명", Color.BLACK);
        lblPresent = createLabel("출석: 0", new Color(0, 150, 0));
        lblLate = createLabel("지각: 0", Color.ORANGE.darker());
        lblAbsent = createLabel("결석: 0", Color.RED);
        lblPublic = createLabel("공결: 0", Color.BLUE);

        add(lblTotal);
        add(new JLabel("|"));
        add(lblPresent);
        add(new JLabel("|"));
        add(lblLate);
        add(new JLabel("|"));
        add(lblAbsent);
        add(new JLabel("|"));
        add(lblPublic);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        return label;
    }

    public void updateCounts(int total, int present, int late, int absent, int pub) {
        lblTotal.setText("총원: " + total + "명");
        lblPresent.setText("출석: " + present);
        lblLate.setText("지각: " + late);
        lblAbsent.setText("결석: " + absent);
        lblPublic.setText("공결: " + pub);
    }
}