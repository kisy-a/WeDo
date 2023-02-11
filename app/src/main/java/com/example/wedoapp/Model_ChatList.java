package com.example.wedoapp;

public class Model_ChatList {
    private String ReceiverID;

    public Model_ChatList(String ReceiverID) {
        this.ReceiverID = ReceiverID;
    }

    public Model_ChatList() {
    }

    public String getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(String receiverID) {
        ReceiverID = receiverID;
    }
}

