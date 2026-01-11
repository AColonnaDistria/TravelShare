package com.travel.travelshare.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.travel.travelshare.Auth;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private final Auth auth;

    public RegisterViewModelFactory(Auth auth) {
        this.auth = auth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(auth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}