package com.travel.travelshare.ui.dialog;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travel.travelshare.LocationUtils;
import com.travel.travelshare.model.location.ExactLocation;
import com.travel.travelshare.model.location.Location;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocationPickerDialogViewModel extends ViewModel {
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setSelectedLocation(Location location) {
        this.selectedLocation.setValue(location);
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public void searchFromMap(Context context, double lat, double lon) {
        LocationUtils utils = new LocationUtils(context);

        isLoading.setValue(true);
        executor.execute(() -> {
            utils.getLocationFromCoords(lat, lon, new LocationUtils.OnLocationResultListener() {
                @Override
                public void onResult(ExactLocation location) {
                    selectedLocation.postValue(location);
                    isLoading.postValue(false);
                }
                @Override
                public void onError(Exception e) { isLoading.postValue(false); }
            });
        });
    }

    public void searchFromAddress(Context context, String name, String city, String region, String country) {
        LocationUtils utils = new LocationUtils(context);

        isLoading.setValue(true);
        executor.execute(() -> {
            Location loc = utils.getLocationFromApproximateAddress(name, city, region, country);
            selectedLocation.postValue(loc);
            isLoading.postValue(false);
        });
    }
}