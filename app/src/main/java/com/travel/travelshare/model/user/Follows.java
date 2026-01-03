package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

public class Follows implements DatabaseItem {
    private String fromUserId;
    private String followableContentId;
    private String followableContentType;
    private Timestamp createdAt;
    private String id;

    public Follows() {}

    public Follows(String id, String fromUserId, String followableContentId, String followableContentType, Timestamp createdAt) {
        this.setId(id);

        this.setFromUserId(fromUserId);
        this.setFollowableContentId(followableContentId);
        this.setFollowableContentType(followableContentType);

        this.createdAt = createdAt;
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

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
