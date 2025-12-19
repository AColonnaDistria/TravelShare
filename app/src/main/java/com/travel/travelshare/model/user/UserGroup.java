package com.travel.travelshare.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserGroup {
    // User group is limited to 8 users
    ArrayList<String> users;

    public UserGroup() {}

    public boolean addUserMember(String connectedUserId) {
        if (this.users.size() >= 8) return false;

        return this.users.add(connectedUserId);
    }

    public List<String> getUserMembers()  {
        return Collections.unmodifiableList(this.users);
    }
}
