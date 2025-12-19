package com.travel.travelshare.ui.publish;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;

public class PublishViewModel extends ViewModel {

    private final SavedStateHandle savedStateHandle;
    private static final String PHOTO_KEY_URI = "photo_uri";
    private static final String VISIBILITY_KEY_URI = "is_public";
    private static final String DATE_KEY_URI = "date";
    private static final String DESCRIPTION_KEY_URI = "description";
    private static final String LOCATION_KEY_URI = "location";

    public PublishViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
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

    public void setLocation(String location) {
        savedStateHandle.set(LOCATION_KEY_URI, location);
    }
    public LiveData<String> getLocation() {
        return savedStateHandle.getLiveData(LOCATION_KEY_URI);
    }
}