package com.example.study;

public class MessageModel {
    private String messageId; // Added for a unique identifier
    private String message;
    private String senderId;
    private long timestamp;

    // Required default constructor for Firebase
    public MessageModel() {
        // Firebase uses this default constructor for deserialization
    }

    // Constructor to initialize the message model
    public MessageModel(String messageId, String message, String senderId, long timestamp) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public MessageModel(String message, String senderUid, long time) {
    }

    // Getter and Setter methods for the messageId
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // Getter and Setter methods for the message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and Setter methods for the sender ID
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    // Getter and Setter methods for the timestamp
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Optional: Override toString() for easier debugging
    @Override
    public String toString() {
        return "MessageModel{" +
                "messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    // Optional: Add other utility methods as needed
}
