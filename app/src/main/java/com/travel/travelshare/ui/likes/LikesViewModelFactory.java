package com.travel.travelshare.ui.likes;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.travel.travelshare.Auth;
import com.travel.travelshare.ui.auth.RegisterViewModel;

public class LikesViewModelFactory implements ViewModelProvider.Factory {
    private final Auth auth;

    public LikesViewModelFactory(Auth auth) {
        this.auth = auth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LikesViewModel.class)) {
            return (T) new LikesViewModel(auth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}