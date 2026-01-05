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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.travel.travelshare.model.post.PicturePost;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostRepository {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    //private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private static final String collectionPath = "travelshare_picture_posts";
    //private static final String storagePath = "travelshare_pictures";

    public void getItem(String id, OnSuccessListener<PicturePost> listener) {
        this.database.collection(PostRepository.collectionPath).document(id)
                .get()
                .addOnSuccessListener(document -> {
                    PicturePost post = document.toObject(PicturePost.class);
                    listener.onSuccess(post);
                });
    }

    public void getAll(OnSuccessListener<List<PicturePost>> listener) {
        this.database.collection(PostRepository.collectionPath).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<PicturePost> posts = querySnapshot.getDocuments().stream().map(documentSnapshot -> {
                        return documentSnapshot.toObject(PicturePost.class);
                    }).collect(Collectors.toList());

                    listener.onSuccess(posts);
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
            Query query = this.database.collection(PostRepository.collectionPath)
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
                                    posts.add(doc.toObject(PicturePost.class));
                                }
                            }
                        }

                        listener.onSuccess(posts);
                    }
                });
    }

    public void putItem(PicturePost item, Uri imageLocalURI) {
        DocumentReference document = this.database.collection(PostRepository.collectionPath).document();

        String id = document.getId();
        item.setId(id);

        String filename = id;

        //String firebaseImageUri = PostRepository.storagePath + "/" + filename + ".jpg";
        //StorageReference firebaseImageRef = storage.getReference().child(firebaseImageUri);

        Storage.uploadImage(imageLocalURI, new Storage.OnUploadListener() {
            @Override
            public void onFailure(String error) {
                Log.v("FIREBASE", "Unsuccessful upload");
            }
            @Override
            public void onSuccess(String imageUrl) {
                item.setPhoto_URI(imageUrl);

                document.set(item);
            }
        });
    }
}
