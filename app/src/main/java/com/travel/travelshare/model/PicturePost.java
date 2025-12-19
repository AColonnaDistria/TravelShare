package com.travel.travelshare.model;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.annotation.Annotation;
import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.user.UserGroup;

import java.util.ArrayList;

public class PicturePost {
    private String photo_URI;
    private String description;
    private String instructions;
    private Timestamp date;
    private Timestamp createdAt;
    private boolean visibility;

    private Location location;
    private ArrayList<UserGroup> shared_to;

    private ArrayList<Comment> comments;
    private ArrayList<Annotation> annotations;

    public PicturePost() {}

    public PicturePost(String imageUri, boolean visibility, Timestamp timestamp, String description, String instructions, String location) {

    }
}
