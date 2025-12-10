package view.dialog;

import util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class NoticeDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTitle;
    private JTextArea txtContent;

    public NoticeDialog(Frame parent) {
        super(parent, "공지사항 게시판", true); // true = 모달 창 (이 창을 닫기 전엔 뒤에 클릭 불가)
        setSize(850, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- 1. 왼쪽: 공지 목록 ---
        String[] cols = {"ID", "제목", "작성일"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);

        // 테이블 클릭 이벤트 (내용 불러오기)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int id = (int) model.getValueAt(row, 0);
                    loadContent(id);
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(table);
        listScroll.setPreferredSize(new Dimension(380, 0));

        // --- 2. 오른쪽: 입력 및 버튼 ---
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtTitle = new JTextField();
        txtTitle.setBorder(BorderFactory.createTitledBorder("제목"));

        txtContent = new JTextArea();
        txtContent.setLineWrap(true);
        txtContent.setBorder(BorderFactory.createTitledBorder("내용"));

        inputPanel.add(txtTitle, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(txtContent), BorderLayout.CENTER);

        // 버튼 패널
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("신규 등록");
        JButton btnDel = new JButton("선택 삭제");
        JButton btnClose = new JButton("닫기");

        // 버튼 스타일
        btnAdd.setBackground(new Color(70, 130, 180)); btnAdd.setForeground(Color.WHITE);
        btnDel.setBackground(Color.RED); btnDel.setForeground(Color.WHITE);
        btnClose.setBackground(Color.DARK_GRAY); btnClose.setForeground(Color.WHITE);

        // ★★★ [핵심] 버튼 기능 연결 (여기가 없으면 작동 안 함) ★★★
        btnAdd.addActionListener(e -> addNotice());
        btnDel.addActionListener(e -> deleteNotice());
        btnClose.addActionListener(e -> dispose()); // 창 닫기 기능

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        btnPanel.add(btnClose);

        rightPanel.add(inputPanel, BorderLayout.CENTER);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        // 스플릿 뷰로 합치기
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, rightPanel);
        split.setDividerLocation(380);
        add(split, BorderLayout.CENTER);

        // 시작할 때 목록 불러오기
        loadList();
    }

    // 목록 불러오기 (DB 연동)
    private void loadList() {
        model.setRowCount(0);
        String sql = "SELECT id, title, created_at FROM notice ORDER BY id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "목록 로드 실패: " + e.getMessage());
        }
    }

    // 내용 상세 불러오기
    private void loadContent(int id) {
        String sql = "SELECT title, content FROM notice WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtTitle.setText(rs.getString("title"));
                txtContent.setText(rs.getString("content"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 신규 등록
    private void addNotice() {
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "제목을 입력하세요.");
            return;
        }

        String sql = "INSERT INTO notice (title, content) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            loadList();    // 목록 갱신
            clearFields(); // 입력창 비우기
            JOptionPane.showMessageDialog(this, "공지사항이 등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "등록 실패 (DB 오류): " + e.getMessage());
        }
    }

    // 선택 삭제
    private void deleteNotice() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 공지사항을 목록에서 선택하세요.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);

        String sql = "DELETE FROM notice WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            loadList();
            clearFields();
            JOptionPane.showMessageDialog(this, "삭제되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "삭제 실패: " + e.getMessage());
        }
    }

    // 입력창 초기화
    private void clearFields() {
        txtTitle.setText("");
        txtContent.setText("");
        table.clearSelection();
    }
}