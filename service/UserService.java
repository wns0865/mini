package mini.service;

import mini.dao.UserDao;
import mini.model.User;

public class UserService {
    private UserDao userDAO;
    private User currentUser;

    public UserService() {
    }

    public UserService(UserDao userDAO, User currentUser) {
        this.userDAO = userDAO;
        this.currentUser = currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean login(String userId, String password) {
        User user = userDAO.findById(userId);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(User newUser) {
        if(userDAO.findById(newUser.getId()) == null) {
            userDAO.save(newUser);
            return true;
        }
        else return false;
    }
}
