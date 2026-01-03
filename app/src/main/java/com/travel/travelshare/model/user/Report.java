package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

public class Report implements DatabaseItem {
    private String message;
    private String category;
    private Timestamp createdAt;
    private String id;

    public Report() {}

    public Report(String id, String message, String category, Timestamp createdAt) {
        this.setId(id);
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

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
