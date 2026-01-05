package com.travel.travelshare.ui.map;

import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;

public class MapMarkerData {
    private PicturePost post;
    private User author;

    public MapMarkerData(PicturePost post, User author) {
        this.setPost(post);
        this.setAuthor(author);
    }

    public PicturePost getPost() {
        return this.post;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setPost(PicturePost post) {
        this.post = post;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}