package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

public class Like implements DatabaseItem {
    private String id;
    private String fromUserId;
    private String postId;
    private Timestamp createdAt;

    public Like() {}

    public Like(String id, String fromUserId, String postId, Timestamp createdAt) {
        this.setId(id);
        this.setFromUserId(fromUserId);
        this.setPostId(postId);
        this.createdAt = createdAt;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
