package com.travel.travelshare.ui.likes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travelshare.R;
import com.travel.travelshare.ui.discover.MosaicAdapter;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity {
    private LikesViewModel viewModel;
    private MosaicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        // Initialize UI
        RecyclerView recyclerView = findViewById(R.id.likes_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MosaicAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(LikesViewModel.class);

        // Observe data changes
        viewModel.getLikedPosts().observe(this, posts -> {
            adapter.addPosts(posts); // Assuming setPosts method exists in MosaicAdapter
        });

        // Load data for current user (Replace "current_user_id" with actual auth ID)
        viewModel.loadLikedPosts("current_user_id");
    }
}