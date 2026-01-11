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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.travel.travelshare.Auth;
import com.travel.travelshare.R;
import com.travel.travelshare.ui.auth.LoginViewModel;
import com.travel.travelshare.ui.auth.LoginViewModelFactory;
import com.travel.travelshare.ui.discover.MosaicAdapter;
import com.travel.travelshare.ui.elements.ReturnBarFragment;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity implements ReturnBarFragment.OnCloseRequestedListener {
    private LikesViewModel mViewModel;
    private MosaicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        RecyclerView recyclerView = findViewById(R.id.likes_recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MosaicAdapter();
        recyclerView.setAdapter(adapter);

        LikesViewModelFactory factory = new LikesViewModelFactory(Auth.getInstance());
        this.mViewModel = new ViewModelProvider(this, factory).get(LikesViewModel.class);

        this.mViewModel.getLikedPosts().observe(this, posts -> {
            if (posts != null) {
                adapter.addPosts(posts);
            }
        });
    }

    @Override
    public void onRequestClose() {
        finish();
    }
}