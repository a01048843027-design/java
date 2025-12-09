package view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtId;
    private JPasswordField txtPw;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("학사 관리 시스템 - 로그인");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SYSTEM LOGIN");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(title, gbc);

        JLabel lblId = new JLabel("ID");
        lblId.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        gbc.gridwidth = 1; gbc.gridy++;
        centerPanel.add(lblId, gbc);

        txtId = new JTextField(15);
        txtId.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        txtId.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 1;
        centerPanel.add(txtId, gbc);

        JLabel lblPw = new JLabel("PW");
        lblPw.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        gbc.gridx = 0; gbc.gridy++;
        centerPanel.add(lblPw, gbc);

        txtPw = new JPasswordField(15);
        txtPw.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        txtPw.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 1;
        centerPanel.add(txtPw, gbc);

        btnLogin = new JButton("로그인");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        btnLogin.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 15;
        centerPanel.add(btnLogin, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public String getId() { return txtId.getText(); }
    public String getPw() { return new String(txtPw.getPassword()); }
    public JButton getBtnLogin() { return btnLogin; }
    public JPasswordField getTxtPw() { return txtPw; }
}