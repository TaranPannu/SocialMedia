package com.example.revision2;

public class ModelChat {
    String message, receiver, sender, timestamp;

    boolean isSeen;

    public ModelChat(boolean isSeen, String message,String receiver, String sender,String timestamp) {
        this.isSeen = isSeen;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;

    }

    public  ModelChat() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
