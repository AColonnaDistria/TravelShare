package com.travel.travelshare.model.annotation;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;
import com.travel.travelshare.model.post.FollowableContent;

public class Tag implements FollowableContent, DatabaseItem {
    private String tagId;
    private String tagName;

    public Tag() {}

    public Tag(String id, String tagName) {
        this.setId(id);
        this.setTagName(tagName);
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getId() {
        return this.tagId;
    }

    @Override
    public void setId(String id) {

    }
}
