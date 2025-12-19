package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserGroup {
    private static final int USER_GROUP_LIMIT = 8;

    // User group is limited to 8 users
    private ArrayList<String> usersId;
    private Timestamp createdAt;

    public UserGroup() {}

    public UserGroup(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean addUserMemberId(String connectedUserId) {
        if (this.usersId.size() >= USER_GROUP_LIMIT) return false;

        return this.usersId.add(connectedUserId);
    }

    public boolean addUserMemberId(List<String> connectedUsersId) {
        if (connectedUsersId.size() >= USER_GROUP_LIMIT) return false;

        return this.usersId.addAll(connectedUsersId);
    }

    public List<String> getUserMembersId()  {
        return Collections.unmodifiableList(this.usersId);
    }
}
