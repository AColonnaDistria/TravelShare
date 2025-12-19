package com.travel.travelshare.model.user;

import java.util.ArrayList;

public class User {
    private String userId;
    private UserType userType;

    public User() {}

    public User(UserType userType) {
        this.userType = userType;
    }

    public User(String userId, UserType userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public String getUserId() {
        return this.userId;
    }

    public UserType getUserType() {
        return this.userType;
    }
}
