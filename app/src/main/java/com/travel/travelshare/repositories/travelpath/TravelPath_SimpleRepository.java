package com.travel.travelshare.repositories.travelpath;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.travel.travelshare.model.DatabaseItem;
import com.travel.travelshare.model.travelpath.TravelPath_Firebase;
import com.travel.travelshare.repositories.IRepository;
import java.util.List;
import java.util.stream.Collectors;

public class TravelPath_SimpleRepository<Item extends DatabaseItem> implements IRepository<Item> {
    protected TravelPath_Firebase travelpath_firebase;
    protected FirebaseFirestore database;
    protected final Class<Item> class_;
    protected final String collectionPath;

    public TravelPath_SimpleRepository(Context context, Class<Item> class_, String collectionPath) {
        this.class_ = class_;
        this.collectionPath = collectionPath;
        this.travelpath_firebase = TravelPath_Firebase.getInstance(context);
        if (this.travelpath_firebase != null) {
            this.database = this.travelpath_firebase.getDatabase();
        }
    }

    public void getAll(OnSuccessListener<List<Item>> listener) {
        this.database.collection(collectionPath).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Item> items = querySnapshot.getDocuments().stream().map(documentSnapshot -> {
                        return documentSnapshot.toObject(class_);
                    }).collect(Collectors.toList());
                    listener.onSuccess(items);
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
    public void getPage(OnSuccessListener<List<Item>> listener, int pageSize, String lastId) {
        Query query = this.database.collection(this.collectionPath)
                .orderBy("id")
                .limit(pageSize);

        if (lastId != null && !lastId.isEmpty()) {
            query = query.startAfter(lastId);
        }

        query.get().addOnSuccessListener(querySnapshot -> {
            List<Item> items = querySnapshot
                    .getDocuments()
                    .stream()
                    .map(documentSnapshot -> {
                        return documentSnapshot.toObject(this.class_);
                    }).collect(Collectors.toList());

            listener.onSuccess(items);
        });
    }


    @Override
    public String putItem(Item item, OnCompleteListener<Void> listener) {
        String id = item.getId();

        Task<Void> task = this.database
                .collection(this.collectionPath)
                .document(id)
                .set(item);

        if (listener != null) {
            task.addOnCompleteListener(listener);
        }

        return id;
    }

    @Override
    public String putItem(String id, Item item, OnCompleteListener<Void> listener) {
        item.setId(id);

        Task<Void> task = this.database
                .collection(this.collectionPath)
                .document(id)
                .set(item);

        if (listener != null) {
            task.addOnCompleteListener(listener);
        }

        return id;
    }

    @Override
    public String putItem() {
        DocumentReference doc = this.database.collection(this.collectionPath).document();
        String id = doc.getId();

        return id;
    }

    @Override
    public String putItem(Item item) {
        return this.putItem(item, null);
    }

    @Override
    public String putItem(String id, Item item) {
        return this.putItem(id, item, (OnCompleteListener<Void>) null);
    }
}