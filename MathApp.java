package mini;

import mini.service.UserService;
import mini.ui.GameUI;
import mini.ui.MainMenuUI;
import mini.ui.UserUI;

public class MathApp {

    public static void main(String[] args) {

        UserService userService = new UserService();
        UserUI userUI = new UserUI(userService);
        GameUI gameUI = new GameUI();
        MainMenuUI menuUI = new MainMenuUI(userService, userUI, gameUI);

        menuUI.start();

        MainMenuUI mainMenuUI = new MainMenuUI();
        mainMenuUI.start();
    }
}
