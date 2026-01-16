package com.travel.travelshare.ui.dialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.geofire.util.GeoUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.travel.travelshare.LocationUtils;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.DialogLocationPickerBinding;
import com.travel.travelshare.model.location.ExactLocation;
import com.travel.travelshare.model.location.Location;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class LocationPickerDialog extends BottomSheetDialogFragment {

    private static double ZOOM_LEVEL_PICTURE_POST_TRIGGER = 15.0;

    public interface LocationResultListener {
        void onLocationSelected(Location location);
    }

    private LocationResultListener listener;
    private DialogLocationPickerBinding binding; // Assuming you have a layout file

    private LocationPickerDialogViewModel mViewModel;

    private Marker selectedMarker;

    public void setLocationResultListener(LocationResultListener listener) {
        this.listener = listener;
    }

    private void setupMapLogic() {
        selectedMarker = new Marker(binding.mapPicker);
        selectedMarker.setTitle("Selected Location");
        selectedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);


        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.map_marker);
        selectedMarker.setIcon(icon);

        binding.mapPicker.getOverlays().add(selectedMarker);

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                selectedMarker.setPosition(p);
                binding.mapPicker.invalidate(); // Refresh map to show marker move

                mViewModel.searchFromMap(getContext(), p.getLatitude(), p.getLongitude());
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(mReceive);
        binding.mapPicker.getOverlays().add(eventsOverlay);

        GpsMyLocationProvider provider = new GpsMyLocationProvider(requireContext());
        MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(provider, binding.mapPicker);
        myLocationOverlay.enableMyLocation();

        myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                final GeoPoint myLocation = myLocationOverlay.getMyLocation();
                if (myLocation != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.mapPicker.getController().animateTo(myLocation);
                            binding.mapPicker.getController().setZoom(15.0); // Set a closer zoom level

                            selectedMarker.setPosition(myLocation);
                            binding.mapPicker.invalidate();

                            mViewModel.searchFromMap(getContext(), myLocation.getLatitude(), myLocation.getLongitude());
                        }
                    });
                }
            }
        });
        binding.mapPicker.getOverlays().add(myLocationOverlay);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mViewModel = new ViewModelProvider(this).get(LocationPickerDialogViewModel.class);

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
                listener.onLocationSelected(this.mViewModel.getSelectedLocation().getValue());
            }
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getSelectedLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                binding.pickerLocationName.setText(location.getName());
                binding.pickerLocationCity.setText(location.getCity());
                binding.pickerLocationRegion.setText(location.getRegion());
                binding.pickerLocationCountry.setText(location.getCountry());
            }
        });
    }
}

