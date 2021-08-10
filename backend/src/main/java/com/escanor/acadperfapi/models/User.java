package com.escanor.acadperfapi.models;

public class User {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String kind;
    private String username;
    private String password;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(Integer userId, String firstName, String lastName, String kind, String username, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.kind = kind;
        this.username = username;
        this.password = password;
    }
}
