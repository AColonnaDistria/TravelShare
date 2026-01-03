package com.travel.travelshare.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.travel.travelshare.databinding.FragmentMapBinding;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.repositories.PostRepository;

import org.osmdroid.api.IMapController;
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

            map.getOverlays().add(marker);
        }

        binding.map.invalidate();
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

        List<PicturePost> picturePosts = this.postRepository.ge

        addMarkersToMap()

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}