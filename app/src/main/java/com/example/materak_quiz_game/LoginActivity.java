package com.example.materak_quiz_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.btn_reg);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn();
            }
        });
    }

    private void startBtn() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
