package com.travel.travelshare.model.post;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.annotation.Annotation;
import com.travel.travelshare.model.location.Location;

import java.util.ArrayList;

public class PicturePost {

    private String authorId;
    private String photo_URI;
    private String description;
    private String instructions;
    private Timestamp date;
    private Timestamp createdAt;
    private boolean visibility;
    private Location location;
    // Up to 16 annotations
    private ArrayList<Annotation> annotations;

    /*
    private ArrayList<String> shared_to;
    private ArrayList<String> comments;
    private ArrayList<String> liked_by;
    */

    public PicturePost() {}

    public PicturePost(String imageUri, boolean visibility, Timestamp timestamp, String description, String instructions, String location) {

    }
}
