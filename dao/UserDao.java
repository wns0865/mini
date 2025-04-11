package mini.dao;

import mini.model.GameResult;
import mini.model.User;

import java.io.*;
import java.util.*;

public class UserDao {
    GameResultDao gameResultDao = new GameResultDao();
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

        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(filePath, true));
            writer.write(user.toString());
            writer.write(System.lineSeparator());
            return true;
        } catch (IOException e) {
            System.err.println("사용자 저장 실패: " + e.getMessage());
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("저장 시 파일 닫기 실패: " + e.getMessage());
                }
            }
        }
    }

    public boolean update(User user) {
        List<User> users = findAll();
        boolean found = false;
        OutputStreamWriter writer = null;

        try {
            writer = new OutputStreamWriter(new FileOutputStream(filePath));
            for (User existingUser : users) {
                if (existingUser.getId().equals(user.getId())) {
                    writer.write(user.toString());
                    found = true;
                } else {
                    writer.write(existingUser.toString());
                }
                writer.write(System.lineSeparator());
            }
            //랭킹 정보 업데이트
            if (gameResultDao.getScore(1, user.getId()) != null) {
                //기존의 랭킹 가져옴
                GameResult gameResult1 = gameResultDao.getScore(1, user.getId());
                // 이름만 업데이트
                gameResult1.setName(user.getName());
                //변경해서 다시 서장
                gameResultDao.save(gameResult1);
            }
            if (gameResultDao.getScore(2, user.getId()) != null) {
                GameResult gameResult2 = gameResultDao.getScore(2, user.getId());
                gameResult2.setName(user.getName());
                gameResultDao.save(gameResult2);

            }
            return found;
        } catch (IOException e) {
            System.err.println("사용자 업데이트 실패: " + e.getMessage());
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("업데이트 시 파일 닫기 실패: " + e.getMessage());
                }
            }
        }
    }

    public void delete(User userToDelete) {
        gameResultDao.delete(1, userToDelete.getId());
        gameResultDao.delete(2, userToDelete.getId());
        String userId = userToDelete.getId();
        List<User> users = findAll();
        OutputStreamWriter writer = null;

        try {

            writer = new OutputStreamWriter(new FileOutputStream(filePath));
            for (User user : users) {
                if (!user.getId().equals(userId)) {
                    writer.write(user.toString());
                    writer.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 삭제 실패: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("삭제 시 파일 닫기 실패: " + e.getMessage());
                }
            }
        }
    }

    public User findById(String userId) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromString(line);
                if (user != null && user.getId().equals(userId)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 조회 실패: " + e.getMessage());
        } finally {
            try {
                if (reader != null) reader.close();
                if (isr != null) isr.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                System.err.println("조회 시 파일 닫기 실패: " + e.getMessage());
            }
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 목록 조회 실패: " + e.getMessage());
        } finally {
            try {
                if (reader != null) reader.close();
                if (isr != null) isr.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                System.err.println("목록 조회 시 파일 닫기 실패: " + e.getMessage());
            }
        }
        return users;
    }
}
