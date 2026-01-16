package com.travel.travelshare.ui.groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.travel.travelshare.Auth;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.post.SharedTo;
import com.travel.travelshare.model.user.Like;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.LikeRepository;
import com.travel.travelshare.repositories.SharedToRepository;
import com.travel.travelshare.ui.discover.MosaicLoaderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupFeedViewModel extends MosaicLoaderViewModel {
    private final MutableLiveData<List<PicturePost>> sharedPosts = new MutableLiveData<>(new ArrayList<>());
    private final SharedToRepository sharedToRepository = new SharedToRepository();
    private final MutableLiveData<String> groupId = new MutableLiveData<>();

    public GroupFeedViewModel() {
        super();
    }

    public MutableLiveData<String> getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId.setValue(groupId);
    }

    public void loadSharedToPosts(String groupId) {
        if (groupId == null) return;
        this.sharedToRepository.getAllSharedToGroup(groupId, this::fetchPostsFromSharedTo);
    }

    public LiveData<List<PicturePost>> getSharedToPosts() {
        return sharedPosts;
    }

    private void fetchPostsFromSharedTo(List<SharedTo> sharedTo) {
        List<PicturePost> posts = new ArrayList<>();
        if (sharedTo.isEmpty()) {
            sharedPosts.postValue(posts);
            return;
        }

        final int totalSharedTo = sharedTo.size();
        final AtomicInteger counter = new AtomicInteger(0);

        for (SharedTo sharedToItem : sharedTo) {
            postRepository.getItem(sharedToItem.getPostId(), post -> {
                if (post != null) {
                    posts.add(post);
                }

                if (counter.incrementAndGet() == totalSharedTo) {
                    // all posts fetched
                    sharedPosts.postValue(posts); // postValue is safe for async
                }
            });
        }
    }
}
