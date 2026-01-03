package com.travel.travelshare.repositories;
import com.travel.travelshare.model.user.Like;

public class LikeRepository extends SimpleRepository<Like> {

    public LikeRepository() {
        super(Like.class, "travelshare_likes");
    }
}