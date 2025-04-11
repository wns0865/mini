package mini.service;

import mini.dao.GameResultDao;
import mini.model.GameResult;
import mini.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class GameService {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Scanner sc = new Scanner(System.in);
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private GameResultDao gameResultDao;
    private Random random;

    public GameService() {
        this.gameResultDao = new GameResultDao();
        this.random = new Random();
    }


    public GameResult play(int type, User user) {
        int r = 0;
        int answerCnt = 0;
        long startTime = System.currentTimeMillis();
        while (r < 5) {
            r++;
            int answer = 0;
            int userAnswer = 0;
            System.out.println("\n" + r + "번 ----");
            if (type == 1) {
                answer = generateArithmeticQuestion();
            }
            if (type == 2) {
                answer = generateEquationQuestion();
            }
            System.out.print("정답을 입력하세요 (5초 이내): ");
            Future<Integer> future = executor.submit(() -> Integer.parseInt(br.readLine()));

            try {
                Integer result = future.get(5, TimeUnit.SECONDS);
                if (result == null) {
                    System.out.println("잘못된 입력입니다. 오답 처리됩니다.");
                    continue;
                }
                userAnswer = result;
            }
            catch (TimeoutException e) {
                System.out.println("⏰ 시간 초과! 오답 처리됩니다.");
                future.cancel(true);
                System.out.println("정답: "+answer+"\nEnter를 눌러주세요");
                try {
                    while (br.ready()) {
                        br.read();
                    }
                } catch (IOException ioe) {
                    // 예외 처리
                }

                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }finally {

            }
            System.out.println("정답: "+answer);
            if (checkArithmeticAnswer(answer, userAnswer)) {
                answerCnt++;
            }
        }
        long endTime = System.currentTimeMillis();
        double elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        return new GameResult(type, user.getId(), user.getName(), answerCnt, elapsedTimeSeconds);
    }


    public int generateArithmeticQuestion() {
        int num1 = random.nextInt(50) + 1;
        int num2 = random.nextInt(50) + 1;
        int operatorIndex = random.nextInt(4);
        int answer = 0;
        String operator;

        switch (operatorIndex) {
            case 0:
                operator = "+";
                answer = num1 + num2;
                break;
            case 1:
                operator = "-";
                // 큰 수에서 작은 수를 빼도록 조정
                if (num1 < num2) {
                    int temp = num1;
                    num1 = num2;
                    num2 = temp;
                }
                answer = num1 - num2;
                break;
            case 2:
                operator = "*";
                // 곱셈은 더 작은 수로 조정
                num1 = random.nextInt(20) + 1;
                num2 = random.nextInt(10) + 1;
                answer = num1 * num2;
                break;
            default:
                operator = "÷";
                // 나눗셈은 나누어 떨어지는 경우만 생성
                num2 = random.nextInt(30) + 1;
                answer = random.nextInt(10) + 1;
                num1 = num2 * answer;
                break;
        }
        System.out.println(num1 + " " + operator + " " + num2);
        return answer;
    }

    public boolean checkArithmeticAnswer(int answer, int userAnswer) {
        try {
            return answer == userAnswer;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean compareScore(GameResult gameResult) {
        GameResult originResult = gameResultDao.getScore(gameResult.getType(), gameResult.getId());
        if (originResult == null) {
            return true;
        } else if (originResult.getAnswerCount() < gameResult.getAnswerCount()) {
            return true;
        } else if (originResult.getAnswerCount() == gameResult.getAnswerCount()
                && originResult.getTotalTime() > gameResult.getTotalTime()
        ) {
            return true;
        } else return false;
    }

    public void save(GameResult gameResult) {
        gameResultDao.save(gameResult);
    }

    public int generateEquationQuestion() {
        int a;
        do {
            a = random.nextInt(19) - 9; // -9부터 9까지 (0 제외)
        } while (a == 0); // a는 0이 되면 안 됨

        int x = random.nextInt(5) + 1; // x는 1부터 5까지
        int b = random.nextInt(20) +1; //
        int operatorIndex = random.nextInt(2); // + 또는 -

        int y = 0;
        String operator = "";
        switch (operatorIndex) {
            case 0: // + 연산
                operator = "+";
                y = a * x + b;
                break;
            case 1: // - 연산
                operator = "-";
                y = a * x - b;
                break;
        }
        System.out.println(a + "x" +operator+b+"="+y);

        // 방정식 형태: ax + b = result
        return x;
    }


    public void getRank() {
        List<GameResult> list1=gameResultDao.getRank(1);
        List<GameResult> list2=gameResultDao.getRank(2);
        System.out.println("============ 사칙연산 ============");
        System.out.println("등수        이름        점수      시간");
        int i=0;
        for (GameResult gameResult1 : list1) {

            System.out.print(++i+"등 ");
            System.out.println(gameResult1.showRanking());

        }
        System.out.println("\n");
        i=0;
        System.out.println("============ 방정식 ============");
        System.out.println("등수        이름        점수      시간");
        for (GameResult gameResult2 : list2) {
            System.out.print(++i+"등 ");
            System.out.println(gameResult2.showRanking());
        }
        System.out.println("\n\n");
    }

}
