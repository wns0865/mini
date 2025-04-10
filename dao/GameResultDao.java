package mini.dao;

import mini.model.GameResult;
import mini.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameResultDao {
    private final String filePath = "C:\\Temp\\MiniProject\\Games\\results.txt";

    public GameResultDao() {
        initFile();
    }

    private void initFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File directory = new File("C:\\Temp\\MiniProject\\Games");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("파일 생성 실패: " + e.getMessage());
            }
        }
    }

    public GameResult getScore(int type, String userId) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                GameResult result = GameResult.fromString(line);
                if (result != null && result.getId().equals(userId)&& result.getType() == type) {
                    return result;
                }
            }

        } catch (IOException e) {
            System.err.println("사용자 기록 조회 실패: " + e.getMessage());
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

    public void save(GameResult gameResult) {
        GameResult originResult = getScore(gameResult.getType(), gameResult.getId());
        OutputStreamWriter writer = null;
        try{
            if(originResult == null){
                writer = new OutputStreamWriter(new FileOutputStream(filePath, true));
                writer.write(gameResult.toString());
                writer.write(System.lineSeparator());
            }
            else{
                delete(gameResult.getType(), gameResult.getId());
                writer = new OutputStreamWriter(new FileOutputStream(filePath, true));
                writer.write(gameResult.toString());
                writer.write(System.lineSeparator());
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("저장 시 파일 닫기 실패: " + e.getMessage());
                }
            }
        }
    }
    public void delete(int type, String userId) {
        List<GameResult> list = findAll();
        boolean found = false;
        OutputStreamWriter writer = null;
        try{
            writer = new OutputStreamWriter(new FileOutputStream(filePath));
            for (GameResult gameResult : list) {
                if(!gameResult.getId().equals(userId)){
                    writer.write(gameResult.toString());
                    writer.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.err.println("사용자 삭제 실패: " + e.getMessage());
        }finally{
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("삭제 시 파일 닫기 실패: " + e.getMessage());
                }
            }
        }
    }
    public List<GameResult> findAll() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        List<GameResult> list = new ArrayList<GameResult>();
        try{
            fis= new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                GameResult gameResult = GameResult.fromString(line);
                if (gameResult != null) {
                    list.add(gameResult);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (reader != null) reader.close();
                if (isr != null) isr.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                System.err.println("목록 조회 시 파일 닫기 실패: " + e.getMessage());
            }
        }
        return list;
    }

    public List<GameResult> getRank(int type){
        List<GameResult> allList = findAll();
        List<GameResult> filterdList=new ArrayList<>();
        for (GameResult gameResult : allList) {
            if(gameResult.getType()==type){
                filterdList.add(gameResult);
            }
        }
        filterdList.sort((r1,r2)->{
          if(r1.getAnswerCount()!=r2.getAnswerCount()){
            return Integer.compare(r2.getAnswerCount(),r1.getAnswerCount());
          }
          else return Double.compare(r1.getTotalTime(),r2.getTotalTime());
        });

        return filterdList;
    }
}
