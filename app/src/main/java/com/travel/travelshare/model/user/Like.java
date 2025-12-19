package com.travel.travelshare.model.user;

public class Like {
    private String fromUserId;
    private String postId;

    public Like() {}

    public Like(String fromUserId, String postId) {
        this.setFromUserId(fromUserId);
        this.setPostId(postId);
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
