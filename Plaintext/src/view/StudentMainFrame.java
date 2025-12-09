package view;

import view.component.StudentSideMenuPanel;
import view.panel.StudentDashboardPanel;
import view.panel.StudentEnrolmentPanel; // 추가됨
import view.panel.StudentMyCoursePanel;  // 추가됨

import javax.swing.*;
import java.awt.*;

public class StudentMainFrame extends JFrame {
    private StudentSideMenuPanel sideMenu;
    private JPanel mainContainer;
    private CardLayout cardLayout;

    // 패널들
    private StudentDashboardPanel dashboardPanel;
    private StudentEnrolmentPanel enrolmentPanel;
    private StudentMyCoursePanel myCoursePanel;

    public StudentMainFrame() {
        setTitle("학사 관리 시스템 - [학생 모드]");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sideMenu = new StudentSideMenuPanel();
        add(sideMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 패널 생성 및 추가
        dashboardPanel = new StudentDashboardPanel();
        enrolmentPanel = new StudentEnrolmentPanel();
        myCoursePanel = new StudentMyCoursePanel();

        mainContainer.add(dashboardPanel, "DASHBOARD");
        mainContainer.add(enrolmentPanel, "ENROLMENT");
        mainContainer.add(myCoursePanel, "MY_COURSE");

        add(mainContainer, BorderLayout.CENTER);
    }

    public StudentSideMenuPanel getSideMenu() { return sideMenu; }
    public JPanel getMainContainer() { return mainContainer; }
    public void showCard(String name) { cardLayout.show(mainContainer, name); }

    // Getters
    public StudentDashboardPanel getDashboardPanel() { return dashboardPanel; }
    public StudentEnrolmentPanel getEnrolmentPanel() { return enrolmentPanel; }
    public StudentMyCoursePanel getMyCoursePanel() { return myCoursePanel; }
}