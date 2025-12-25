package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class User {
    private String userId;
    private UserType userType;
    private Timestamp createdAt;

    public User() {}

    public User(UserType userType) {
        this.userType = userType;
    }

    public User(String userId, Timestamp createdAt, UserType userType) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.userType = userType;
    }

    public String getUserId() {
        return this.userId;
    }

    public UserType getUserType() {
        return this.userType;
    }
}
