package com.travel.travelshare.model;

import com.google.firebase.Timestamp;

public class Comment {
    private String content;
    private Timestamp createdAt;

    public Comment(String content, Timestamp createdAt) {
        this.setContent(content);
        this.setCreatedAt(createdAt);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
