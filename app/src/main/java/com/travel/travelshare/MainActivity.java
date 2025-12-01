package com.travel.travelshare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.travel.travelshare.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private TextView pageTitle;
    private ImageView profileButton;
    private ImageView notificationButton;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_discover, R.id.navigation_publish)
                .build();
        this.navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        this.pageTitle = findViewById(R.id.page_title);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destinationId = destination.getId();

            if (destinationId == R.id.navigation_map) {
                this.pageTitle.setText(R.string.title_map);
            }
            else if (destinationId == R.id.navigation_discover) {
                this.pageTitle.setText(R.string.title_discover);
            }
            else if (destinationId == R.id.navigation_publish) {
                this.pageTitle.setText(R.string.title_publish);
            }
        });

        this.profileButton = findViewById(R.id.profile);

        this.profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showProfileMenu(v);
            }
        });

        this.notificationButton = findViewById(R.id.notifications);

        this.notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showNotificationMenu(v);
            }
        });
    }

    private void showProfileMenu(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);

        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_profile) {
                    // Handle Open Profile
                    return true;
                }
                else if (id == R.id.action_groups) {
                    // Handle Groups logic
                    return true;
                }
                else if (id == R.id.action_likes) {
                    // Handle Likes logic
                    return true;
                }
                return false;
            }
        });

        popup.show();
    }

    private void showNotificationMenu(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);
        Menu menu = popup.getMenu();

        List<String> notifications = List.of(
                "Alex2003 has published a new photo",
                "New photo added at Lake Como",
                "New post found matching Hiking",
                "Julia liked your photo at Villa Monastero",
                "You have been invited to join 'Backpackers Europe'");

        int count = 1;
        for (String notification : notifications) {
            menu.add(Menu.NONE, count, count, notification);
            count++;
        }

        menu.add(Menu.NONE, count, count, "See more...");
        count++;

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        popup.show();
    }

    public void openCardView(String imagePath, int position) {
        Intent intent = new Intent(MainActivity.this, CardViewActivity.class);

        intent.putExtra("IMAGE_PATH", imagePath);
        intent.putExtra("POSITION", position);

        startActivity(intent);
    }
}