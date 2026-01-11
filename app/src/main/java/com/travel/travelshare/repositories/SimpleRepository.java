package com.travel.travelshare.repositories;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travel.travelshare.model.DatabaseItem;
import com.travel.travelshare.model.annotation.Tag;
import com.travel.travelshare.model.post.Comment;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleRepository<Item extends DatabaseItem> implements IRepository<Item> {
    protected final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final Class<Item> class_;
    protected final String collectionPath;

    public SimpleRepository(Class<Item> class_, String collectionPath) {
        this.class_ = class_;
        this.collectionPath = collectionPath;
    }


    public void getAll(OnSuccessListener<List<Item>> listener) {
        this.database.collection(collectionPath).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Item> items = querySnapshot.getDocuments().stream().map(documentSnapshot -> {
                        return documentSnapshot.toObject(class_);
                    }).collect(Collectors.toList());

                    if (listener != null) {
                        listener.onSuccess(items);
                    }
                });
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
