package com.travel.travelshare.ui.groups;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.travel.travelshare.Auth;

public class GroupViewModelFactory implements ViewModelProvider.Factory {
    private final Auth auth;

    public GroupViewModelFactory(Auth auth) {
        this.auth = auth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GroupViewModel.class)) {
            return (T) new GroupViewModel(auth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
