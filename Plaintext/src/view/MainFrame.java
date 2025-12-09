package view;

import view.component.SideMenuPanel;
import view.panel.DashboardPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private SideMenuPanel sideMenu;
    private JPanel mainContainer;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("학사 관리 시스템 - [교수 모드]");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sideMenu = new SideMenuPanel();
        add(sideMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(new DashboardPanel(), "DASHBOARD");

        add(mainContainer, BorderLayout.CENTER);
    }

    public SideMenuPanel getSideMenu() { return sideMenu; }
    public void showCard(String name) { cardLayout.show(mainContainer, name); }
    public JPanel getMainContainer() { return mainContainer; }
}