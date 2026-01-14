package com.travel.travelshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.travel.travelshare.databinding.DialogLocationPickerBinding;
import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.location.ExactLocation;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.ui.map.MapFragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class LocationPickerDialog extends BottomSheetDialogFragment {

    private static double ZOOM_LEVEL_PICTURE_POST_TRIGGER = 15.0;

    public interface LocationResultListener {
        void onLocationSelected(Location location);
    }

    private LocationUtils locationUtils;
    private LocationResultListener listener;
    private DialogLocationPickerBinding binding; // Assuming you have a layout file

    public void setLocationResultListener(LocationResultListener listener) {
        this.listener = listener;
    }

    // Inside your LocationPickerDialog (BottomSheetDialogFragment)
    private Marker selectedMarker;

    private void setupMapLogic() {
        // 1. Initialize the Marker
        selectedMarker = new Marker(binding.mapPicker);
        selectedMarker.setTitle("Selected Location");
        selectedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        binding.mapPicker.getOverlays().add(selectedMarker);

        // 2. Capture Map Clicks
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // Move the marker to where the user tapped
                selectedMarker.setPosition(p);
                binding.mapPicker.invalidate(); // Refresh map to show marker move

                // Optionally update the manual Lat/Lon EditTexts
                //binding.editLat.setText(String.valueOf(p.getLatitude()));
                //binding.editLon.setText(String.valueOf(p.getLongitude()));
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(mReceive);
        binding.mapPicker.getOverlays().add(eventsOverlay);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.locationUtils = new LocationUtils(getContext());

        binding = DialogLocationPickerBinding.inflate(inflater, container, false);
        binding.mapPicker.setMultiTouchControls(true);

        binding.mapPicker.setMinZoomLevel(4.0);
        binding.mapPicker.setMaxZoomLevel(20.0);

        setupMapLogic();

        IMapController mapController = binding.mapPicker.getController();
        mapController.setZoom(15.0);

        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        // Tab selection logic
        binding.locationTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.viewPrecise.setVisibility(View.VISIBLE);
                    binding.viewApproximate.setVisibility(View.GONE);
                } else {
                    binding.viewPrecise.setVisibility(View.GONE);
                    binding.viewApproximate.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.mapPicker.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);


                    break;
            }

            return v.onTouchEvent(event);
        });

        binding.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                if (binding.locationTabs.getSelectedTabPosition() == 0) {
                    double lat = binding.mapPicker.getMapCenter().getLatitude();
                    double lon = binding.mapPicker.getMapCenter().getLongitude();

                    listener.onLocationSelected(locationUtils.getLocationFromCoords(lat, lon));
                }
                else {
                    String name = binding.pickerLocationName.getText().toString();
                    String city = binding.pickerLocationCity.getText().toString();
                    String region = binding.pickerLocationRegion.getText().toString();
                    String country = binding.pickerLocationCountry.getText().toString();

                    listener.onLocationSelected(locationUtils.getLocationFromApproximateAddress(
                        name,
                        city,
                        region,
                        country
                    ));
                }
            }
            dismiss(); // Close the dialog
        });

        return binding.getRoot();
    }
}
