package com.travel.travelshare.repositories;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.travel.travelshare.model.DatabaseItem;

import java.util.List;

public interface IRepository<Item extends DatabaseItem> {

    void getItem(String id, OnSuccessListener<Item> listener);
    void getPage(OnSuccessListener<List<Item>> listener, int pageSize, String lastId);

    String putItem(Item item, OnCompleteListener<Void> listener);
    String putItem(String id, Item item, OnCompleteListener<Void> listener);
    String putItem();

    String putItem(Item item);
    String putItem(String id, Item item);
}
