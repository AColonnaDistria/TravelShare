package com.travel.travelshare.model.user;

public class GuestUser extends User {
    public GuestUser() {}

    public GuestUser(String userId) {
        super(userId, UserType.GUEST);
    }
}
