package com.travel.travelshare.repositories;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.user.Like;
import com.travel.travelshare.model.user.UserGroup;

import java.util.List;

public class UserGroupRepository extends SimpleRepository<UserGroup> {

    public UserGroupRepository() {
        super(UserGroup.class, "travelshare_usergroups");
    }

    public void getAllFromMember(String userId, OnSuccessListener<List<UserGroup>> listener) {
        this.database.collection(collectionPath)
                .whereArrayContains("usersId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<UserGroup> groups = querySnapshot.toObjects(UserGroup.class);

                    if (listener != null) {
                        listener.onSuccess(groups);
                    }
                })
                .addOnFailureListener(e -> {
                    // This will tell you EXACTLY why it failed (Permission denied, Index missing, etc.)
                    Log.e("FIRESTORE_ERROR", "Error fetching groups: ", e);
                });
    }
}
