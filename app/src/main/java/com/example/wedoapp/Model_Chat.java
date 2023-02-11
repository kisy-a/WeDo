package com.example.wedoapp;

public class Model_Chat {

    private String MID, Message, ReceiverID, ReceiverName, SenderID, MessageType;

    public Model_Chat() {
    }


    public Model_Chat(String MID, String Message, String ReceiverID, String ReceiverName, String SenderID, String MessageType) {
        this.MID = MID;
        this.Message = Message;
        this.ReceiverID = ReceiverID;
        this.ReceiverName = ReceiverName;
        this.SenderID = SenderID;
        this.MessageType = MessageType;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(String receiverID) {
        ReceiverID = receiverID;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

}