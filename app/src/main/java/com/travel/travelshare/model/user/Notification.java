package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;

public class Notification {
    private String message;
    private String category;
    private Timestamp createdAt;

    public Notification(String message, String category, Timestamp createdAt) {
        this.setMessage(message);
        this.setCategory(category);
        this.setCreatedAt(createdAt);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
