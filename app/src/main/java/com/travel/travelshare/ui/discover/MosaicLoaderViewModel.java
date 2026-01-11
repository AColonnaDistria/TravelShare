package com.travel.travelshare.ui.discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.repositories.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MosaicLoaderViewModel extends ViewModel {
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    protected MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    protected MutableLiveData<String> lastId = new MutableLiveData<>();

    protected PostRepository postRepository;

    public MosaicLoaderViewModel() {
        this.postRepository = new PostRepository();

        this.setIsLoading(false);
        this.setIsLastPage(false);
        this.setLastId(null);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return this.isLoading;
    }

    public void setIsLoading(boolean value) {
        this.isLoading.setValue(value);
    }

    public MutableLiveData<Boolean> getIsLastPage() {
        return this.isLastPage;
    }

    public void setIsLastPage(boolean value) {
        this.isLastPage.setValue(value);
    }

    public MutableLiveData<String> getLastId() {
        return this.lastId;
    }

    public void setLastId(String value) {
        this.lastId.setValue(value);
    }

    public void fetchPost(String postId, OnSuccessListener<PicturePost> listener) {
        this.postRepository.getItem(postId, listener);
    }

    public void fetchNextPage(OnSuccessListener<List<PicturePost>> listener) {
        if (Boolean.TRUE.equals(this.getIsLoading().getValue()) || Boolean.TRUE.equals(this.getIsLastPage().getValue())) return;

        this.setIsLoading(true);

        this.postRepository.getPage(picturePosts -> {
            if (picturePosts == null || picturePosts.isEmpty()) {
                setIsLastPage(true);
            }
            else {
                listener.onSuccess(picturePosts);
                this.setLastId(picturePosts.get(picturePosts.size() - 1).getId());
            }
            this.setIsLoading(false);
        }, 16, this.getLastId().getValue());
    }

}