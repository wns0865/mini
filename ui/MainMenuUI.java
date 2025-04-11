package mini.ui;

import mini.service.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class MainMenuUI {
    private UserService userService;
    private UI ui;

    public MainMenuUI(UserService userService) {
        this.userService = userService;
    }

    public void setMediator(UI ui) {
        this.ui = ui;
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void start() {
        try {
            while (true) {
                System.out.println("\n===== 수학 게임 프로그램 =====");
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("0. 종료");
                System.out.print("선택: ");

                int choice = Integer.parseInt(br.readLine());
//                br.readLine();
                switch (choice) {
                    case 1:
                        ui.startLogin();
                        if (userService.isLoggedIn()) {
                            showMainMenu();
                        }
                        break;
                    case 2:
                        ui.startRegister();
                        if (userService.isLoggedIn()) {
                            showMainMenu();
                        }
                        break;
                    case 0:
                        System.out.println("프로그램을 종료합니다.");
                        System.out.println("===== 프로그램 종료 =====");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void showMainMenu() {
        try{
        boolean loggedIn = true;

        while (loggedIn && userService.isLoggedIn()) {
            System.out.println("\n===== 메인 메뉴 =====");
            System.out.println("환영합니다, " + userService.getCurrentUser().getName() + "님!");
            System.out.println("1. 게임");
            System.out.println("2. 유저 정보");
            System.out.println("0. 로그아웃");
            System.out.print("선택: ");

            int choice = Integer.parseInt(br.readLine());
//            br.readLine();
            switch (choice) {
                case 1:
                    ui.gameMenu();
                    break;
                case 2:
                    ui.userMenu();
                    break;
                case 0:
                    ui.logout();
                    loggedIn = false;
                    System.out.println("로그아웃 되었습니다.");
//                    start();
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }
    } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

