package mini.ui;

import mini.service.GameService;
import mini.service.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MathApp {
    public void math (){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
                                break;
                            case 2:
                                ui.startRegister();
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

        }catch (NumberFormatException e){
            System.out.println("숫자만 입력해주세요");
        }
        catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
    }
    }
}
