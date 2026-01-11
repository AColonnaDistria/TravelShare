package com.travel.travelshare.ui.publish;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

public class PublishViewModel extends ViewModel {
    private static final String PHOTO_KEY_URI = "photo_uri";
    private static final String VISIBILITY_KEY_URI = "is_public";
    private static final String DATE_KEY_URI = "date";
    private static final String DESCRIPTION_KEY_URI = "description";
    private static final String INSTRUCTION_KEY_URI = "instructions";
    private static final String LOCATION_KEY_URI = "location";

    private final MutableLiveData<User> activeUser = new MutableLiveData<>();

    private final SavedStateHandle savedStateHandle;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FirebaseAuth mAuth;

    public PublishViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;

        this.postRepository = new PostRepository();
        this.userRepository = new UserRepository();

        this.userRepository.getItemByFirebaseUid(FirebaseAuth.getInstance().getCurrentUser().getUid(), this::setActiveUser);

        this.mAuth = FirebaseAuth.getInstance();
        this.loadUser();

        this.setVisibility(true); // prevents crash
    }
    public void setPhotoURI(Uri value) {
        savedStateHandle.set(PHOTO_KEY_URI, value);
    }
    public LiveData<Uri> getPhotoURI() {
        return savedStateHandle.getLiveData(PHOTO_KEY_URI);
    }

    public void setVisibility(boolean value) {
        savedStateHandle.set(VISIBILITY_KEY_URI, value);
    }
    public LiveData<Boolean> getVisibility() {
        return savedStateHandle.getLiveData(VISIBILITY_KEY_URI);
    }

    public void setDate(LocalDateTime date) {
        savedStateHandle.set(DATE_KEY_URI, date);
    }
    public LiveData<LocalDateTime> getDate() {
        return savedStateHandle.getLiveData(DATE_KEY_URI);
    }

    public void setDescription(String description) {
        savedStateHandle.set(DESCRIPTION_KEY_URI, description);
    }
    public LiveData<String> getDescription() {
        return savedStateHandle.getLiveData(DESCRIPTION_KEY_URI);
    }

    public void setInstructions(String instructions) {
        savedStateHandle.set(INSTRUCTION_KEY_URI, instructions);
    }
    public LiveData<String> getInstructions() {
        return savedStateHandle.getLiveData(INSTRUCTION_KEY_URI);
    }

    public void setLocation(String location) {
        savedStateHandle.set(LOCATION_KEY_URI, location);
    }
    public LiveData<String> getLocation() {
        return savedStateHandle.getLiveData(LOCATION_KEY_URI);
    }

    public User getActiveUser() {
        return activeUser.getValue();
    }

    public void setActiveUser(User user) {
        activeUser.setValue(user);
    }

    public void loadUser() {
        this.mAuth.getCurrentUser().reload().addOnCompleteListener(task -> {
            this.userRepository.getItemByFirebaseUid(mAuth.getUid(), this::setActiveUser);
        });
    }

    public void saveImagePublication() {
        Uri imageUri = this.getPhotoURI().getValue();
        if (imageUri == null) {
            Log.e("PublishViewModel", "No image selected!");
            return; // or show a Toast to user
        }

        boolean visibility = Boolean.TRUE.equals(this.getVisibility().getValue());

        Timestamp timestamp;
        try {
            timestamp = new Timestamp(Instant.from(this.getDate().getValue()));
        } catch (Exception e) {
            timestamp = new Timestamp(0, 0);
        }

        Timestamp date = timestamp;
        String description = this.getDescription().getValue();
        String instructions = this.getInstructions().getValue();
        Location location = new ApproximateLocation(
                this.getLocation().getValue(),
                "city",
                "region",
                "country",
                0.0,
                0.0,
                "s0000"
        );

        Timestamp createdAt = Timestamp.now();

        PicturePost picturePost = new PicturePost(
                getActiveUser().getId(),
                imageUri.toString(),
                description,
                instructions,
                date,
                createdAt,
                visibility,
                location
        );

        this.postRepository.putItem(picturePost, imageUri);
    }

    public Uri createPhotoUri(Context context) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalCacheDir();

            File imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Convert File to Uri using FileProvider
            return FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    imageFile
            );

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}