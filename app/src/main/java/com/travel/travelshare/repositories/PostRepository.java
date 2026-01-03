package com.travel.travelshare.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.travel.travelshare.model.post.PicturePost;

public class PostRepository {
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final String collectionPath = "travelshare_picture_posts";
    private static final String storagePath = "travelshare_pictures";

    public void getItem(String id, OnSuccessListener<PicturePost> listener) {
        this.database.collection(PostRepository.collectionPath).document(id)
                .get()
                .addOnSuccessListener(document -> {
                    PicturePost post = document.toObject(PicturePost.class);
                    listener.onSuccess(post);
                });
    }

    /*
    private static final List<String> IMAGE_SUPPORTED_EXTENSIONS = List.of(
            ".png", ".jpg", ".jpeg", ".bmp", ".webp", ".gif"
    );


    private static String extractExtension(String filepath) {
        if (filepath == null)
            return null;

        String lowercase_filepath = filepath.toLowerCase();
        if (!lowercase_filepath.contains("."))
            return "";

        return (lowercase_filepath.substring(lowercase_filepath.lastIndexOf('.')));
    }

    private static boolean isFileSupported(String filepath) {
        if (filepath == null) return false;

        String lowercase_filepath = filepath.toLowerCase();

        return PostRepository.IMAGE_SUPPORTED_EXTENSIONS.stream().anyMatch(lowercase_filepath::endsWith);
    }
    */

    public void putItem(PicturePost item, Uri imageLocalURI) {
        DocumentReference document = this.database.collection(PostRepository.collectionPath).document();

        String id = document.getId();
        item.setId(id);

        String filename = id;

        String firebaseImageUri = PostRepository.storagePath + "/" + filename + ".jpg";
        StorageReference firebaseImageRef = storage.getReference().child(firebaseImageUri);

        firebaseImageRef.putFile(imageLocalURI)
            .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.v("FIREBASE", "Unsuccessful upload");
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                item.setPhoto_URI(firebaseImageUri);

                document.set(item);
            }
        });
    }
}
