package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

import java.util.ArrayList;

public class User implements DatabaseItem {
    private String id;
    private UserType userType;
    private Timestamp createdAt;

    public User() {}

    public User(UserType userType) {
        this.userType = userType;
    }

    public User(String userId, Timestamp createdAt, UserType userType) {
        this.id = userId;
        this.createdAt = createdAt;
        this.userType = userType;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserType getUserType() {
        return this.userType;
    }
}
