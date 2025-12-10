package view;

import controller.LoginController; // 컨트롤러 연결 필요
import view.component.SideMenuPanel;
import view.panel.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private SideMenuPanel sideMenu;

    private ProfessorDashboardPanel dashboardPanel;
    private CoursePanel coursePanel;
    private SchedulePanel schedulePanel;
    private AttendancePanel attendancePanel;

    public MainFrame() {
        setTitle("학사 관리 시스템 - [교수 모드]");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sideMenu = new SideMenuPanel();
        add(sideMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        dashboardPanel = new ProfessorDashboardPanel();
        coursePanel = new CoursePanel();
        schedulePanel = new SchedulePanel();
        attendancePanel = new AttendancePanel();

        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(coursePanel, "Course");
        contentPanel.add(schedulePanel, "Schedule");
        contentPanel.add(attendancePanel, "Attendance");

        add(contentPanel, BorderLayout.CENTER);

        initMenuActions();

        setVisible(true);
    }

    private void initMenuActions() {
        sideMenu.addDashboardListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        sideMenu.addCourseMgmtListener(e -> cardLayout.show(contentPanel, "Course"));

        sideMenu.addCalendarListener(e -> {
            schedulePanel.loadScheduleFromDB();
            cardLayout.show(contentPanel, "Schedule");
        });

        sideMenu.addAttendanceListener(e -> {
            attendancePanel.loadLectureList();
            cardLayout.show(contentPanel, "Attendance");
        });

        // ★ [여기가 문제였습니다] 순서 변경: 새 창 열기 -> 헌 창 닫기
        sideMenu.addLogoutListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                try {
                    // 1. 로그인 화면을 먼저 생성 (Controller 연결 필수)
                    LoginFrame loginFrame = new LoginFrame();
                    new LoginController(loginFrame); // ★ 이게 없으면 로그인 버튼 안 눌림
                    loginFrame.setVisible(true); // ★ 화면에 보이게 설정

                    // 2. 그 다음 현재 창(교수 메인)을 닫음
                    this.dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "오류 발생: " + ex.getMessage());
                }
            }
        });
    }
}