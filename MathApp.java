package mini;

import mini.dao.UserDao;
import mini.service.GameService;
import mini.service.UserService;
import mini.ui.GameUI;
import mini.ui.MainMenuUI;
import mini.ui.UserUI;

import java.util.Scanner;

public class MathApp {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        UserService userService = new UserService(); // 기본 생성자 사용
        GameService gameService = new GameService();


        MainMenuUI menuUI = new MainMenuUI();
        GameUI gameUI = new GameUI(gameService,userService,menuUI);
        UserUI userUI = new UserUI(userService);


        menuUI.setUserService(userService);
        menuUI.setGameUI(gameUI);
        menuUI.setUserUI(userUI);
        userUI.setMainMenuUI(menuUI);

        try {
            System.out.println("===== 수학 게임 프로그램 시작 =====");
            menuUI.start();
        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("===== 프로그램 종료 =====");
        }
    }
}
