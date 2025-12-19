package com.travel.travelshare.model.user;

public class GuestUser extends User {
    private String sessionID;

    public GuestUser(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }
}
