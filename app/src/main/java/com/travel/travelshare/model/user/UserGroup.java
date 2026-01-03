package com.travel.travelshare.model.user;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.DatabaseItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserGroup implements DatabaseItem {
    private static final int USER_GROUP_LIMIT = 8;

    // User group is limited to 8 users
    private String id;
    private ArrayList<String> usersId;
    private Timestamp createdAt;

    public UserGroup() {}

    public UserGroup(String id, Timestamp createdAt) {
        this.setId(id);
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
        return this.usersId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
