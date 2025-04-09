package mini.dao;

import mini.model.User;

import java.io.*;
import java.util.*;
public class UserDao {
    private final String filePath = "C:\\Temp\\MiniProject\\Users\\users.txt";

    public UserDao() {
        initFile();
    }

    private void initFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File directory = new File("C:\\Temp\\MiniProject\\Users");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("파일 생성 실패: " + e.getMessage());
            }
        }
    }
    public boolean save(User user) {
        if (findById(user.getId()) != null) {
            return update(user);
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath, true))) {
            writer.write(user.toString());
            writer.write(System.lineSeparator());
            return true;
        } catch (IOException e) {
            System.err.println("사용자 저장 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean update(User user) {
        List<User> users = findAll();
        boolean found = false;

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath))) {
            for (User existingUser : users) {
                if (existingUser.getId().equals(user.getId())) {
                    writer.write(user.toString());
                    found = true;
                } else {
                    writer.write(existingUser.toString());
                }
                writer.write(System.lineSeparator());
            }
            return found;
        } catch (IOException e) {
            System.err.println("사용자 업데이트 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String userId) {
        List<User> users = findAll();
        boolean found = false;

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath))) {
            for (User user : users) {
                if (!user.getId().equals(userId)) {
                    writer.write(user.toString());
                    writer.write(System.lineSeparator());
                } else {
                    found = true;
                }
            }
            return found;
        } catch (IOException e) {
            System.err.println("사용자 삭제 실패: " + e.getMessage());
            return false;
        }
    }

    public User findById(String userId) {
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromString(line);
                if (user != null && user.getId().equals(userId)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 조회 실패: " + e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 목록 조회 실패: " + e.getMessage());
        }
        return users;
    }

}
