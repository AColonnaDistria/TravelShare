package com.travel.travelshare.model.user;

import com.travel.travelshare.model.post.FollowableContent;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.post.Comment;

import java.util.List;

/* TODO : Handle security better */
public class ConnectedUser extends User {
    private String username;
    private String hashedPassword;
    private String email;

    /*
    private ArrayList<PicturePost> picturePosts;
    private ArrayList<Comment> comments;
    private ArrayList<FollowableContent> follows;
    private ArrayList<UserGroup> userGroupOwner;
    */

    public ConnectedUser(String username, String hashedPassword, String email) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    public void comment(Comment comment) {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public List<Comment> getComments() {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public void post(PicturePost picturePost) {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public List<PicturePost> getPicturePosts() {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public void follow(FollowableContent followableContent) {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public List<FollowableContent> getFollows() {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public void createGroup(UserGroup userGroup) {
        throw new UnsupportedOperationException("Not implemented method");
    }

    public List<UserGroup> getUserGroupsBelongingTo() {
        throw new UnsupportedOperationException("Not implemented method");
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
