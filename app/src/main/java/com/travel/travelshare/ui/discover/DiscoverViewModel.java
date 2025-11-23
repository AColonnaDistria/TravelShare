package com.travel.travelshare.ui.discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscoverViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DiscoverViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a discover fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}