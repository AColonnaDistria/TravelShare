package com.travel.travelshare.model.user;

public class Follows {
    private String fromUserId;
    private String followableContentId;
    private String followableContentType;

    public Follows(String fromUserId, String followableContentId, String followableContentType) {
        this.setFromUserId(fromUserId);
        this.setFollowableContentId(followableContentId);
        this.setFollowableContentType(followableContentType);
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFollowableContentId() {
        return followableContentId;
    }

    public void setFollowableContentId(String followableContentId) {
        this.followableContentId = followableContentId;
    }

    public String getFollowableContentType() {
        return followableContentType;
    }

    public void setFollowableContentType(String followableContentType) {
        this.followableContentType = followableContentType;
    }
}
