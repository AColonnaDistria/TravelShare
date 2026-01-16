package com.travel.travelshare.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentMapBinding;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.UserRepository;
import com.travel.travelshare.ui.cardview.CardViewActivity;
import com.travel.travelshare.ui.publish.PublishViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private MapView map;
    private IMapController mapController;
    private MapViewModel mViewModel;
    private static double ZOOM_LEVEL_PICTURE_POST_TRIGGER = 15.0;

    private void addMarkersToMap(List<PicturePost> posts) {
        map.getOverlays().clear();

        for (PicturePost post : posts) {
            if (post.getLocation() == null) continue;

            GeoPoint point = new GeoPoint(post.getLocation().getLatitude(), post.getLocation().getLongitude());
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setTitle(post.getLocation().getName());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.map_marker);
            marker.setIcon(icon);

            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    /*
                        INTENT:

                        IMAGE_PATH
                        FULL_TEXT_DESCRIPTION
                        FULL_TEXT_INSTRUCTIONS
                        COUNT_LIKES
                        COUNT_DISLIKES
                        IS_PUBLIC
                        PUBLISH_DATE
                        AUTHOR
                        LOCATION_NAME*/
                    Timestamp timestamp = post.getDate();
                    String dateStr = "";

                    if (timestamp != null) {
                        try {
                            Date date = timestamp.toDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            dateStr = sdf.format(date);
                        }
                        catch (Exception e) {
                            dateStr = "1970-01-01";
                        }
                    }

                    Intent intent = new Intent(MapFragment.this.getContext(), CardViewActivity.class);

                    intent.putExtra("IMAGE_PATH", post.getPhoto_URI());
                    intent.putExtra("POST_ID", post.getId());
                    intent.putExtra("FULL_TEXT_DESCRIPTION", post.getDescription());
                    intent.putExtra("FULL_TEXT_INSTRUCTIONS", post.getInstructions());
                    intent.putExtra("COUNT_LIKES", post.getCountLikes());
                    intent.putExtra("IS_PUBLIC", post.getVisibility());
                    intent.putExtra("PUBLISH_DATE", dateStr);
                    intent.putExtra("LOCATION_NAME", post.getLocation().getName());
                    intent.putExtra("LATITUDE", post.getLocation().getLatitude());
                    intent.putExtra("LONGITUDE", post.getLocation().getLongitude());
                    intent.putExtra("AUTHOR", post.getAuthorId());

                    startActivity(intent);

                    return true;
                }
            });

            map.getOverlays().add(marker);
        }

        map.invalidate();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.mViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.map = binding.map;
        map.setMultiTouchControls(true);

        map.setMinZoomLevel(4.0);
        map.setMaxZoomLevel(20.0);
        map.setBuiltInZoomControls(false);

        this.mapController = map.getController();
        mapController.setZoom(15.0);

        // Set the center as location

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
            mapController.setCenter(startPoint);
        }

        if (mViewModel.getLastMapCenter() != null) {
            mapController.setCenter(mViewModel.getLastMapCenter());
            mapController.setZoom(mViewModel.getLastZoomLevel());
        } else {
            GpsMyLocationProvider provider = new GpsMyLocationProvider(requireContext());
            MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(provider, binding.map);
            myLocationOverlay.enableMyLocation();

            myLocationOverlay.runOnFirstFix(new Runnable() {
                @Override
                public void run() {
                    final GeoPoint myLocation = myLocationOverlay.getMyLocation();
                    if (myLocation != null) {
                        requireActivity().runOnUiThread(() -> {
                            binding.map.getController().animateTo(myLocation);
                            binding.map.getController().setZoom(15.0);
                        });
                    }
                }
            });
            binding.map.getOverlays().add(myLocationOverlay);

        }

        map.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onZoom(ZoomEvent event) {
                // Get the new zoom level
                double zoom = event.getZoomLevel();
                if (zoom >= MapFragment.ZOOM_LEVEL_PICTURE_POST_TRIGGER) {
                    mViewModel.loadPosts();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onScroll(ScrollEvent event) {
                double zoom = map.getZoomLevelDouble();
                if (zoom >= MapFragment.ZOOM_LEVEL_PICTURE_POST_TRIGGER) {
                    mViewModel.loadPosts();
                    return true;
                }

                return false;
            }
        }, 200));

        this.mViewModel.mPosts.observe(this.getViewLifecycleOwner(), this::addMarkersToMap);
        this.mViewModel.loadPosts();

        return root;
    }

    @Override
    public void onDestroyView() {
        if (map != null) {
            mViewModel.saveMapState(
                    (GeoPoint) map.getMapCenter(),
                    map.getZoomLevelDouble()
            );
        }

        super.onDestroyView();
        binding = null;
    }
}