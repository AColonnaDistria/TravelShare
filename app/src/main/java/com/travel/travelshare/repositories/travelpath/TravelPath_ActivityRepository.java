package com.travel.travelshare.repositories.travelpath;

import android.content.Context;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.travel.travelshare.model.travelpath.TravelPath_Activity;

import java.util.ArrayList;
import java.util.List;

public class TravelPath_ActivityRepository extends TravelPath_SimpleRepository<TravelPath_Activity> {
    public TravelPath_ActivityRepository(Context context) {
        super(context, TravelPath_Activity.class, "activites");
    }

    // Dans TravelPath_SimpleRepository.java ou TravelPath_ActivityRepository.java
    public void getNearbyActivities(double lat, double lon, double radius, OnSuccessListener<List<TravelPath_Activity>> listener) {
        GeoLocation center = new GeoLocation(lat, lon);
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radius);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for (GeoQueryBounds b : bounds) {
            Query query = this.database.collection(this.collectionPath)
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);
            tasks.add(query.get());
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(t -> {
            List<TravelPath_Activity> results = new ArrayList<>();
            for (Task<QuerySnapshot> task : tasks) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        TravelPath_Activity activity = doc.toObject(TravelPath_Activity.class);
                        if (activity != null && activity.getLocalisation() != null) {
                            GeoLocation docLoc = new GeoLocation(
                                    activity.getLocalisation().get(0),
                                    activity.getLocalisation().get(1)
                            );
                            if (GeoFireUtils.getDistanceBetween(docLoc, center) <= radius) {
                                results.add(activity);
                            }
                        }
                    }
                }
            }

            listener.onSuccess(results);
        });
    }
}