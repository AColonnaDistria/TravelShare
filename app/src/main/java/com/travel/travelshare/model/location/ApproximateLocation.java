package com.travel.travelshare.model.location;

public class ApproximateLocation extends Location {
    private String name;
    private String city;
    private String region;
    private String country;

    private double latitude; // estimated
    private double longitude;   // estimated

    public ApproximateLocation() {
        super(LocationType.APPROXIMATE);
    }

    public ApproximateLocation(String name, String city, String region, String country, double latitude, double longitude) {
        super(LocationType.APPROXIMATE);

        this.setName(name);
        this.setCity(city);
        this.setRegion(region);
        this.setCountry(country);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return this.region;
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
