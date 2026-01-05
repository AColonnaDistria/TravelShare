package com.travel.travelshare.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a map fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}