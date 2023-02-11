package com.example.wedoapp;

public class NotificationSender {
    public NotificationData data;
    public String to;

    public NotificationSender(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }
}
