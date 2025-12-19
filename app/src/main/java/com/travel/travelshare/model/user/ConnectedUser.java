package com.travel.travelshare.model.user;

import com.travel.travelshare.model.FollowableContent;
import com.travel.travelshare.model.PicturePost;
import com.travel.travelshare.model.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* TODO : Handle security better */
public class ConnectedUser extends User {
    private String username;
    private String hashedPassword;
    private String email;

    private ArrayList<PicturePost> picturePosts;
    private ArrayList<Comment> comments;
    private ArrayList<FollowableContent> follows;
    private ArrayList<UserGroup> userGroupOwner;

    public ConnectedUser(String username, String hashedPassword, String email) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    public void comment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(this.comments);
    }

    public void post(PicturePost picturePost) {
        this.picturePosts.add(picturePost);
    }

    public List<PicturePost> getPicturePosts() {
        return Collections.unmodifiableList(this.picturePosts);
    }

    public void follow(FollowableContent followableContent) {
        this.follows.add(followableContent);
    }

    public List<FollowableContent> getFollows() {
        return Collections.unmodifiableList(this.follows);
    }

    public void createGroup(UserGroup userGroup) {
        this.userGroupOwner.add(userGroup);
    }

    public List<UserGroup> getUserGroupsBelongingTo() {
        return Collections.unmodifiableList(this.userGroupOwner);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
