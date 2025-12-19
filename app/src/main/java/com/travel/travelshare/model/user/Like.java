package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;

public class Like {
    private String fromUserId;
    private String postId;
    private Timestamp createdAt;

    public Like() {}

    public Like(String fromUserId, String postId, Timestamp createdAt) {
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
}
