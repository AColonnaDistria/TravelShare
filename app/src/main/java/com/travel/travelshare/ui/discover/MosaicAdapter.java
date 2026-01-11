package com.travel.travelshare.ui.discover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travelshare.R;
import com.travel.travelshare.model.post.PicturePost;

import java.util.ArrayList;
import java.util.List;

public class MosaicAdapter extends RecyclerView.Adapter<MosaicViewHolder> {
    private List<String> imageList = null;
    private ArrayList<String> imagePostIds = null;
    private OnItemClickListener listener;

    public static int CHUNK_SIZE = 16;

    public interface OnItemClickListener {
        void onItemClick(String postId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MosaicAdapter() {
        this.imageList = new ArrayList<>();
        this.imagePostIds = new ArrayList<>();
    }

    public void addImages(List<String> images, List<String> postIds) {
        int startPos = imageList.size();
        this.imageList.addAll(images);
        this.imagePostIds.addAll(postIds);

        notifyItemRangeInserted(startPos, images.size());
    }

    public void addPosts(List<PicturePost> posts) {
        int startPos = imageList.size();

        for (PicturePost post : posts) {
            this.imageList.add(post.getPhoto_URI());
            this.imagePostIds.add(post.getId());
        }
    }

    @NonNull
    @Override
    public MosaicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_discover, parent, false);
        return new MosaicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MosaicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imagePath = imageList.get(position);

        holder.show(imagePath);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(imagePostIds.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.imageList.size();
    }
}
