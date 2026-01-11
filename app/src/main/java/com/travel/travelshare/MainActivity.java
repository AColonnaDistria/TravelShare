package com.travel.travelshare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.ui.auth.AuthActivity;
import com.travel.travelshare.databinding.ActivityMainBinding;
import com.travel.travelshare.model.user.GuestUser;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.model.user.UserType;
import com.travel.travelshare.repositories.Storage;
import com.travel.travelshare.repositories.UserRepository;
import com.travel.travelshare.ui.cardview.CardViewActivity;
import com.travel.travelshare.ui.likes.LikesActivity;
import com.travel.travelshare.ui.likes.LikesViewModel;

import java.util.List;

import org.osmdroid.config.Configuration;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mViewModel;

    /*
    private TextView pageTitle;
    private ImageView profileButton;
    private ImageView notificationButton;
    private NavController navController;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Auth auth = Auth.getInstance();
        auth.init();

        MainViewModelFactory factory = new MainViewModelFactory(this, auth);
        this.mViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_discover, R.id.navigation_publish)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destinationId = destination.getId();

            if (destinationId == R.id.navigation_map)
                this.mViewModel.setPageTitle(R.string.title_map);
            else if (destinationId == R.id.navigation_discover)
                this.mViewModel.setPageTitle(R.string.title_discover);
            else if (destinationId == R.id.navigation_publish)
                this.mViewModel.setPageTitle(R.string.title_publish);
        });

        binding.profile.setOnClickListener(this::showProfileMenu);
        binding.notifications.setOnClickListener(this::showNotificationMenu);

        this.mViewModel.getPageTitle().observe(this, page_title -> {
            binding.pageTitle.setText(page_title);
        });

        // Handle user logic
        this.mViewModel.getCurrentUser().observe(this, user -> {
            Menu navMenu = binding.navView.getMenu();
            MenuItem publishItem = navMenu.findItem(R.id.navigation_publish);

            publishItem.setVisible((user.getUserType() == UserType.CONNECTED));
        });
    }

    public void reloadUser() {
        this.mViewModel.reloadUser(null);
    }

    private void showProfileMenuGuest(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);

        popup.getMenuInflater().inflate(R.menu.guest_profile_menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_likes) {
                // Handle Likes

                Intent intent = new Intent(MainActivity.this, LikesActivity.class);

                startActivity(intent);

                return true;
            }
            else if (id == R.id.action_signup) {
                // Handle Signup logic

                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                intent.putExtra("START_DESTINATION", "register");

                startActivity(intent);

                return true;
            }
            else if (id == R.id.action_login) {
                // Handle Login logic

                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                intent.putExtra("START_DESTINATION", "login");

                startActivity(intent);

                return true;
            }
            return false;
        });

        popup.show();
    }

    private void showProfileMenuConnected(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);

        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }

        popup.setOnMenuItemClickListener(item -> {
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

                Intent intent = new Intent(MainActivity.this, LikesActivity.class);

                startActivity(intent);

                return true;
            }
            else if (id == R.id.action_log_out) {
                // Handle Logout logic
                mViewModel.logout();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void showProfileMenu(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);

        if (this.mViewModel.getAuth().isActiveUserAnonymous()) {
            this.showProfileMenuGuest(anchorView);
        }
        else {
            this.showProfileMenuConnected(anchorView);
        }
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
}