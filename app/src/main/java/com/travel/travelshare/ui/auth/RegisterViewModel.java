package com.travel.travelshare.ui.auth;

import androidx.lifecycle.ViewModel;

import com.travel.travelshare.Auth;
import com.travel.travelshare.model.user.ConnectedUser;
import com.travel.travelshare.repositories.UserRepository;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();
    private final Auth auth;

    public RegisterViewModel(Auth auth) {
        this.auth = auth;
    }

    public void registerUser(ConnectedUser user, String password, Auth.AuthRegisterCallback callback) {
        this.auth.createConnectedUser(user, password, callback);
    }
}