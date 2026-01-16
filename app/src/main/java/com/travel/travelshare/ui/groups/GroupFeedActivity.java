package com.travel.travelshare.ui.groups;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.Timestamp;
import com.travel.travelshare.Auth;
import com.travel.travelshare.R;
import com.travel.travelshare.ui.cardview.CardViewActivity;
import com.travel.travelshare.ui.discover.MosaicAdapter;
import com.travel.travelshare.ui.elements.ReturnBarFragment;
import com.travel.travelshare.ui.likes.LikesViewModel;
import com.travel.travelshare.ui.likes.LikesViewModelFactory;

import java.util.Date;
import java.util.Locale;

public class GroupFeedActivity extends AppCompatActivity implements ReturnBarFragment.OnCloseRequestedListener {
    private GroupFeedViewModel mViewModel;
    private MosaicAdapter mosaicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feed);

        RecyclerView recyclerView = findViewById(R.id.group_feed_recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mosaicAdapter = new MosaicAdapter();
        recyclerView.setAdapter(mosaicAdapter);

        this.mViewModel = new ViewModelProvider(this).get(GroupFeedViewModel.class);

        String groupId = getIntent().getStringExtra("GROUP_ID");
        this.mViewModel.setGroupId(groupId);
        this.mViewModel.loadSharedToPosts(groupId);

        this.mViewModel.getSharedToPosts().observe(this, posts -> {
            if (posts != null) {
                mosaicAdapter.addPosts(posts);
            }
        });

        mosaicAdapter.setOnItemClickListener(postId -> {
            this.mViewModel.fetchPost(postId, post -> {
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

                Intent intent = new Intent(com.travel.travelshare.ui.groups.GroupFeedActivity.this, CardViewActivity.class);

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
            });
        });
    }

    @Override
    public void onRequestClose() {
        finish();
    }
}