package com.travel.travelshare.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.databinding.FragmentMapBinding;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.ui.cardview.CardViewActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;


public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private MapView map;
    private IMapController mapController;

    private PostRepository postRepository;

    private void addMarkersToMap(List<PicturePost> posts) {
        map.getOverlays().clear();

        for (PicturePost post : posts) {
            if (post.getLocation() == null) continue;

            GeoPoint point = new GeoPoint(post.getLocation().getLatitude(), post.getLocation().getLongitude());
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setTitle(post.getLocation().getName());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    Intent intent = new Intent(MapFragment.this.getContext(), CardViewActivity.class);

                    intent.putExtra("IMAGE_PATH", "");
                    intent.putExtra("POSITION", 0);

                    startActivity(intent);

                    return true;
                }
            });

            map.getOverlays().add(marker);
        }

        map.invalidate();
    }

    private static double ZOOM_LEVEL_PICTURE_POST_TRIGGER = 15.0;

    private void refreshMap() {
        this.postRepository.getAll(new OnSuccessListener<List<PicturePost>>() {
            @Override
            public void onSuccess(List<PicturePost> picturePosts) {
                addMarkersToMap(picturePosts);
            }
        });

        /*
        double zoom = map.getZoomLevelDouble();
        double latCenter = map.getMapCenter().getLatitude();
        double lonCenter = map.getMapCenter().getLongitude();

        double radiusInMeters = 10000.0;

        this.postRepository.getNearby(latCenter, lonCenter, radiusInMeters, new OnSuccessListener<List<PicturePost>>() {
            @Override
            public void onSuccess(List<PicturePost> picturePosts) {
                addMarkersToMap(picturePosts);
            }
        });

         */
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.postRepository = new PostRepository();

        MapViewModel homeViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.map = binding.map;
        map.setMultiTouchControls(true);

        map.setMinZoomLevel(4.0);
        map.setMaxZoomLevel(20.0);

        this.mapController = map.getController();
        mapController.setZoom(15.0);

        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        map.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onZoom(ZoomEvent event) {
                // Get the new zoom level
                double zoom = event.getZoomLevel();
                if (zoom >= MapFragment.ZOOM_LEVEL_PICTURE_POST_TRIGGER) {
                    refreshMap();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onScroll(ScrollEvent event) {
                double zoom = map.getZoomLevelDouble();
                if (zoom >= MapFragment.ZOOM_LEVEL_PICTURE_POST_TRIGGER) {
                    refreshMap();
                    return true;
                }

                return false;
            }
        }, 200));

        refreshMap();

        /*
        this.postRepository.getAll(new OnSuccessListener<List<PicturePost>>() {
            @Override
            public void onSuccess(List<PicturePost> picturePosts) {
                addMarkersToMap(picturePosts);
            }
        });
        */

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}