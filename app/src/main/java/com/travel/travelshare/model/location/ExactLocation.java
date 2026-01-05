package com.travel.travelshare.model.location;

public class ExactLocation extends Location {
    public ExactLocation() {
        super(LocationType.EXACT);
    }

    public ExactLocation(String name, String city, String region, String country, double latitude, double longitude, String geohash) {
        super(LocationType.EXACT, name, city, region, country, latitude, longitude, geohash);
    }
}