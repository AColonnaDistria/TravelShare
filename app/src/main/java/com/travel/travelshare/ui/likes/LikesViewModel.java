package com.travel.travelshare.ui.likes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.Like;
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

    public LiveData<List<PicturePost>> getLikedPosts() {
        return likedPosts;
    }

    public String getCurrentUserId() {
        FirebaseAuth firebaseUser = FirebaseAuth.getInstance();
        String firebaseUid = firebaseUser.getCurrentUser().getUid();

        /*
        this.userRepository.getItemByFirebaseUid(firebaseUid, user -> {
            user.getId();
        });
         */

        return null;
    }

    public void loadLikedPosts(String userId) {
        this.likeRepository.getAllFromUser(userId, this::fetchPostsFromLikes);
    }

    private void fetchPostsFromLikes(List<Like> likes) {
        List<PicturePost> posts = new ArrayList<>();
        if (likes.isEmpty()) {
            likedPosts.setValue(posts);
            return;
        }

        for (Like like : likes) {
            postRepository.getItem(like.getPostId(), posts::add);
        }

        likedPosts.setValue(posts);
    }
}