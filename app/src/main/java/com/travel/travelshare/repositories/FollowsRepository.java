package com.travel.travelshare.repositories;

import com.travel.travelshare.model.user.Follows;

public class FollowsRepository extends SimpleRepository<Follows> {
    public FollowsRepository() {
        super(Follows.class, "travelshare_follows");
    }
}
