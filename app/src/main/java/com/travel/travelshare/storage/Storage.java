package com.travel.travelshare.storage;

import android.media.Image;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.hardware.display.DisplayManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.travel.travelshare.model.ImagePublication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class Storage {
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseDB;
    private StorageReference firebaseRef;
    private FirebaseAuth firebaseAuth;

    public Storage() {
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnSuccessListener(authResult -> {
                Log.d("Auth", "Signed in as: " + authResult.getUser().getUid());
            })
            .addOnFailureListener(e -> {
                Log.e("Auth", "Sign in failed", e);
            });

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance("gs://travelshare-2609c.firebasestorage.app");
        this.firebaseDB = FirebaseFirestore.getInstance();
        this.firebaseRef = this.firebaseStorage.getReference();
    }

    public void saveImagePublication(Uri imageUri, boolean visibility, LocalDateTime date, String description, String instructions, String location) {
        // 1. Get the Image URI from your ViewModel
        if (imageUri == null) {
            // Handle error: No image selected
            return;
        }

        // Upload Image
        String firebaseUri = "images/" + UUID.randomUUID().toString() + ".jpg";

        StorageReference newPostImageRef = this.firebaseRef.child(firebaseUri);
        UploadTask uploadTask = newPostImageRef.putFile(imageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.v("FIREBASE", "Unsuccessful upload");
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Upload post

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.v("FIREBASE", String.valueOf(taskSnapshot.getMetadata()));

                // convert localdatetime to timestamp;
                Timestamp timestamp = new Timestamp(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));

                ImagePublication imagePublication = new ImagePublication(
                        firebaseUri,
                        visibility,
                        timestamp,
                        description,
                        instructions,
                        location
                );

                Storage.this.firebaseDB.collection("posts")
                        .add(imagePublication)
                        .addOnSuccessListener(documentReference -> {
                            Log.d("FIREBASE", "Post added with ID: " + documentReference.getId());
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FIREBASE", "Error adding post", e);
                        });
            }
        });
    }
}
