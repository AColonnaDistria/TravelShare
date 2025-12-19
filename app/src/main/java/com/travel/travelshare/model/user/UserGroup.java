package com.travel.travelshare.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserGroup {
    ArrayList<ConnectedUser> users;

    public UserGroup() {

    }

    public void addUserMember(ConnectedUser connectedUser) {
        this.users.add(connectedUser);
    }

    public List<ConnectedUser> getUserMembers()  {
        return Collections.unmodifiableList(this.users);
    }
}
