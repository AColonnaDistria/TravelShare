package com.travel.travelshare;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.Storage;
import com.travel.travelshare.repositories.UserRepository;

import org.osmdroid.config.Configuration;

public class MainViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();
    private final Auth auth;

    private final MutableLiveData<Integer> pageTitle = new MutableLiveData<>();
    private final MutableLiveData<User> activeUser = new MutableLiveData<>();

    public MainViewModel(Context context, Auth auth) {
        this.auth = auth;

        this.auth.observeAuthState(user -> {
            this.reloadUser(null);
        });

        Storage.init(context);

        // INIT OSM-DROID
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue(context.getPackageName());

        this.auth.reload(this.activeUser::setValue);
        this.setPageTitle(R.string.title_map);
    }

    public void reloadUser(Auth.AuthReloadCallback callback) {
        this.auth.reload(user -> {
            activeUser.setValue(user);
            if (callback != null) {
                callback.onReload(user);
            }
        });
    }

    public void logout() {
        this.auth.logout();

        this.reloadUser(null);
    }

    public Auth getAuth() {
        return this.auth;
    }

    public LiveData<User> getCurrentUser() {
        return this.activeUser;
    }

    public LiveData<Integer>  getPageTitle() {
        return this.pageTitle;
    }

    public void setPageTitle(Integer res) {
        this.pageTitle.setValue(res);
    }
}
