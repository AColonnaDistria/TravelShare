package com.travel.travelshare.repositories;
import com.travel.travelshare.model.post.SharedTo;

public class SharedToRepository extends SimpleRepository<SharedTo> {
    public SharedToRepository() {
        super(SharedTo.class, "travelshare_sharedto");
    }
}