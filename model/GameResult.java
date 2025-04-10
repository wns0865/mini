package mini.model;

public class GameResult {
    private int type;
    private String id;
    private String name;
    private int answerCount;
    private double totalTime;


    public GameResult(int type, String id, String name, int answerCount, double totalTime) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.answerCount = answerCount;
        this.totalTime = totalTime;
    }

    public static GameResult fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length == 5) {
            return new GameResult(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4]));
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return  type+","+id + ',' + name + "," + answerCount + "," + totalTime;
    }
    public String showRanking() {
        return "        "+name + "        " + answerCount + "개       " + totalTime;
    }
}
