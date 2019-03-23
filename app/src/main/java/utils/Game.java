package utils;

public class Game {

    int game_id;
    int user_id;
    int question_1;
    String question_2;
    String question_3;
    int question_4;
    int question_5;
    int score;
    User user;

    public Game() {}

    public Game (int game_id, int user_id, int question_1, String question_2, String question_3, int question_4, int question_5, int score) {
        this.user_id = user_id;
        this.question_1 = question_1;
        this.question_2 = question_2;
        this.question_3 = question_3;
        this.question_4 = question_4;
        this.question_5 = question_5;
        this.score = score;
    }


    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuestion_1() {
        return question_1;
    }

    public void setQuestion_1(int question_1) {
        this.question_1 = question_1;
    }

    public String getQuestion_2() {
        return question_2;
    }

    public void setQuestion_2(String question_2) {
        this.question_2 = question_2;
    }

    public String getQuestion_3() {
        return question_3;
    }

    public void setQuestion_3(String question_3) {
        this.question_3 = question_3;
    }

    public int getQuestion_4() {
        return question_4;
    }

    public void setQuestion_4(int question_4) {
        this.question_4 = question_4;
    }

    public int getQuestion_5() {
        return question_5;
    }

    public void setQuestion_5(int question_5) {
        this.question_5 = question_5;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
