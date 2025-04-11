package mini.ui;


import mini.model.User;
import mini.service.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class UserUI {
//    Scanner sc=new Scanner(System.in);
    BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
    private UserService userService;
    private MainMenuUI mainMenuUI;
    private UI ui;
    public void setMainMenuUI(MainMenuUI mainMenuUI) {
        this.mainMenuUI = mainMenuUI;
    }

    public UserUI(UserService userService, MainMenuUI mainMenuUI) {
        this.userService = userService;
        this.mainMenuUI = mainMenuUI;
    }

    public UserUI(UserService userService) {
        this.userService = userService;
    }

    public void startLogin() {
            try {

                System.out.println("\n===== 로그인 =====");
                System.out.print("아이디: ");
                String id = br.readLine();

                System.out.print("비밀번호: ");
                String password = br.readLine();

                if (userService.login(id, password)) {
                    System.out.println("로그인 성공!");

                } else {
                    System.out.println("로그인 실패. 아이디 또는 비밀번호를 확인해주세요.");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

    }

    public void startRegister() {
        try {


            System.out.println("\n===== 회원가입 =====");

            System.out.print("아이디: ");
            String id = br.readLine();

            System.out.print("비밀번호: ");
            String password = br.readLine();

            System.out.print("이름: ");
            String name =br.readLine();

            System.out.print("이메일: ");
            String email = br.readLine();

            User newUser = new User(id, password, name, email);

            if (userService.register(newUser)) {
                System.out.println("회원가입 성공! 로그인 됩니다.");
                userService.login(id, password);
            } else {
                System.out.println("회원가입 실패. 이미 존재하는 아이디입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void userMenu() {
        try {
            User user = userService.getCurrentUser();
            System.out.println("==== 유저 메뉴 ====");
            System.out.println("1. 유저 정보 수정");
            System.out.println("2. 회원 탈퇴");
            System.out.println("0. 뒤로 가기");
            System.out.print("선택: ");
            int choice = Integer.parseInt(br.readLine());
//            br.readLine();
            switch (choice) {
                case 1:

                    userService.update(updateUserInfo());
                    System.out.println("수정 완료되었습니다!");
                    break;
                case 2:
                    System.out.println("==== 정말 삭제하시겠습니까? ====");
                    System.out.println("1. 네");
                    System.out.println("2. 아니오");
                    System.out.print("선택: ");
                    int choice2 =  Integer.parseInt(br.readLine());
//                    br.readLine();
                    switch (choice2) {
                        case 1:
                            userService.delete(user);
                            System.out.println("삭제 완료되었습니다.");
                            userService.logOut();

                            break;
                        case 2:
                            userMenu();
                            break;

                    }
                    break;
                case 3:
                    mainMenuUI.showMainMenu();
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        userService.logOut();
    }
    public User updateUserInfo() {
        try {
            User user = userService.getCurrentUser();


            System.out.println("==== 유저 정보 수정 ====");
            System.out.println("현재 아이디: " + user.getId());
            System.out.println("현재 이름: " + user.getName());
            System.out.println("현재 이메일: " + user.getEmail());

            System.out.print("새 비밀번호 (Enter 시 변경 안 함): ");
            String newPassword = br.readLine();
            if (!newPassword.isBlank()) {
                user.setPassword(newPassword);
            }

            System.out.print("새 이름 (Enter 시 변경 안 함): ");
            String newName = br.readLine();
            if (!newName.isBlank()) {
                user.setName(newName);
            }

            System.out.print("새 이메일 (Enter 시 변경 안 함): ");
            String newEmail = br.readLine();
            if (!newEmail.isBlank()) {
                user.setEmail(newEmail);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }
}
