package com.example.materak_quiz_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import utils.GameDatabaseHelper;
import utils.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.btn_reg);
        Button login = findViewById(R.id.btn_login);

//        user.email = emailEt.getText().toString();
//        user.password = passwordEt.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String pass_msg = "Login: current password is " + user.getPassword();
//                Log.d("SQL", pass_msg);
                Log.d("SQL", "Clicked login");
                login();
            }
        });
    }

    private void startBtn() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        EditText emailEt = findViewById(R.id.edit_email);
        EditText passwordEt = findViewById(R.id.edit_pass);
        GameDatabaseHelper helper = GameDatabaseHelper.getInstance(this);
//        List ysers = helper.getAllUsers();
        User user = new User(0,"","","",emailEt.getText().toString(),passwordEt.getText().toString());

        if (!user.checkEmail()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();

        }
        if (!user.checkPassword()) {
            Toast.makeText(this, "Invalid Password, it must meet the following criteria: Be between 8 and 40 characters long\n" +
                    "Contain at least one digit.\n" +
                    "Contain at least one lower case character.\n" +
                    "Contain at least one upper case character.\n" +
                    "Contain at least one special character from [ @ # $ % ! . ].", Toast.LENGTH_SHORT).show();
        }
        else {
            User user_result = helper.getUser(user);
            String email = user_result.getEmail();
            int user_id = user_result.getUser_id();
            if (email.isEmpty()) {
                Toast.makeText(this, "Invalid email and password combination", Toast.LENGTH_SHORT).show();
            } else {
                String user_msg = "Email: " + email;
                String id_msg = "ID: " + String.valueOf(user_id);
                Log.d("SQL", user_msg);
                Log.d("SQL", id_msg);
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("ID", user_id);
                startActivity(intent);
            }
        }

    }
}
