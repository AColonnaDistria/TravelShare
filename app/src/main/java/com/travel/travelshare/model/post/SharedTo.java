package com.travel.travelshare.model.post;

public class SharedTo {
    private String fromUserId; // from
    private String postId;
    private String userGroupId;

    public SharedTo(String fromUserId, String postId, String userGroupId) {
        this.setFromUserId(fromUserId);
        this.setPostId(postId);
        this.setUserGroupId(userGroupId);
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
