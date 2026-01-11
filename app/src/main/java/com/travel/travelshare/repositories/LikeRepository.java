package com.travel.travelshare.repositories;
import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.user.Like;

import java.util.List;

public class LikeRepository extends SimpleRepository<Like> {
    private static String collectionPath = "travelshare_likes";

    public LikeRepository() {
        super(Like.class, collectionPath);
    }

    public void getAllFromUser(String userId, OnSuccessListener<List<Like>> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("fromUserId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Like> likes = querySnapshot.toObjects(Like.class);

                    listener.onSuccess(likes);
                });
    }
}