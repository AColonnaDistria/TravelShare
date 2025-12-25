package com.travel.travelshare.model.post;

import com.google.firebase.Timestamp;

public class SharedTo {
    private String fromUserId; // from
    private String postId;
    private String userGroupId;
    private Timestamp createdAt;

    public SharedTo() {}

    public SharedTo(String fromUserId, String postId, String userGroupId, Timestamp createdAt) {
        this.setFromUserId(fromUserId);
        this.setPostId(postId);
        this.setUserGroupId(userGroupId);

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

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }
}
