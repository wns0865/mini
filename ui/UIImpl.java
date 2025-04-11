package mini.ui;

public class UIImpl implements UI {
    private MainMenuUI mainMenuUI;
    private GameUI gameUI;
    private UserUI userUI;

    public UIImpl(GameUI gameUI, MainMenuUI mainMenuUI, UserUI userUI) {
        this.gameUI = gameUI;
        this.mainMenuUI = mainMenuUI;
        this.userUI = userUI;
    }

    @Override
    public void mainMenu() {
        mainMenuUI.showMainMenu();
    }

    @Override
    public void gameMenu() {
        gameUI.showGameMenu();

    }

    @Override
    public void userMenu() {
        userUI.userMenu();
    }

    @Override
    public void logout() {
        userUI.logout();
//        mainMenuUI.start();
    }


    @Override
    public void startLogin() {
        userUI.startLogin();
    }

    @Override
    public void startRegister() {
        userUI.startRegister();
    }
    


}
