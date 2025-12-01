package com.travel.travelshare.ui.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travelshare.R;

import java.util.ArrayList;
import java.util.List;

public class MosaicAdapter extends RecyclerView.Adapter<MosaicViewHolder> {
    private List<String> imageList = null;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String imagePath, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MosaicAdapter() {
        this.imageList = new ArrayList<>();

        // Adding dummy data so you can see the grid immediately.
        imageList.add("file:///android_asset/stock_image_1.png");
        imageList.add("file:///android_asset/stock_image_2.png");
        imageList.add("file:///android_asset/stock_image_3.png");
        imageList.add("file:///android_asset/stock_image_4.png");
        imageList.add("file:///android_asset/stock_image_1.png");
        imageList.add("file:///android_asset/stock_image_2.png");
        imageList.add("file:///android_asset/stock_image_3.png");
        imageList.add("file:///android_asset/stock_image_4.png");
        imageList.add("file:///android_asset/stock_image_1.png");
        imageList.add("file:///android_asset/stock_image_2.png");
        imageList.add("file:///android_asset/stock_image_3.png");
        imageList.add("file:///android_asset/stock_image_4.png");
        imageList.add("file:///android_asset/stock_image_1.png");
        imageList.add("file:///android_asset/stock_image_2.png");
        imageList.add("file:///android_asset/stock_image_3.png");
        imageList.add("file:///android_asset/stock_image_4.png");
    }

    @NonNull
    @Override
    public MosaicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_discover, parent, false);
        return new MosaicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MosaicViewHolder holder, int position) {
        String imagePath = imageList.get(position);

        holder.show(imagePath);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(imagePath, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.imageList.size();
    }
}
