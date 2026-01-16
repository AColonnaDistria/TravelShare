package com.travel.travelshare.ui.cardview;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.travel.travelshare.Auth;
import com.travel.travelshare.R;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.travelpath.TravelPath_Activity;
import com.travel.travelshare.model.user.Like;
import com.travel.travelshare.repositories.LikeRepository;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.travelpath.TravelPath_ActivityRepository;
import com.travel.travelshare.ui.elements.ReturnBarFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CardViewActivity extends AppCompatActivity implements ReturnBarFragment.OnCloseRequestedListener {
    private ImageView imageCardView;
    private ImageView backButton;
    private TextView authorView;
    private TextView locationView;
    private TextView textDescriptionView;
    private TextView textInstructionsView;
    private TextView likesCountView;
    private TextView dislikesCountView;
    private TextView dateView;

    private Button publicPrivateButton;

    /* Variables */
    private String imagePath;
    private String fullTextDescription;
    private String fullTextInstructions;
    private int count_likes;
    private int count_dislikes;
    private boolean is_public;
    private LocalDate date;
    private String author;
    private boolean isLikedByUser;
    private String location;

    private LikeRepository likeRepository = new LikeRepository();
    private PostRepository postRepository = new PostRepository();
    private Auth auth = Auth.getInstance();

    private static String getFormattedDate(LocalDate date) {
        // 1. Format the first part: "Wednesday, November"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM", Locale.US);
        String dayMonth = date.format(formatter);

        // 2. Get the day number (e.g., 19)
        int day = date.getDayOfMonth();

        // 3. Generate the suffix (st, nd, rd, th)
        String suffix = getDayNumberSuffix(day);

        // 4. Combine them: "Wednesday, November" + " " + "19" + "th"
        return dayMonth + " " + day + suffix;
    }
    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    /*
        INTENT:

        IMAGE_PATH
        POST_ID
        FULL_TEXT_DESCRIPTION
        FULL_TEXT_INSTRUCTIONS
        COUNT_LIKES
        IS_PUBLIC
        PUBLISH_DATE
        AUTHOR
        LOCATION_NAME
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        this.imagePath = getIntent().getStringExtra("IMAGE_PATH");
        this.fullTextDescription = getIntent().getStringExtra("FULL_TEXT_DESCRIPTION"); //"  Description: This is a famous view of Lake Como, Italy, likely taken from Villa Monastero in Varenna.";

        if (this.fullTextDescription == null) {
            this.fullTextDescription = "This is a famous view of Lake Como, Italy, likely taken from Villa Monastero in Varenna.";
        }

        this.fullTextInstructions = getIntent().getStringExtra("FULL_TEXT_INSTRUCTIONS");  // "  Instructions: Supporting line text lorem ipsum dolor sit amet, consectetur.";

        if (this.fullTextInstructions == null) {
            this.fullTextInstructions = "Supporting line text lorem ipsum dolor sit amet, consectetur.";
        }

        this.count_likes = (int)getIntent().getLongExtra("COUNT_LIKES", 0);
        this.is_public = getIntent().getBooleanExtra("IS_PUBLIC", false);

        String dateStr = getIntent().getStringExtra("PUBLISH_DATE");

        if (dateStr == null) {
            dateStr = "1970-01-01";
        }

        try {
            this.date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        }
        catch (Exception exception) {
            this.date = LocalDate.of(1970, 01, 01);
        }

        this.author =  getIntent().getStringExtra("AUTHOR");

        if (this.author == null) {
            this.author = "alex2003";
        }

        this.location =  getIntent().getStringExtra("LOCATION_NAME");

        if (this.location == null) {
            this.location = "Lac de Côme";
        }

        this.imageCardView = findViewById(R.id.image_cardview);

        this.textDescriptionView = findViewById(R.id.text_description);
        this.textInstructionsView = findViewById(R.id.text_instructions);

        // Definit la date
        this.dateView = findViewById(R.id.dateView);
        this.dateView.setText(getFormattedDate(this.date));
        // Definit les likes
        this.likesCountView = findViewById(R.id.like_count);
        this.likesCountView.setText(String.valueOf(this.count_likes));

        //this.dislikesCountView = findViewById(R.id.dislike_count);
        //this.dislikesCountView.setText(String.valueOf(this.count_dislikes));

        // Definit public/privé
        this.publicPrivateButton = findViewById(R.id.public_private_button);
        if (this.is_public) {
            this.publicPrivateButton.setText("Public");
            this.publicPrivateButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.public_24px, 0, 0, 0);
        }
        else {
            this.publicPrivateButton.setText("Private");
            this.publicPrivateButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.visibility_off_24px, 0, 0, 0);
        }

        // Definit l'author
        this.authorView = findViewById(R.id.authorView);
        this.authorView.setText(String.format(getString(R.string.author_text), this.author));

        // Definit la localisation
        this.locationView = findViewById(R.id.locationView);
        this.locationView.setText(String.format(getString(R.string.location_text), this.location));

        // Definit la description et les instructions
        this.setIconTextView(this.textDescriptionView, String.format("  " + getString(R.string.description_text), fullTextDescription), R.drawable.info_24px);
        this.setIconTextView(this.textInstructionsView, String.format("  " + getString(R.string.instructions_text), fullTextInstructions), R.drawable.directions_walk_24px);

        // Definit l'image
        Uri uri = Uri.parse(imagePath);

        Glide.with(this)
                .load(uri)
                .centerCrop()
                .error(R.drawable.explore_24px) // optional: show error if loading fails
                .into(this.imageCardView);

        ImageView like = (ImageView)findViewById(R.id.image_like);

        likeRepository.checkLike(getIntent().getStringExtra("POST_ID"), auth.getActiveUserId(), isLikedByUser -> {
            if (isLikedByUser) {
                like.setImageResource(R.drawable.favorite_filled_24px);
            }
            else {
                like.setImageResource(R.drawable.favorite_24px);
            }
        });

        like.setOnClickListener(v -> {
            String userId = auth.getActiveUserId();
            String postId = getIntent().getStringExtra("POST_ID");

            String id = likeRepository.putItem();

            if (isLikedByUser) {
                // remove like

                likeRepository.removeLikes(postId, userId, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postRepository.decrementLikes(postId);

                        isLikedByUser = !isLikedByUser;
                        count_likes--;

                        likesCountView.setText(String.valueOf(count_likes));

                        Toast.makeText(getApplicationContext(), "You removed your like!", Toast.LENGTH_LONG).show();

                        like.setImageResource(R.drawable.favorite_24px);
                    }
                });
            }
            else {
                // add like
                likeRepository.putItem(new Like(id, userId, postId, Timestamp.now()), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            postRepository.incrementLikes(postId);

                            like.setImageResource(R.drawable.favorite_filled_24px);

                            isLikedByUser = !isLikedByUser;
                            count_likes++;
                            likesCountView.setText(String.valueOf(count_likes));

                            Toast.makeText(getApplicationContext(), "You liked this post!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        double lat = getIntent().getDoubleExtra("LATITUDE", 0);
        double lon = getIntent().getDoubleExtra("LONGITUDE", 0);

        if (lat != 0 && lon != 0) {
            loadNearbyActivities(lat, lon);
        }
    }

    private void loadNearbyActivities(double lat, double lon) {
        TravelPath_ActivityRepository activityRepo = new TravelPath_ActivityRepository(this);
        activityRepo.getNearbyActivities(lat, lon, 5000, activities -> {
            // 'activities' contient maintenant les TravelPath_Activity à moins de 5km
            updateCarousel(activities);
        });
    }

    private void updateCarousel(List<TravelPath_Activity> activities) {
        RecyclerView recyclerView = findViewById(R.id.recycler_nearby_activities);
        TextView titleView = findViewById(R.id.title_nearby_activities);

        if (activities.isEmpty()) {
            titleView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        titleView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        // Utilisation d'un adaptateur anonyme pour plus de rapidité
        // (Ou créez une classe NearbyAdapter séparée)
        NearbyActivitiesAdapter adapter = new NearbyActivitiesAdapter(activities, activity -> {
            // Action au clic : Ouvrir Google Maps
            double aLat = activity.getLocalisation().get(0);
            double aLon = activity.getLocalisation().get(1);
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + aLat + "," + aLon);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void setIconTextView(TextView textView, String fullText, int imageResId) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(fullText);

        Drawable drawable = ContextCompat.getDrawable(this, imageResId);
        int iconSize = (int) (textView.getTextSize() * 1.2);
        drawable.setBounds(0, 0, iconSize, iconSize);

        drawable.setTint(getColor(R.color.black));

        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        ssb.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(ssb);
    }

    @Override
    public void onRequestClose() {
        finish();
    }
}