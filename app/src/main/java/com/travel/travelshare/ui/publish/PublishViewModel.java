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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.Storage;
import com.travel.travelshare.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

public class PublishViewModel extends ViewModel {
    private static final String PHOTO_KEY_URI = "photo_uri";
    private static final String VISIBILITY_KEY_URI = "is_public";
    private static final String DESCRIPTION_KEY_URI = "description";
    private static final String INSTRUCTION_KEY_URI = "instructions";

    private final MutableLiveData<Location> location = new MutableLiveData<>();
    private final MutableLiveData<Date> date = new MutableLiveData<>();
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

        this.setDate(Date.from(Instant.now()));
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

    public void setDate(Date date) {
        this.date.setValue(date);
    }
    public LiveData<Date> getDate() {
        return this.date;
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

    public void setLocation(Location location) {
        this.location.setValue(location);
    }
    public LiveData<Location> getLocation() {
        return this.location;
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

    public void saveImagePublication(Context context, OnCompleteListener<Void> listener) {
        Uri imageUri = this.getPhotoURI().getValue();
        if (imageUri == null) {
            Log.e("PublishViewModel", "No image selected!");
            return; // or show a Toast to user
        }

        boolean visibility = Boolean.TRUE.equals(this.getVisibility().getValue());

        Timestamp timestamp;
        try {
            timestamp = new Timestamp(this.getDate().getValue().toInstant());
        } catch (Exception e) {
            timestamp = new Timestamp(0, 0);
        }

        Timestamp date = timestamp;
        String description = this.getDescription().getValue();
        String instructions = this.getInstructions().getValue();
        Location location = this.getLocation().getValue();

        Timestamp createdAt = Timestamp.now();

        String id = this.postRepository.putItem();
        Storage.uploadImage(context, imageUri, new Storage.OnUploadListener() {
            @Override
            public void onFailure(String error) {
                Log.v("FIREBASE", "Unsuccessful upload");
            }
            @Override
            public void onSuccess(String imageUrl) {
                PicturePost picturePost = new PicturePost(
                        getActiveUser().getId(),
                        imageUrl,
                        description,
                        instructions,
                        date,
                        createdAt,
                        visibility,
                        location
                );

                postRepository.putItem(id, picturePost, listener);
            }
        });
    }

    public enum ValidationFormStatus {
        PICTURE_MISSING,
        DATE_MISSING,
        DESCRIPTION_MISSING,
        INSTRUCTIONS_MISSING,
        LOCATION_MISSING,
        OK
    }

    public ValidationFormStatus validateForm() {
        if (this.getPhotoURI().getValue() == null)
            return ValidationFormStatus.PICTURE_MISSING;

        if (this.getDescription().getValue() == null)
            return ValidationFormStatus.DESCRIPTION_MISSING;

        if (this.getInstructions().getValue() == null)
            return ValidationFormStatus.INSTRUCTIONS_MISSING;

        if (this.getLocation().getValue() == null)
            return ValidationFormStatus.LOCATION_MISSING;

        return ValidationFormStatus.OK;
    }

    public void reset() {
        this.setVisibility(true);
        this.setInstructions(null);
        this.setDescription(null);
        this.setDate(Date.from(Instant.now()));
        this.setPhotoURI(null);
        this.setLocation(null);
    }

    // not the role of the View Model
    public Uri createTempUri(Context context) {
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