package controller;

import view.MainFrame;

public class MainController {

    private MainFrame view;

    public MainController(MainFrame view) {
        this.view = view;
        // MainFrame 내부에서 화면 전환을 자체적으로 처리하므로
        // 여기서는 별도의 리스너 등록 코드가 필요 없습니다.
        view.setVisible(true);
    }
}