package mini.ui;

import mini.model.User;
import mini.service.UserService;

import java.util.Scanner;

public class UserUI {
    Scanner sc=new Scanner(System.in);
    private UserService userService;

    public UserUI(UserService userService) {
        this.userService = userService;
    }

    public void startLogin() {
           System.out.println("\n===== 로그인 =====");
            System.out.print("아이디: ");
            String id =  sc.nextLine();

            System.out.print("비밀번호: ");
            String password = sc.nextLine();

            if (userService.login(id, password)) {
                System.out.println("로그인 성공!");
            } else {
                System.out.println("로그인 실패. 아이디 또는 비밀번호를 확인해주세요.");
            }

    }

    public void startRegister() {
        System.out.println("\n===== 회원가입 =====");

        System.out.print("아이디: ");
        String id = sc.nextLine();

        System.out.print("비밀번호: ");
        String password = sc.nextLine();

        System.out.print("이름: ");
        String name = sc.nextLine();

        System.out.print("이메일: ");
        String email = sc.nextLine();

        User newUser = new User(id, password, name, email);

        if (userService.register(newUser)) {
            System.out.println("회원가입 성공! 로그인 됩니다.");
            userService.login(id, password);
        } else {
            System.out.println("회원가입 실패. 이미 존재하는 아이디입니다.");
        }
    }


    public void userMenu() {
    }

    public void logout() {
    }
}
