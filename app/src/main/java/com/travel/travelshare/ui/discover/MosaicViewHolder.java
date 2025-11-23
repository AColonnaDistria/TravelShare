package com.travel.travelshare.ui.discover;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travel.travelshare.R;

public class MosaicViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    View itemView;

    public MosaicViewHolder(final View itemView) {
        super(itemView);

        this.itemView = itemView;
        this.imageView = (ImageView)itemView.findViewById(R.id.mosaic_image);
    }

    public void show(String imagePath) {
        Uri uri = Uri.parse(imagePath);

        Glide.with(itemView)
                .load(uri)
                .error(R.drawable.explore_24px) // optional: show error if loading fails
                .into(imageView);
    }
}
