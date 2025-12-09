package controller;

import view.MainFrame;
import view.LoginFrame;
import view.dialog.AddCourseDialog; // import 추가됨
import view.panel.CourseListPanel;
import view.panel.AttendanceContainerPanel;
import view.panel.TablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainController {
    private MainFrame view;
    private CourseListPanel coursePanel;
    private AttendanceContainerPanel attendancePanel;

    public MainController(MainFrame view) {
        this.view = view;

        coursePanel = new CourseListPanel();
        attendancePanel = new AttendanceContainerPanel();

        view.getMainContainer().add(coursePanel, "COURSE_LIST");
        view.getMainContainer().add(attendancePanel, "ATTENDANCE");

        initController();
    }

    private void initController() {
        view.getSideMenu().addDashboardListener(e -> view.showCard("DASHBOARD"));
        view.getSideMenu().addCourseMgmtListener(e -> view.showCard("COURSE_LIST"));
        view.getSideMenu().addLogoutListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                logout();
            }
        });

        // [신규 기능] 강의 추가 버튼 클릭 시 팝업 오픈
        coursePanel.getBtnAddCourse().addActionListener(e -> openAddCourseDialog());

        coursePanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view.showCard("ATTENDANCE");
                    loadAttendanceData();
                }
            }
        });

        attendancePanel.getBtnBack().addActionListener(e -> view.showCard("COURSE_LIST"));
        attendancePanel.getControlPanel().addBatchListener(e -> processBatchAttendance());
        attendancePanel.getControlPanel().addResetListener(e -> resetAllToUnprocessed());
    }

    // [신규 메서드] 강의 추가 다이얼로그 열기 및 처리
    private void openAddCourseDialog() {
        AddCourseDialog dialog = new AddCourseDialog(view);

        // 등록 버튼 클릭 이벤트
        dialog.getBtnSave().addActionListener(e -> {
            String code = dialog.getCode();
            String name = dialog.getCourseName();
            String room = dialog.getRoom();
            String time = dialog.getDayTime();
            int cap = dialog.getCapacity();

            if (code.isEmpty() || name.isEmpty() || room.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "모든 정보를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 테이블에 행 추가 {코드, 강의명, 강의실, 시간, 인원, 상태}
            DefaultTableModel model = (DefaultTableModel) coursePanel.getTable().getModel();
            model.addRow(new Object[]{code, name, room, time, cap + "명", "진행중"});

            JOptionPane.showMessageDialog(view, "새로운 강의가 개설되었습니다.");
            dialog.dispose(); // 창 닫기
        });

        // 취소 버튼
        dialog.getBtnCancel().addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void logout() {
        view.dispose();
        LoginFrame loginView = new LoginFrame();
        new LoginController(loginView);
        loginView.setVisible(true);
    }

    private void processBatchAttendance() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 3);
            String currentAtt = (String) model.getValueAt(i, 4);
            if ("재학".equals(status) && "미처리".equals(currentAtt)) {
                model.setValueAt("출석", i, 4);
            }
        }
        updateSummary();
        JOptionPane.showMessageDialog(view, "미처리 인원이 일괄 출석 처리되었습니다.");
    }

    private void resetAllToUnprocessed() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 3);
            if (!"휴학".equals(status) && !"자퇴".equals(status)) {
                model.setValueAt("미처리", i, 4);
                model.setValueAt("", i, 5);
            }
        }
        updateSummary();
    }

    private void updateSummary() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        int total = 0, present = 0, late = 0, absent = 0, pub = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = (String) model.getValueAt(i, 3);
            String att = (String) model.getValueAt(i, 4);

            if ("재학".equals(status)) {
                total++;
                if (att != null) {
                    switch (att) {
                        case "출석": present++; break;
                        case "지각": late++; break;
                        case "결석": absent++; break;
                        case "공결": pub++; break;
                    }
                }
            }
        }
        attendancePanel.getSummaryPanel().updateCounts(total, present, late, absent, pub);
    }

    private void loadAttendanceData() {
        DefaultTableModel model = attendancePanel.getTablePanel().getModel();
        model.setRowCount(0);
        model.addRow(new Object[]{"S001", "강민수", "컴퓨터공학과", "재학", "미처리", ""});
        model.addRow(new Object[]{"S002", "김영희", "컴퓨터공학과", "재학", "결석", "무단"});
        model.addRow(new Object[]{"S003", "박철수", "컴퓨터공학과", "재학", "지각", "버스 지연"});
        model.addRow(new Object[]{"S004", "이훈", "컴퓨터공학과", "재학", "미처리", ""});
        model.addRow(new Object[]{"S005", "정미경", "컴퓨터공학과", "재학", "미처리", ""});
        model.addRow(new Object[]{"S016", "휴학왕", "컴퓨터공학과", "휴학", "미처리", "군 입대"});
        updateSummary();
    }
}