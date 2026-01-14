package com.travel.travelshare;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.model.location.ExactLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {
    private final Geocoder geocoder;

    public LocationUtils(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public ApproximateLocation getLocationFromApproximateAddress(String locationName, String city, String region, String country) {
        StringBuilder addressBuilder = new StringBuilder();
        if (!city.isEmpty()) addressBuilder.append(city).append(", ");
        if (!region.isEmpty()) addressBuilder.append(region).append(", ");
        if (!country.isEmpty()) addressBuilder.append(country);

        String address = (locationName + " " + city + " " + region + " " + country).trim();
        if (address.isEmpty())
            return null;

        try {
            List<Address> results = geocoder.getFromLocationName(address, 1);
            if (results != null && !results.isEmpty()) {
                Address match = results.get(0);
                double lat = match.getLatitude();
                double lon = match.getLongitude();
                String geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon));

                return new ApproximateLocation(
                    locationName,
                    city,
                    region,
                    country,
                    lat,
                    lon,
                    geohash
                );
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ExactLocation getLocationFromCoords(double lat, double lon) {
        try {
            List<Address> addresses = this.geocoder.getFromLocation(lat, lon, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                String placeName = address.getFeatureName();
                String city = address.getLocality();
                String region = address.getAdminArea();
                String country = address.getCountryName();
                String geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon));

                return new ExactLocation(
                        placeName,
                        city,
                        region,
                        country,
                        lat, lon,
                        geohash
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
