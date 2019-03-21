package utils;

import android.text.TextUtils;

import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {

    int user_id;
    String first_name;
    String family_name;
    String date_of_birth;
    String email;
    String password;
    List<Game> games = new ArrayList<>();
//    ArrayList games;

    User() {}

    public User (String user_id, String first_name, String family_name, String date_of_birth, String email, String password) {
//        this.user_id = user_id;
        this.first_name = first_name;
        this.family_name = family_name;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.password = password;
    }

    public boolean checkFirst() {
        return this.first_name.length() < 3 || this.first_name.length() >= 30;
    }

    public boolean checkLast() {
        return this.family_name.length() >= 3 && this.family_name.length() < 30;
    }

    public boolean checkDob() {
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.date_of_birth);
        DateFormat df = new SimpleDateFormat("MM/dd/YYYY");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));
        return matcher.matches();
    }

    public boolean checkEmail() {
        if(TextUtils.isEmpty(this.email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(this.email).matches();
        }
    }

    public boolean checkPassword() {
        Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");
        Matcher matcher = pattern.matcher(this.password);
        return matcher.matches();
    }

    public void createAccount() {}

    public void validateLogin() {}
}
