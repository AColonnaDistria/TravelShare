package com.travel.travelshare.repositories;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.travel.travelshare.model.user.User;

public class UserRepository extends SimpleRepository<User> {
    static String collectionPath = "travelshare_users";

    public UserRepository() {
        super(User.class, collectionPath);
    }

    public void putItemAndReplaceByFirebaseUid(User user) {
        String id = user.getId();
        String firebaseUid = user.getFirebaseUid();

        this.removeItemsByFirebaseUid(firebaseUid, x -> {
            this.database.collection(this.collectionPath).document(id).set(user);
        });
    }

    public void getItemByFirebaseUid(String firebaseUid, OnSuccessListener<User> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("firebaseUid", firebaseUid)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        User item = querySnapshot
                                .getDocuments()
                                .get(0)
                                .toObject(User.class);
                        listener.onSuccess(item);
                    } else
                    {
                        listener.onSuccess(null);
                    }
                });
    }
    public void removeItemsByFirebaseUid(String firebaseUid, OnSuccessListener<?> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("firebaseUid", firebaseUid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.v("FIREBASE", "Deleted document: " + document.getId());
                                })
                                .addOnFailureListener(e -> {
                                    Log.v("FIREBASE", "Failed to delete: " + document.getId());
                                });
                    }

                    listener.onSuccess(null);
                });
    }
}