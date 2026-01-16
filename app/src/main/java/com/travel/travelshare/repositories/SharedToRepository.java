package com.travel.travelshare.repositories;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.travel.travelshare.model.post.SharedTo;
import com.travel.travelshare.model.user.Like;

import java.util.List;

public class SharedToRepository extends SimpleRepository<SharedTo> {
    public SharedToRepository() {
        super(SharedTo.class, "travelshare_sharedto");
    }

    public void getAllSharedToGroup(String groupId, OnSuccessListener<List<SharedTo>> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("userGroupId", groupId) // Filtre par groupe
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<SharedTo> sharedTo = querySnapshot.toObjects(SharedTo.class);

                    if (listener != null) {
                        listener.onSuccess(sharedTo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SHARED_TO_REPOSITORY", e.getMessage().toString());
                    }
                });
    }
}