package com.travel.travelshare.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.travel.travelshare.Auth;

public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final Auth auth;

    public LoginViewModelFactory(Auth auth) {
        this.auth = auth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(auth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
