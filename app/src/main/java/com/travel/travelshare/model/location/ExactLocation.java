package com.travel.travelshare.model.location;

public class ExactLocation extends Location {
    private double latitude;
    private double longitude;

    public ExactLocation() {
        super(LocationType.EXACT);
    }

    public ExactLocation(double latitude, double longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
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
}
