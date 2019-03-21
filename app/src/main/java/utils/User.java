package utils;

public class User {

    String first_name;
    String family_name;
    String date_of_birth;
    String email;
    String password;

    public User (String first_name, String family_name, String date_of_birth, String email, String password) {
        this.first_name = first_name;
        this.family_name = family_name;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.password = password;
    }
}
