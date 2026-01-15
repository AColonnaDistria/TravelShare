package com.travel.travelshare.model;

import com.google.firebase.Timestamp;
import com.travel.travelshare.model.location.Location;

import java.util.Date;

public class DiscoverFilter {
    private String placeType;
    private Timestamp startDate;
    private Timestamp endDate;
    private String authorName;
    private Location nearLocation;
    private boolean searchBySimilarity;

    public DiscoverFilter() {}

    public DiscoverFilter(String placeType, Timestamp startDate, Timestamp endDate, String authorName, Location nearLocation, boolean searchBySimilarity) {
        this.setPlaceType(placeType);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setAuthorName(authorName);
        this.setNearLocation(nearLocation);
        this.setSearchBySimilarity(searchBySimilarity);
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Location getNearLocation() {
        return nearLocation;
    }

    public void setNearLocation(Location nearLocation) {
        this.nearLocation = nearLocation;
    }

    public boolean isSearchBySimilarity() {
        return searchBySimilarity;
    }

    public void setSearchBySimilarity(boolean searchBySimilarity) {
        this.searchBySimilarity = searchBySimilarity;
    }
}