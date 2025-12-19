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

import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.post.PicturePost;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
        this.firebaseStorage = FirebaseStorage.getInstance(StorageConfig.FIREBASE_STORAGE_URI);
        this.firebaseDB = FirebaseFirestore.getInstance();
        this.firebaseRef = this.firebaseStorage.getReference();
    }

    private boolean checkIfImage(String path) {
        Log.v("STORAGE", extractFileExtension(path));

        return StorageConfig.IMAGE_SUPPORTED_EXTENSIONS.contains(extractFileExtension(path));
    }

    private String extractFileExtension(String path) {
        int dotIndex = path.lastIndexOf(".");
        String fileExtension = (dotIndex > 0) ? path.substring(dotIndex + 1).toLowerCase() : "";

        Log.v("STORAGE", fileExtension);

        return fileExtension;
    }

    public void saveImagePublication(String authorId, Uri imageUri, boolean visibility, Timestamp date, String description, String instructions, Location location) {
        // 1. Get the Image URI from your ViewModel
        if (imageUri == null || !checkIfImage(imageUri.toString())) {
            // Handle error: No image selected
            if (imageUri != null) {
                Log.e("STORAGE", imageUri.toString() + " is not a valid image URI");
            }
            else {
                Log.e("STORAGE", "Image URI is null");
            }
            return;
        }

        String fileExtension = extractFileExtension(imageUri.toString());
        // Upload Image
        String firebaseUri = StorageURIGenerator.generateImageURI(fileExtension);

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

                Timestamp createdAt = Timestamp.now();

                PicturePost picturePost = new PicturePost(
                    authorId, firebaseUri, description, instructions, date, createdAt, visibility, location
                );

                Storage.this.firebaseDB.collection(StorageConfig.PICTURE_POSTS_ROUTE)
                        .add(picturePost)
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
