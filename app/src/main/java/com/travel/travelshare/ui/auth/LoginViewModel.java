package com.travel.travelshare.ui.auth;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.Auth;
import com.travel.travelshare.R;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.model.user.UserType;
import com.travel.travelshare.repositories.Storage;
import com.travel.travelshare.repositories.UserRepository;

import org.osmdroid.config.Configuration;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();
    private final Auth auth;

    public LoginViewModel(Auth auth) {
        this.auth = auth;
    }

    public void loginUser(String email, String password, Auth.AuthLoginCallback callback) {
        this.auth.signInWithEmailAndPassword(email, password, callback);
    }
}