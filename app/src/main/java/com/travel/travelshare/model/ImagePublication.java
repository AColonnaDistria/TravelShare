package com.travel.travelshare.model;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;

public class ImagePublication {
    private boolean visibility;
    private String imageUri;
    private Timestamp timestamp;
    private String description;
    private String instructions;
    private String location;

    public ImagePublication() {}

    public ImagePublication(String imageUri, boolean visibility, Timestamp timestamp, String description, String instructions, String location) {
        this.imageUri = imageUri;
        this.visibility = visibility;
        this.timestamp = timestamp;
        this.description = description;
        this.instructions = instructions;
        this.location = location;
    }

    // Add Getters and Setters here
    public Timestamp getTimestamp() { return this.timestamp; }
    public String getImageURI() { return this.imageUri; }
    public boolean getVisibility() { return this.visibility; }
    public String getDescription() { return this.description; }
    public String getInstructions() { return this.instructions; }
    public String getLocation() { return this.location; }
}
