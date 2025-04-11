package mini.ui;

import mini.service.GameService;
import mini.service.UserService;

public class MathApp {
    public void math (){

        UserService userService = new UserService();
        GameService gameService = new GameService();

        MainMenuUI mainMenuUI = new MainMenuUI(userService);
        GameUI gameUI = new GameUI(gameService, userService,mainMenuUI);
        UserUI userUI = new UserUI(userService);

        UI ui = new UIImpl(gameUI,mainMenuUI,userUI);
        mainMenuUI.setMediator(ui);
        gameUI.setUi(ui);
        userUI.setUi(ui);

        try {
            System.out.println("===== 수학 게임 프로그램 시작 =====");
            ui.start();
        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
    }
    }
}
