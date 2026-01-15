package com.travel.travelshare.ui.discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscoverViewModel extends MosaicLoaderViewModel {
    public DiscoverViewModel() {
        super();
    }
}