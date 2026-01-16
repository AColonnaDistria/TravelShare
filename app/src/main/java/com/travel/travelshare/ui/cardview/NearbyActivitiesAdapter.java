package com.travel.travelshare.ui.cardview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travel.travelshare.R;
import com.travel.travelshare.model.travelpath.TravelPath_Activity;

import java.util.List;

public class NearbyActivitiesAdapter extends RecyclerView.Adapter<NearbyActivitiesAdapter.ViewHolder> {
    private List<TravelPath_Activity> activities;
    private OnActivityClickListener listener;

    public interface OnActivityClickListener {
        void onClick(TravelPath_Activity activity);
    }

    public NearbyActivitiesAdapter(List<TravelPath_Activity> activities, OnActivityClickListener listener) {
        this.activities = activities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelPath_Activity activity = activities.get(position);

        holder.nameView.setText(activity.getNom());

        if (activity.getSearch_labels() != null && !activity.getSearch_labels().isEmpty()) {
            holder.labelView.setText(activity.getSearch_labels().get(0));
        } else {
            holder.labelView.setText("Activité");
        }

        Glide.with(holder.itemView.getContext())
                .load(activity.getPhoto_claire())
                .centerCrop()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> listener.onClick(activity));
    }

    @Override
    public int getItemCount() { return activities.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView labelView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_mosaic);
            nameView = itemView.findViewById(R.id.text_activity_name);
            labelView = itemView.findViewById(R.id.text_activity_label);
        }
    }
}