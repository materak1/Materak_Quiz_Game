package com.example.materak_quiz_game;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import utils.GameDatabaseHelper;
import utils.User;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = (Button) findViewById(R.id.btn_login);
        Button button2 = (Button) findViewById(R.id.btn_reg);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInput();
            }
        });
    }

    private void startBtn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void processInput() {
        EditText first_name = (EditText) findViewById(R.id.edit_first);
        String first = first_name.getText().toString();
        Log.d("first", first);
        EditText last_name = (EditText) findViewById(R.id.edit_family);
        String last = last_name.getText().toString();
        Log.d("last", last);
        EditText date_of_birth = (EditText) findViewById(R.id.edit_dob);
        String dob = date_of_birth.getText().toString();
        Log.d("dob", dob);
        EditText email = (EditText) findViewById(R.id.edit_email);
        String email_new = email.getText().toString();
        Log.d("email", email_new);
        EditText password = (EditText) findViewById(R.id.edit_pass);
        String pass = password.getText().toString();
        Log.d("password", pass);
        User user = new User(0,first, last, dob, email_new, pass);

        if(user.checkFirst()) {
            showError("Invalid First Name");
        }

        if(!user.checkLast()) {
            showError("Invalid Family Name");
        }

        if(!user.checkDob()) {
            showError("Invalid Date of Birth");
        }

        if(!user.checkEmail()) {
            showError("Invalid Email");
        }

        if(!user.checkPassword()) {
            showError("Invalid Password, it must meet the following criteria: Be between 8 and 40 characters long\n" +
                    "Contain at least one digit.\n" +
                    "Contain at least one lower case character.\n" +
                    "Contain at least one upper case character.\n" +
                    "Contain at least on special character from [ @ # $ % ! . ].");
        } else {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            GameDatabaseHelper helper = GameDatabaseHelper.getInstance(this);
            long new_id = helper.addOrUpdateUser(user);
            String id_msg = "New ID is " + String.valueOf(new_id);
            Log.d("SQL", id_msg);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
//            SecureRandom random = new SecureRandom();
//            byte[] salt = new byte[16];
//            random.nextBytes(salt);
//            KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
//            SecretKeyFactory factory = null;
//            try {
//                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//            try {
//                byte[] hash = factory.generateSecret(spec).getEncoded();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void showError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
}
