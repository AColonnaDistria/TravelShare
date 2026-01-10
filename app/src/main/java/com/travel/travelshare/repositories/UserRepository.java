package com.travel.travelshare.repositories;
import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.user.User;

public class UserRepository extends SimpleRepository<User> {
    static String collectionPath = "travelshare_users";

    public UserRepository() {
        super(User.class, collectionPath);
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
}