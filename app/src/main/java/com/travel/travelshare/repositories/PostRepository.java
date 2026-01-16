package com.travel.travelshare.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.post.SharedTo;
import com.travel.travelshare.model.user.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostRepository extends SimpleRepository<PicturePost> {
    public PostRepository() {
        super(PicturePost.class, "travelshare_picture_posts");
    }

    public void getAllPublic(OnSuccessListener<List<PicturePost>> listener) {
        this.database.collection(collectionPath)
                .whereEqualTo("visibility", true)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<PicturePost> items = querySnapshot.getDocuments().stream().map(documentSnapshot -> {
                        return documentSnapshot.toObject(PicturePost.class);
                    }).collect(Collectors.toList());

                    if (listener != null) {
                        listener.onSuccess(items);
                    }
                });
    }

    public void incrementLikes(String postId) {
        this.database.collection(collectionPath)
                .document(postId)
                .update("countLikes", com.google.firebase.firestore.FieldValue.increment(1));
    }

    public void decrementLikes(String postId) {
        this.database.collection(collectionPath)
                .document(postId)
                .update("countLikes", com.google.firebase.firestore.FieldValue.increment(-1));
    }


    public void getPagePublic(OnSuccessListener<List<PicturePost>> listener, int pageSize, String lastId) {
        Query query = this.database.collection(this.collectionPath)
                .whereEqualTo("visibility", true)
                .orderBy("id")
                .limit(pageSize);

        if (lastId != null && !lastId.isEmpty()) {
            query = query.startAfter(lastId);
        }

        query.get().addOnSuccessListener(querySnapshot -> {
            List<PicturePost> items = querySnapshot
                    .getDocuments()
                    .stream()
                    .map(documentSnapshot -> {
                        return documentSnapshot.toObject(PicturePost.class);
                    }).collect(Collectors.toList());

            listener.onSuccess(items);
        });
    }

    public void getNearby(double latCenter, double longCenter, double radiusInMeters, OnSuccessListener<List<PicturePost>> listener) {
        GeoLocation center = new GeoLocation(latCenter, longCenter);

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(
                center,
                radiusInMeters
        );

        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query query = this.database.collection(this.collectionPath)
                                        .orderBy("location.geohash")
                                        .startAt(b.startHash)
                                        .endAt(b.endHash);

            tasks.add(query.get());
        }

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<PicturePost> posts = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            if (task.isSuccessful()) {
                                QuerySnapshot snap = task.getResult();
                                for (DocumentSnapshot doc : snap.getDocuments()) {
                                    GeoLocation docLocation = new GeoLocation(latCenter, longCenter);
                                    double distanceInMeters = GeoFireUtils.getDistanceBetween(docLocation, center);
                                    if (distanceInMeters <= radiusInMeters) {
                                        posts.add(doc.toObject(PicturePost.class));
                                    }
                                }
                            }
                        }

                        listener.onSuccess(posts);
                    }
                });
    }
}
