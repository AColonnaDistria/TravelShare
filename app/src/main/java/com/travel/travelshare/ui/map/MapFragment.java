package com.travel.travelshare.ui.map;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.travel.travelshare.MainActivity;
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
                    intent.putExtra("FULL_TEXT_DESCRIPTION", post.getDescription());
                    intent.putExtra("FULL_TEXT_INSTRUCTIONS", post.getInstructions());
                    intent.putExtra("COUNT_LIKES", 0);
                    intent.putExtra("COUNT_DISLIKES", 0);
                    intent.putExtra("IS_PUBLIC", post.getVisibility());
                    intent.putExtra("PUBLISH_DATE", dateStr);
                    intent.putExtra("LOCATION_NAME", post.getLocation().getName());
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
        super.onDestroyView();
        binding = null;
    }
}