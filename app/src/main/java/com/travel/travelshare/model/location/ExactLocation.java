package com.travel.travelshare.model.location;

public class ExactLocation extends Location {
    private double latitude;
    private double longitude;

    private String name; // estimated
    private String city; // estimated
    private String region; // estimated
    private String country; // estimated

    public ExactLocation() {
        super(LocationType.EXACT);
    }

    public ExactLocation(String name, String city, String region, String country, double latitude, double longitude) {
        super(LocationType.EXACT, name, city, region, country, latitude, longitude);

        this.setName(name);
        this.setCity(city);
        this.setRegion(region);
        this.setCountry(country);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
}
