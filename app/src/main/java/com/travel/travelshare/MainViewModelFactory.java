package com.travel.travelshare;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private final Auth auth;

    public MainViewModelFactory(Context context, Auth auth) {
        this.context = context;
        this.auth = auth;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(context, auth);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
