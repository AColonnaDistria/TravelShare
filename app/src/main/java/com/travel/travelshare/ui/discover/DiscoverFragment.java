package com.travel.travelshare.ui.discover;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.databinding.FragmentDiscoverBinding;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.ui.cardview.CardViewActivity;
import com.travel.travelshare.ui.map.MapFragment;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DiscoverFragment extends Fragment {

    private FragmentDiscoverBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DiscoverViewModel dashboardViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.discoverRecyclerView;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        MosaicAdapter mosaicAdapter = new MosaicAdapter();
        PostRepository postRepository = new PostRepository();

        mosaicAdapter.setOnItemClickListener(postId -> {
            postRepository.getItem(
                    postId,
                    new OnSuccessListener<PicturePost>() {
                @Override
                public void onSuccess(PicturePost post) {
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

                    Intent intent = new Intent(getContext(), CardViewActivity.class);

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
                }
            });
        });

        postRepository.getPage(new OnSuccessListener<List<PicturePost>>() {
            @Override
            public void onSuccess(List<PicturePost> picturePosts) {
                List<String> imageUris = picturePosts.stream().map(post -> post.getPhoto_URI()).collect(Collectors.toList());
                List<String> imagePostIds = picturePosts.stream().map(post -> post.getId()).collect(Collectors.toList());

                mosaicAdapter.addImages(imageUris, imagePostIds);
            }
        }, 16, 0);

        recyclerView.setAdapter(mosaicAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}