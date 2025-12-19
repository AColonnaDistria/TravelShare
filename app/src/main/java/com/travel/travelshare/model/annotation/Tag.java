package com.travel.travelshare.model.annotation;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.post.FollowableContent;

public class Tag implements FollowableContent {
    private String tagId;
    private String tagName;

    public Tag() {}

    public Tag(String tagId, String tagName) {
        this.setTagName(tagName);
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
