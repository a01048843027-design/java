package view.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private JButton btnBatch, btnReset, btnSave, btnLogout;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnBatch = new JButton("일괄 출석 (미처리만)");
        btnReset = new JButton("전체 해제");
        btnSave = new JButton("일괄 저장");
        btnLogout = new JButton("로그아웃");

        btnBatch.setBackground(new Color(230, 240, 255));

        add(btnBatch);
        add(btnReset);
        add(btnSave);
        add(btnLogout);
    }

    public void addBatchListener(ActionListener listener) { btnBatch.addActionListener(listener); }
    public void addResetListener(ActionListener listener) { btnReset.addActionListener(listener); }
    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    public void addLogoutListener(ActionListener listener) { btnLogout.addActionListener(listener); }
}