package com.travel.travelshare.model.location;

import com.travel.travelshare.model.post.FollowableContent;

public class Location implements FollowableContent {
    private LocationType type;
    private double latitude;
    private double longitude;

    private String name; // estimated
    private String city; // estimated
    private String region; // estimated
    private String country; // estimated

    public Location() {}

    public Location(LocationType type, String name, String city, String region, String country, double latitude, double longitude) {
        this.type = type;

        this.setName(name);
        this.setCity(city);
        this.setRegion(region);
        this.setCountry(country);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
    public LocationType getLocationType() {
        return this.type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
