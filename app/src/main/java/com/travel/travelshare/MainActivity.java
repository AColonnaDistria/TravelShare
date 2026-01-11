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

import java.util.List;

import org.osmdroid.config.Configuration;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private TextView pageTitle;
    private ImageView profileButton;
    private ImageView notificationButton;
    private NavController navController;

    private FirebaseAuth mAuth;
    private User activeUser; // Votre classe modèle
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // INIT Storage
        Storage.init(this);

        // INIT OSM-DROID
        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue(getPackageName());

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
        this.userRepository = new UserRepository();

        this.mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            this.userRepository.getItemByFirebaseUid(mAuth.getCurrentUser().getUid(), new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user == null) {
                        signInAnonymously();
                    }
                    else {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(MainActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                        }
                        else if (!mAuth.getCurrentUser().isAnonymous()) {
                            // Send a verification email
                            mAuth.getCurrentUser().reload().addOnCompleteListener(task -> {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(MainActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                                    changePublishItemVisibility();
                                } else {
                                    mAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(verifyTask -> {
                                        if (verifyTask.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Please verify your email to continue.", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "Failed to send verification email: " + verifyTask.getException(), Toast.LENGTH_LONG).show();
                                        }

                                        changePublishItemVisibility();

                                    });
                                }
                            });
                        }

                        activeUser = user;
                    }
                }
            });
        }
        else {
            signInAnonymously();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) return;

        fbUser.reload().addOnCompleteListener(task -> {
            userRepository.getItemByFirebaseUid(fbUser.getUid(), user -> {
                if (user != null) {
                    activeUser = user;
                }
                changePublishItemVisibility();
            });
        });
    }

    private void changePublishItemVisibility() {
        Menu navMenu = binding.navView.getMenu();
        MenuItem publishItem = navMenu.findItem(R.id.navigation_publish);

        if (activeUser.getUserType() == UserType.GUEST || !isEmailVerifiedUser()) {
            // Hide the publish tab
            publishItem.setVisible(false);
        } else {
            publishItem.setVisible(true);
        }
    }

    private boolean isEmailVerifiedUser() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        return fbUser != null && !fbUser.isAnonymous() && fbUser.isEmailVerified();
    }

    private void signInAnonymously() {
        this.mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                this.userRepository.getItemByFirebaseUid(firebaseUser.getUid(), new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user == null) {
                            activeUser = new GuestUser(firebaseUser.getUid(), Timestamp.now());
                            userRepository.putItem(activeUser);
                        }
                        else {
                            activeUser = user;
                        }

                        changePublishItemVisibility();
                    }
                });
            }
            else {
                Log.v("[UNAUTHORIZED]", "Anonymous sign in did not work");
            }
        });
    }

    private void logout() {
        this.mAuth.signOut();
        activeUser = null;

        signInAnonymously();
    }

    private void showProfileMenu(View anchorView) {
        PopupMenu popup = new PopupMenu(this, anchorView);

        if (activeUser.getUserType() == UserType.GUEST) {
            popup.getMenuInflater().inflate(R.menu.guest_profile_menu, popup.getMenu());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true);
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.action_likes) {
                        // Handle Likes
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
                }
            });

            popup.show();
        }
        else {
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
                    else if (id == R.id.action_log_out) {
                        // Handle Logout logic
                        logout();
                        return true;
                    }
                    return false;
                }
            });

            popup.show();
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

    public void openCardView(String imagePath, int position) {
        Intent intent = new Intent(MainActivity.this, CardViewActivity.class);

        intent.putExtra("IMAGE_PATH", imagePath);
        intent.putExtra("POSITION", position);

        startActivity(intent);
    }
}