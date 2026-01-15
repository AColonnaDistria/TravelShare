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
    private String bannerPhoto_URI;
    private String name;
    private ArrayList<String> usersId;
    private Timestamp createdAt;

    public UserGroup() {
    }

    public UserGroup(String id, String bannerPhoto_URI, Timestamp createdAt) {
        this.setBannerPhoto_URI(bannerPhoto_URI);
        this.setId(id);
        this.setName(name);
        this.createdAt = createdAt;
    }

    public void setBannerPhoto_URI(String bannerPhotoUri) {
        this.bannerPhoto_URI = bannerPhotoUri;
    }

    public String getBannerPhoto_URI() {
        return this.bannerPhoto_URI;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


    public boolean addUserMemberId(String connectedUserId) {
        if (this.usersId.size() >= USER_GROUP_LIMIT) return false;

        return this.usersId.add(connectedUserId);
    }

    public boolean addUserMemberId(List<String> connectedUsersId) {
        if (connectedUsersId.size() >= USER_GROUP_LIMIT) return false;

        return this.usersId.addAll(connectedUsersId);
    }

    public List<String> getUsersId()  {
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
