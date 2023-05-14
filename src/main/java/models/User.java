package models;

import com.github.javafaker.Faker;

public class User {

    static Faker faker = new Faker();

    private String email;
    private String password;
    private String name;

    public User() {

    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User generateUser() {
        return new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
    }

}
