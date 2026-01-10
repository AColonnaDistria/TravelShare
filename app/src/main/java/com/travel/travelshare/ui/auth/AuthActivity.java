package com.travel.travelshare.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.travel.travelshare.R;
import com.travel.travelshare.ui.elements.ReturnBarFragment;

public class AuthActivity extends AppCompatActivity implements ReturnBarFragment.OnCloseRequestedListener {
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        this.navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_auth);
        this.navController = navHostFragment.getNavController();

        if (navHostFragment != null) {
            String destination = getIntent().getStringExtra("START_DESTINATION");
            if (destination != null) {
                if (destination.equals("login")) {
                    navController.navigate(R.id.loginFragment);
                }
                else if (destination.equals("register")) {
                    navController.navigate(R.id.registerFragment);
                }
            }
        }
    }

    @Override
    public void onRequestClose() {
        finish();
    }
}