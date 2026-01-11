package com.travel.travelshare.ui.likes;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.Timestamp;
import com.travel.travelshare.Auth;
import com.travel.travelshare.R;
import com.travel.travelshare.ui.auth.LoginViewModel;
import com.travel.travelshare.ui.auth.LoginViewModelFactory;
import com.travel.travelshare.ui.cardview.CardViewActivity;
import com.travel.travelshare.ui.discover.MosaicAdapter;
import com.travel.travelshare.ui.elements.ReturnBarFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LikesActivity extends AppCompatActivity implements ReturnBarFragment.OnCloseRequestedListener {
    private LikesViewModel mViewModel;
    private MosaicAdapter mosaicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        RecyclerView recyclerView = findViewById(R.id.likes_recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mosaicAdapter = new MosaicAdapter();
        recyclerView.setAdapter(mosaicAdapter);

        LikesViewModelFactory factory = new LikesViewModelFactory(Auth.getInstance());
        this.mViewModel = new ViewModelProvider(this, factory).get(LikesViewModel.class);

        this.mViewModel.getLikedPosts().observe(this, posts -> {
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

                Intent intent = new Intent(LikesActivity.this, CardViewActivity.class);

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
            });
        });
    }

    @Override
    public void onRequestClose() {
        finish();
    }
}