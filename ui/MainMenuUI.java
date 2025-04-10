package mini.ui;

import mini.service.UserService;

import java.util.*;

public class MainMenuUI {
    static Scanner sc = new Scanner(System.in);
    private UserService userService;
    private GameUI gameUI;
    private UserUI userUI;

    public MainMenuUI(UserService userService, GameUI gameUI, UserUI userUI) {
        this.userService = userService;
        this.gameUI = gameUI;
        this.userUI = userUI;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    public void setUserUI(UserUI userUI) {
        this.userUI = userUI;
    }
    public MainMenuUI() {
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== 수학 게임 프로그램 =====");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    userUI.startLogin();
                    if (userService.isLoggedIn()) {
                        showMainMenu();
                    }
                    break;
                case 2:
                    userUI.startRegister();
                    if (userService.isLoggedIn()) {
                        showMainMenu();
                    }
                    break;
                case 0:
                    running = false;
                    System.out.println("프로그램을 종료합니다.");
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }

    }
    public void showMainMenu() {
        boolean loggedIn = true;

        while (loggedIn && userService.isLoggedIn()) {
            System.out.println("\n===== 메인 메뉴 =====");
            System.out.println("환영합니다, " + userService.getCurrentUser().getName() + "님!");
            System.out.println("1. 게임");
            System.out.println("2. 유저 정보");
            System.out.println("0. 로그아웃");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    gameUI.showGameMenu();
                    break;
                case 2:
                    userUI.userMenu();
                    break;
                case 0:
                    userUI.logout();
                    loggedIn = false;
                    System.out.println("로그아웃 되었습니다.");
                    start();
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }
    }
}

