package com.travel.travelshare.ui.groups;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travelshare.R;
import com.travel.travelshare.model.user.UserGroup;
import com.travel.travelshare.ui.discover.MosaicViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {
    private List<String> imageList = null;
    private ArrayList<String> groupsIds = null;
    private ArrayList<String> groupsNames = null;
    private ArrayList<Integer> groupsMembersCount = null;
    private OnItemClickListener listener;

    public static int CHUNK_SIZE = 16;

    public interface OnItemClickListener {
        void onItemClick(String postId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public GroupAdapter() {
        this.imageList = new ArrayList<>();
        this.groupsIds = new ArrayList<>();
        this.groupsNames = new ArrayList<>();
        this.groupsMembersCount = new ArrayList<>();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    public void addGroups(List<UserGroup> groups) {
        if (groups != null) {
            int startPos = imageList.size();

            for (UserGroup group : groups) {
                this.imageList.add(group.getBannerPhoto_URI());
                this.groupsIds.add(group.getId());
                this.groupsNames.add(group.getName());

                List<String> usersId = group.getUsersId();

                this.groupsMembersCount.add(group.getUsersId().size());
            }

            notifyItemRangeInserted(startPos, groups.size());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.show(imageList.get(position), groupsNames.get(position), groupsMembersCount.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(groupsIds.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.imageList.size();
    }
}
