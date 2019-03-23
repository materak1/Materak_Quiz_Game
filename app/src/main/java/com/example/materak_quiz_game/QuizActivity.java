package com.example.materak_quiz_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import utils.Game;
import utils.GameDatabaseHelper;
import utils.User;

public class QuizActivity extends AppCompatActivity {

    Game new_game = new Game(0,0,0,"","",0,0,0);
    GameDatabaseHelper helper = GameDatabaseHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        final int user_id = intent.getIntExtra("ID", 0);
        Log.d("INTENT USER ID", String.valueOf(user_id));
        new_game.setUser_id(user_id);
        final EditText q2 = findViewById(R.id.q2_et);
        final EditText q3 = findViewById(R.id.q3_et);
        RadioButton iraq_btn = findViewById(R.id.iraq_btn);
        RadioButton iran_btn = findViewById(R.id.iran_btn);
        RadioButton uzbek_btn = findViewById(R.id.uzbek_btn);
        RadioButton afghan_btn = findViewById(R.id.afghan_btn);
        Button quiz_submit = findViewById(R.id.quiz_submit);
        final Snackbar mySnackbar = Snackbar.make(findViewById(R.id.quiz_layout), "Are you sure?", Snackbar.LENGTH_LONG);

        mySnackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.addGame(new_game);
                Log.d("SUBMIT", "CONFIRMED");
                Log.d("GAME SCORE", String.valueOf(new_game.getScore()));
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.putExtra("ID", user_id);
                startActivity(intent);
//                finish();
            }
        });

        quiz_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = 0;
                String q2_et = q2.getText().toString();
                new_game.setQuestion_2(q2_et);
                Log.d("Q2",q2_et);
                String q3_et = q3.getText().toString();
                new_game.setQuestion_3(q3_et);
                Log.d("Q3",q3_et);
                if (new_game.getQuestion_1() == 1) { score += 20;}
                if (new_game.getQuestion_2().contains("Beagle")) { score += 20;}
                if (new_game.getQuestion_3().equals("3")) { score += 20; }
                if (new_game.getQuestion_4() == 1) { score += 10; }
                if (new_game.getQuestion_4() > 1) { score += 20; }
                if (new_game.getQuestion_5() == 1) { score += 20; }
                new_game.setScore(score);
                Log.d("GAME SCORE", String.valueOf(new_game.getScore()));
                mySnackbar.show();
            }
        });

//        q2.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                String q2_et = q2.getText().toString();
//                new_game.setQuestion_2(q2_et);
//                Log.d("Q2",q2_et);
//                return false;
//            }
//        });
//
//        q3.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                String q3_et = q3.getText().toString();
//                new_game.setQuestion_3(q3_et);
//                Log.d("Q3",q3_et);
//                return false;
//            }
//        });

        iraq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_game.setQuestion_4(new_game.getQuestion_4()+1);
                Log.d("Q4","Iraq");
            }
        });

        iran_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new_game.setQuestion_4(new_game.getQuestion_4());
                Log.d("Q4","Iran");
            }
        });

        uzbek_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new_game.setQuestion_4(new_game.getQuestion_4());
                Log.d("Q4","Uzbekistan");
            }
        });

        afghan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_game.setQuestion_4(new_game.getQuestion_4()+1);
                Log.d("Q4","Afghanistan");
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.years_99:
                if (checked)
                    new_game.setQuestion_1(1);
                    Log.d("Q1","99 Years");
                    break;
            case R.id.years_100:
                if (checked)
                    new_game.setQuestion_1(2);
                    Log.d("Q1","100 Years");
                    break;
            case R.id.years_101:
                if (checked)
                    new_game.setQuestion_1(3);
                    Log.d("Q1","101 Years");
                    break;
            case R.id.years_116:
                if (checked)
                    new_game.setQuestion_1(4);
                    Log.d("Q1","116 Years");
                    break;
            case R.id.true_btn:
                if (checked)
                    new_game.setQuestion_5(1);
                Log.d("Q5","True");
                break;
            case R.id.false_btn:
                if (checked)
                    new_game.setQuestion_5(0);
                Log.d("Q5","False");
                break;
        }
    }
}
