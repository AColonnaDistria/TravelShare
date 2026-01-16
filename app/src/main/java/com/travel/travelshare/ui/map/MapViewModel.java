package com.travel.travelshare.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.UserRepository;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapViewModel extends ViewModel {
    private PostRepository postRepository;
    public MutableLiveData<List<PicturePost>> mPosts = new MutableLiveData<>();
    private GeoPoint lastMapCenter = null;
    private Double lastZoomLevel = null;
    public void saveMapState(GeoPoint center, double zoom) {
        this.lastMapCenter = center;
        this.lastZoomLevel = zoom;
    }

    public GeoPoint getLastMapCenter() { return lastMapCenter; }
    public Double getLastZoomLevel() { return lastZoomLevel; }

    public MapViewModel() {
        this.postRepository = new PostRepository();
    }

    public void loadPosts() {
        postRepository.getAllPublic(posts -> {
            mPosts.setValue(posts);
        });
    }
}