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

//    public GameResult play(User user) {
//        int r = 0;
//        int answerCnt = 0;
//        long startTime = System.currentTimeMillis();
//
//        while (r < 5) {
//            r++;
//            int answer = 0;
//            int userAnswer = 0;
//            System.out.println("\n" + r + "번 ----");
//            answer = generateArithmeticQuestion(answer);
//            System.out.print("정답을 입력하세요 (5초 이내): ");
//
//            final AtomicReference<String> inputHolder = new AtomicReference<>(null);
//            Thread inputThread = new Thread(() -> {
//                try {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//                    String input = reader.readLine();
//                    inputHolder.set(input);
//                } catch (IOException e) {
//                    // 무시
//                }
//            });
//
//            inputThread.start();
//            try {
//                inputThread.join(5000); // 5초 대기
//
//                if (inputThread.isAlive()) {
//                    inputThread.interrupt(); // 입력 스레드 강제 종료
//                    System.out.println("⏰ 시간 초과! 오답 처리됩니다.");
//                    continue;
//                }
//
//                String line = inputHolder.get();
//                if (line == null || line.trim().isEmpty()) {
//                    System.out.println("잘못된 입력입니다. 오답 처리됩니다.");
//                    continue;
//                }
//
//                userAnswer = Integer.parseInt(line.trim());
//
//            } catch (Exception e) {
//                System.out.println("입력 처리 중 오류 발생: " + e.getMessage());
//                continue;
//            }
//
//            if (checkArithmeticAnswer(answer, userAnswer)) {
//                answerCnt++;
//            }
//        }
//
//        long endTime = System.currentTimeMillis();
//        double elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        return new GameResult(1, user.getId(), user.getName(), answerCnt, elapsedTimeSeconds);
//    }


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
            Future<Integer> future = executor.submit(() -> {
                try {
                    String line = br.readLine();
                    return Integer.parseInt(line.trim());
                } catch (Exception e) {
                    return null;
                }
            });

            try {
                Integer result = future.get(5, TimeUnit.SECONDS);
                if (result == null) {
                    System.out.println("잘못된 입력입니다. 오답 처리됩니다.");
                    continue;
                }
                userAnswer = result;
            } catch (TimeoutException e) {
                System.out.println("⏰ 시간 초과! 오답 처리됩니다.");
                future.cancel(true);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(answer);
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
                // 음수 결과를 피하기 위해 큰 수에서 작은 수를 빼도록 조정
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
                operator = "/";
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


    public boolean checkEquationAnswer(String question, String userAnswer) {
        try {
            // 예: "2x + 3 = 7" 에서 x = 2
            int x = Integer.parseInt(userAnswer);

            String[] parts = question.split("=");
            String equation = parts[0].trim();
            int result = Integer.parseInt(parts[1].trim());

            // 계수 a 추출 (예: "2x + 3"에서 2)
            String[] termParts = equation.split("x");
            int a = Integer.parseInt(termParts[0].trim());

            // 상수항 b 추출 (예: "+ 3"에서 3)
            int b = Integer.parseInt(termParts[1].trim());

            return (a * x + b) == result;
        } catch (Exception e) {
            return false;
        }
    }

    public void getRank() {
        List<GameResult> list1=gameResultDao.getRank(1);
        List<GameResult> list2=gameResultDao.getRank(2);
        System.out.println("============ 사칙연산 ============");
        System.out.println("등수      이름        점수      시간");
        int i=0;
        for (GameResult gameResult1 : list1) {

            System.out.print(++i+"등 ");
            System.out.println(gameResult1.showRanking());
        }
        System.out.println("\n");
        i=0;
        System.out.println("============ 방정식 ============");
        System.out.println("등수      이름        점수      시간");
        for (GameResult gameResult2 : list2) {
            System.out.print(++i+"등 ");
            System.out.println(gameResult2.showRanking());
        }
        System.out.println("\n\n");
    }

//    public boolean checkArithmeticAnswer(String question, String userAnswer) {
//        try {
//            String[] parts = question.split("=");
//            int correctAnswer = Integer.parseInt(parts[1]);
//            int providedAnswer = Integer.parseInt(userAnswer);
//            return correctAnswer == providedAnswer;
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
