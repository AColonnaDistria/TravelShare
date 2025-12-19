package com.travel.travelshare.model.annotation;

import com.travel.travelshare.model.FollowableContent;

public class Tag implements FollowableContent {
    private String tagName;

    public Tag(String tagName) {
        this.setTagName(tagName);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
