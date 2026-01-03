package com.travel.travelshare.repositories;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travel.travelshare.model.DatabaseItem;
import com.travel.travelshare.model.annotation.Tag;
import com.travel.travelshare.model.post.Comment;
import com.travel.travelshare.model.user.User;

import java.util.Map;

public class SimpleRepository<Item extends DatabaseItem> implements IRepository<Item> {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final Class<Item> class_;
    private final String collectionPath;

    private static final Map<Class<?>, String> COLLECTION_PATHS = Map.of(
            User.class, "travelshare_users",
            Comment.class, "travelshare_comments",
            Tag.class, "travelshare_tags"
    );

    public SimpleRepository(Class<Item> class_, String collectionPath) {
        this.class_ = class_;
        this.collectionPath = collectionPath;
    }

    @Override
    public void getItem(String id, OnSuccessListener<Item> listener) {
        this.database.collection(this.collectionPath).document(id)
                .get()
                .addOnSuccessListener(document -> {
                    Item item = document.toObject(class_);
                    listener.onSuccess(item);
                });
    }

    @Override
    public void putItem(Item item) {
        String id = item.getId();

        this.database.collection(this.collectionPath).document(id).set(item);
    }
}
