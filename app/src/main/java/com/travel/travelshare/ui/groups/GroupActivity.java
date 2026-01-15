package com.travel.travelshare.ui.groups;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.travel.travelshare.Auth;
import com.travel.travelshare.R;
import com.travel.travelshare.ui.elements.ReturnBarFragment;

public class GroupActivity extends AppCompatActivity implements ReturnBarFragment.OnCloseRequestedListener {
    private GroupViewModel mViewModel;
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        RecyclerView recyclerView = findViewById(R.id.group_recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        groupAdapter = new GroupAdapter();
        recyclerView.setAdapter(groupAdapter);

        GroupViewModelFactory factory = new GroupViewModelFactory(Auth.getInstance());
        this.mViewModel = new ViewModelProvider(this, factory).get(GroupViewModel.class);

        this.mViewModel.getGroups().observe(this, groups -> {
            if (groups != null) {
                groupAdapter.addGroups(groups);
            }
        });
    }

    @Override
    public void onRequestClose() {
        finish();
    }
}
