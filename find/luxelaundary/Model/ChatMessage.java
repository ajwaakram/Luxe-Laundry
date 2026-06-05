package com.find.luxelaundary.Model;

public class ChatMessage {
    private String sendernAME;
    private String senderId;
    private String message;
    private long timestamp;

    public ChatMessage() {} // Required for Firebase


    public ChatMessage(String sendernAME, String senderId, String message, long timestamp) {
        this.sendernAME = sendernAME;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSendernAME() {
        return sendernAME;
    }

    public void setSendernAME(String sendernAME) {
        this.sendernAME = sendernAME;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
