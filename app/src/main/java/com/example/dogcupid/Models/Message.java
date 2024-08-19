package com.example.dogcupid.Models;

public class Message {
    private String messageId;
    private String senderId;
    private String message;
    private long timestamp;


    public Message() {
    }

    public Message(String messageId, String senderId, String message, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public Message setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public Message setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Message setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
