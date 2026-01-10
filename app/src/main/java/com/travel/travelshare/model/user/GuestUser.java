package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;

public class GuestUser extends User {
    public GuestUser() {}

    public GuestUser(String userId, Timestamp createdAt) {
        super(userId, userId, createdAt, UserType.GUEST);
    }
}
