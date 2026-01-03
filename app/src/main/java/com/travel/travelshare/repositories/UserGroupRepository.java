package com.travel.travelshare.repositories;

import com.travel.travelshare.model.user.UserGroup;

public class UserGroupRepository extends SimpleRepository<UserGroup> {

    public UserGroupRepository() {
        super(UserGroup.class, "travelshare_usergroups");
    }
}
