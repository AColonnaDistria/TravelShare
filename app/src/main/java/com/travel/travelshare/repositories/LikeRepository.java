package com.travel.travelshare.repositories;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.user.Like;

import java.util.List;

public class LikeRepository extends SimpleRepository<Like> {
    public LikeRepository() {
        super(Like.class, "travelshare_likes");
    }

    public void getAllFromUser(String userId, OnSuccessListener<List<Like>> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("fromUserId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Like> likes = querySnapshot.toObjects(Like.class);

                    if (listener != null) {
                        listener.onSuccess(likes);
                    }
                });
    }

    public void removeLikes(String postId, String userId, OnCompleteListener<Void> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("postId", postId)
                .whereEqualTo("fromUserId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    com.google.firebase.firestore.WriteBatch batch = this.database.batch();

                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        batch.delete(doc.getReference());
                    }

                    batch.commit().addOnCompleteListener(listener);
                })
                .addOnFailureListener(e -> Log.e("LIKE_REPOSITORY", "Failed to find likes to remove", e));
    }

    public void countLikes(String postId, OnSuccessListener<Long> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("postId", postId)
                .count()
                .get(com.google.firebase.firestore.AggregateSource.SERVER)
                .addOnSuccessListener(querySnapshot -> {
                    long count = querySnapshot.getCount();

                    if (listener != null) {
                        listener.onSuccess(count);
                    }
                });
    }

    public void checkLike(String postId, String userId, OnSuccessListener<Boolean> listener) {
        this.database.collection(collectionPath)
            .whereEqualTo("postId", postId)
            .whereEqualTo("fromUserId", userId)
            .count()
            .get(com.google.firebase.firestore.AggregateSource.SERVER)
            .addOnSuccessListener(querySnapshot -> {
                long count = querySnapshot.getCount();

                if (listener != null) {
                    if (count == 0) {
                        listener.onSuccess(false);
                    }
                    else {
                        listener.onSuccess(true);
                    }
                }
            });
    }
}