package com.travel.travelshare.model.location;

public class ApproximateLocation extends Location {
    public ApproximateLocation() {
        super(LocationType.APPROXIMATE);
    }

    public ApproximateLocation(String name, String city, String region, String country, double latitude, double longitude, String geohash) {
        super(LocationType.APPROXIMATE, name, city, region, country, latitude, longitude, geohash);
    }
}
