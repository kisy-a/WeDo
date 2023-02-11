package com.example.wedoapp;

public class NotificationToken {
    private String token;

    public NotificationToken(String token) {
        this.token = token;
    }

    public NotificationToken(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
