package com.travel.travelshare.model.location;

import com.google.android.gms.common.data.DataBuffer;
import com.travel.travelshare.model.DatabaseItem;

public class Cluster implements DatabaseItem {
    private String id; // geohash
    private ExactLocation location;
    private int numberOfPosts;

    public Cluster(String id, ExactLocation location, int numberOfPosts) {
        this.setId(id);
        this.setLocation(location);
        this.setNumberOfPosts(numberOfPosts);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public ExactLocation getLocation() {
        return location;
    }

    public void setLocation(ExactLocation location) {
        this.location = location;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }
}
