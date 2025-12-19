package com.travel.travelshare.model.user;

import com.travel.travelshare.model.post.FollowableContent;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.post.Comment;

import java.util.List;

/* Hashed password are securely handled by firebase authentification */

public class ConnectedUser extends User {
    private String username;
    private String email;

    public ConnectedUser() {
        super(UserType.CONNECTED);
    }

    public ConnectedUser(String userId, String username, String email) {
        super(userId, UserType.CONNECTED);

        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
