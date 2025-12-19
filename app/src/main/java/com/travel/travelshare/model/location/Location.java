package com.travel.travelshare.model.location;

import com.travel.travelshare.model.post.FollowableContent;

public class Location implements FollowableContent {
    private LocationType type;

    public Location() {}

    public Location(LocationType type) {
        this.type = type;
    }

    public LocationType getLocationType() {
        return this.type;
    }
}
