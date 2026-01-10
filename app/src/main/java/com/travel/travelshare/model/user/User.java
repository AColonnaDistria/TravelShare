package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

import java.util.ArrayList;

public class User implements DatabaseItem {
    private String id;
    private String firebaseUid;
    private UserType userType;
    private Timestamp createdAt;

    public User() {}

    public User(UserType userType) {
        this.userType = userType;
    }

    public User(String userId, String firebaseUid, Timestamp createdAt, UserType userType) {
        this.id = userId;
        this.firebaseUid = firebaseUid;
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

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
}
