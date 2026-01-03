package com.travel.travelshare.repositories;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.DatabaseItem;

public interface IRepository<Item extends DatabaseItem> {

    void getItem(String id, OnSuccessListener<Item> listener);

    void putItem(Item item);
}
