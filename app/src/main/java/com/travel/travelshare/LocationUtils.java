package com.travel.travelshare;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.model.location.ExactLocation;

import org.osmdroid.bonuspack.location.NominatimPOIProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationUtils {
    private final Geocoder geocoder;
    private final NominatimPOIProvider poiProvider;
    private final ExecutorService executor;

    public LocationUtils(Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.poiProvider = new NominatimPOIProvider("TravelShareApp");
        this.executor = Executors.newSingleThreadExecutor();
    }

    private String resolveBestName(Address addr, double lat, double lon) {
        try {

        } catch (Exception ignored) {}

        String name = addr.getFeatureName();
        if (name != null && name.matches("\\d+")) {
            return addr.getThoroughfare();
        }

        return (name != null) ? name : "";
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

    public interface OnLocationResultListener {
        void onResult(ExactLocation location);
        void onError(Exception e);
    }

    public void getLocationFromCoords(double lat, double lon, OnLocationResultListener listener) {
        executor.execute(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                if (addresses == null || addresses.isEmpty()) {
                    listener.onResult(null);
                    return;
                }

                Address addr = addresses.get(0);
                String finalName = resolveBestName(addr, lat, lon);

                ExactLocation result = new ExactLocation(
                        finalName,
                        addr.getLocality(),
                        addr.getAdminArea(),
                        addr.getCountryName(),
                        lat, lon,
                        GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lon))
                );

                listener.onResult(result);
            } catch (Exception e) {
                listener.onError(e);
            }
        });
    }
}
