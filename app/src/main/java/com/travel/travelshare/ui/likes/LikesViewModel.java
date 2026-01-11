package com.travel.travelshare.ui.likes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.travel.travelshare.Auth;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.Like;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.LikeRepository;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LikesViewModel extends ViewModel {
    private final MutableLiveData<List<PicturePost>> likedPosts = new MutableLiveData<>(new ArrayList<>());
    private final LikeRepository likeRepository = new LikeRepository();
    private final PostRepository postRepository = new PostRepository();
    private final UserRepository userRepository = new UserRepository();
    private final Auth auth;

    public LikesViewModel(Auth auth) {
        this.auth = auth;

        this.auth.observeAuthState(user -> {
            if (user != null) {
                loadLikedPosts(user.getId());
            }
        });
    }

    public Auth getAuth() {
        return this.auth;
    }

    public LiveData<List<PicturePost>> getLikedPosts() {
        return likedPosts;
    }

    public User getCurrentUser() {
        return auth.getActiveUser();
    }

    public void loadLikedPosts(String userId) {
        if (userId == null) return;
        this.likeRepository.getAllFromUser(userId, this::fetchPostsFromLikes);
    }

    private void fetchPostsFromLikes(List<Like> likes) {
        List<PicturePost> posts = new ArrayList<>();
        if (likes.isEmpty()) {
            likedPosts.postValue(posts);
            return;
        }

        final int totalLikes = likes.size();
        final AtomicInteger counter = new AtomicInteger(0);

        for (Like like : likes) {
            postRepository.getItem(like.getPostId(), post -> {
                if (post != null) {
                    posts.add(post);
                }

                if (counter.incrementAndGet() == totalLikes) {
                    // all posts fetched
                    likedPosts.postValue(posts); // postValue is safe for async
                }
            });
        }
    }

}