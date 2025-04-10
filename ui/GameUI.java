package mini.ui;

import mini.model.GameResult;
import mini.model.User;
import mini.service.GameService;
import mini.service.UserService;

import java.util.List;
import java.util.Scanner;

public class GameUI {
    private UI ui;
    private GameService gameService;
    private UserService userService;
    private MainMenuUI mainMenuUI;


    public GameUI(UserService userService) {
        this.userService = userService;
    }

    public GameUI(GameService gameService, UserService userService, MainMenuUI mainMenuUI) {
        this.gameService = gameService;
        this.userService = userService;
        this.mainMenuUI = mainMenuUI;
    }

    public GameUI(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;

    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public void setMainMenuUI(MainMenuUI mainMenuUI) {
        this.mainMenuUI = mainMenuUI;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    Scanner sc = new Scanner(System.in);
    public void showGameMenu() {
        User user = userService.getCurrentUser();
        System.out.println("==== 게임을 선택하세요 ====");
        System.out.println("1. 사칙연산");
        System.out.println("2. 방정식");
        System.out.println("3. 랭킹 조회");
        System.out.println("0. 뒤로 가기");
        System.out.print("선택: ");
        int choice = sc.nextInt();
        sc.nextLine();
        GameResult gameResult=null;
        switch (choice) {
            case 1:
                System.out.println("----- 🚩사칙 연산 게임 시작🚩 -----");
                 gameResult = gameService.play(1,user);
                System.out.println("\n----- 결과 -----");
                System.out.println("정답 수 : "+gameResult.getAnswerCount());
                System.out.println("소요 시간 : "+gameResult.getTotalTime());
                if(gameService.compareScore(gameResult)){
                    gameService.save(gameResult);
                    System.out.println("🎉신기록 달성!🎉");
                }else{
                    System.out.println("실패 !");
                }
                break;
            case 2:
                System.out.println("----- 🚩방정식 게임 시작🚩 -----");
                gameResult = gameService.play(2,user);
                System.out.println("\n----- 결과 -----");
                System.out.println("정답 수 : "+gameResult.getAnswerCount());
                System.out.println("소요 시간 : "+gameResult.getTotalTime());
                if(gameService.compareScore(gameResult)){
                    gameService.save(gameResult);
                    System.out.println("🎉신기록 달성!🎉");
                }else{
                    System.out.println("실패 !");
                }
                break;
            case 3:
                System.out.println("------------ 🏆랭킹🏆 ------------\n");
                gameService.getRank();
                showGameMenu();
                break;
            case 0:
                mainMenuUI.showMainMenu();
                break;
            default:
                System.out.println("잘못된 선택입니다. 다시 선택해주세요.");

        }

    }


    public void setUi(UI ui) {
        this.ui=ui;
    }
}
