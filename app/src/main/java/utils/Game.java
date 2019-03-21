package utils;

public class Game {

    int user_id;
    int question_1;
    int question_2;
    int question_3;
    int question_4;
    int question_5;
    String score;

    public Game (int user_id, int question_1, int question_2, int question_3, int question_4, int question_5) {
        this.user_id = user_id;
        this.question_1 = question_1;
        this.question_2 = question_2;
        this.question_3 = question_3;
        this.question_4 = question_4;
        this.question_5 = question_5;
        score();
    }

    public void score() {
        this.score = String.valueOf(this.question_1 + this.question_2 + this.question_3 + this.question_4 + this.question_5/5);
    }
}
