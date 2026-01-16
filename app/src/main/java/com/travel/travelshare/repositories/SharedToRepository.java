package com.travel.travelshare.repositories;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.travel.travelshare.model.post.SharedTo;
import com.travel.travelshare.model.user.Like;

import java.util.List;

public class SharedToRepository extends SimpleRepository<SharedTo> {
    public SharedToRepository() {
        super(SharedTo.class, "travelshare_sharedto");
    }

    public void getLastPostFromGroup(String groupId, OnSuccessListener<SharedTo> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("userGroupId", groupId) // Filtre par groupe
                .orderBy("createdAt", Query.Direction.DESCENDING) // Plus récent en premier
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        SharedTo lastShare = querySnapshot.getDocuments().get(0).toObject(SharedTo.class);
                        if (listener != null) {
                            listener.onSuccess(lastShare);
                        }
                    } else if (listener != null) {
                        listener.onSuccess(null);
                    }
                });
    }

    public void getAllSharedToGroup(String groupId, OnSuccessListener<List<SharedTo>> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("userGroupId", groupId) // Filtre par groupe
                .orderBy("createdAt", Query.Direction.DESCENDING) // Plus récent en premier
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<SharedTo> sharedTo = querySnapshot.toObjects(SharedTo.class);

                    if (listener != null) {
                        listener.onSuccess(sharedTo);
                    }
                });
    }
}