package com.travel.travelshare.ui.groups;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travel.travelshare.R;

import org.w3c.dom.Text;

public class GroupViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView groupNameText;
    TextView groupMemberCount;
    private static int MAX_MEMBERS_COUNT = 8;

    View itemView;

    public GroupViewHolder(final View itemView) {
        super(itemView);

        this.itemView = itemView;
        this.imageView = (ImageView)itemView.findViewById(R.id.group_mosaic_image);
        this.groupNameText = (TextView)itemView.findViewById(R.id.group_name_text);
        this.groupMemberCount = (TextView)itemView.findViewById(R.id.group_member_count);
    }

    public void show(String imagePath, String groupName, int groupMemberCount) {
        Uri uri = Uri.parse(imagePath);

        Glide.with(itemView)
                .load(uri)
                .error(R.drawable.explore_24px) // optional: show error if loading fails
                .into(imageView);

        this.groupNameText.setText(groupName);
        this.groupMemberCount.setText(String.format("%d / %d members", groupMemberCount, MAX_MEMBERS_COUNT));
    }
}
