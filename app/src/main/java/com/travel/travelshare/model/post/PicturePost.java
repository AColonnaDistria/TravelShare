package com.travel.travelshare.model.post;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.annotation.Annotation;
import com.travel.travelshare.model.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PicturePost {
    private static final int ANNOTATIONS_LIMIT = 16;

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

    public PicturePost(
            String authorId, String photo_URI, String description,
            String instructions, Timestamp date, Timestamp createdAt,
            boolean visibility, Location location) {
        this.authorId = authorId;
        this.setPhoto_URI(photo_URI);
        this.setDescription(description);
        this.setInstructions(instructions);
        this.setDate(date);
        this.setVisibility(visibility);
        this.setLocation(location);

        this.createdAt = createdAt;
    }

    public PicturePost(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getPhoto_URI() {
        return photo_URI;
    }

    public void setPhoto_URI(String photo_URI) {
        this.photo_URI = photo_URI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public boolean isVisible() {
        return this.visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void toggleVisibility() {
        this.visibility = !this.visibility;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Annotation> getAnnotations() {
        return Collections.unmodifiableList(this.annotations);
    }

    public boolean setAnnotations(List<Annotation> annotations) {
        if (annotations.size() >= ANNOTATIONS_LIMIT) return false;

        this.annotations.clear();
        return this.annotations.addAll(annotations);
    }

    public boolean addAnnotation(Annotation annotation) {
        if (this.annotations.size() >= ANNOTATIONS_LIMIT) return false;

        return this.annotations.add(annotation);
    }
}
