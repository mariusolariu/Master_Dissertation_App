package com.example.myapplication.model;

public class UserInfo {
    public String user_name;
    public String email;

    public UserInfo() {

    }

    public UserInfo(String userName, String userEmail) {
        user_name = userName;
        email = userEmail;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
