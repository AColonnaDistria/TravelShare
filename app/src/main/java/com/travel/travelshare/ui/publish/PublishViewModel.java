package com.travel.travelshare.ui.publish;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PublishViewModel extends ViewModel {

    private final MutableLiveData<Uri> photoUri;

    public PublishViewModel() {
        photoUri = new MutableLiveData<>();
    }
    public void setPhotoURI(Uri value) {
        this.photoUri.setValue(value);
    }
    public LiveData<Uri> getPhotoURI() {

        return photoUri;
    }
}