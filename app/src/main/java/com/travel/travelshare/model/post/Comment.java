package com.travel.travelshare.model.post;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

public class Comment implements DatabaseItem {
    private String id;
    private String authorId;
    private String content;
    private Timestamp createdAt;

    public Comment() {}

    public Comment(String id, String authorId, String content, Timestamp createdAt) {
        this.setId(id);
        this.setAuthorId(authorId);
        this.setContent(content);
        this.setCreatedAt(createdAt);
    }

    private void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    private String getAuthorId(String authorId) {
        return this.authorId;
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

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
