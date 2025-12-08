package com.travel.travelshare.ui.publish;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class PublishViewModel extends ViewModel {

    private final SavedStateHandle savedStateHandle;
    private static final String PHOTO_KEY_URI = "photo_uri";

    public PublishViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }
    public void setPhotoURI(Uri value) {
        savedStateHandle.set(PHOTO_KEY_URI, value);
    }
    public LiveData<Uri> getPhotoURI() {
        return savedStateHandle.getLiveData(PHOTO_KEY_URI);
    }
}