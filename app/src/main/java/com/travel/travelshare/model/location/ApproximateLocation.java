package com.travel.travelshare.model.location;

public class ApproximateLocation extends Location {
    private String name;
    private String city;
    private String region;
    private String country;

    public ApproximateLocation(String name, String city, String region, String country) {
        this.setName(name);
        this.setCity(city);
        this.setRegion(region);
        this.setCountry(country);
    }

    private void setCountry(String country) {
        this.country = country;
    }

    private String getCountry() {
        return this.country;
    }

    private void setRegion(String region) {
        this.region = region;
    }

    private String getRegion() {
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
}
