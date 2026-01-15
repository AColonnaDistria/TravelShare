package com.travel.travelshare.ui.groups;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.travelshare.Auth;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.model.user.UserGroup;
import com.travel.travelshare.repositories.PostRepository;
import com.travel.travelshare.repositories.UserGroupRepository;

import java.util.ArrayList;
import java.util.List;

public class GroupViewModel extends ViewModel {
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    protected MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    protected MutableLiveData<String> lastId = new MutableLiveData<>();
    protected MutableLiveData<ArrayList<UserGroup>> groups = new MutableLiveData<>(new ArrayList<>());
    protected UserGroupRepository userGroupRepository;

    protected Auth auth;

    public GroupViewModel(Auth auth) {
        this.userGroupRepository = new UserGroupRepository();

        this.setIsLoading(false);
        this.setIsLastPage(false);
        this.setLastId(null);

        this.auth = auth;

        this.auth.observeAuthState(user -> {
            if (user != null) {
                loadGroupIsMemberFrom(user.getId());
            }
        });
    }

    public MutableLiveData<ArrayList<UserGroup>> getGroups() {
        return this.groups;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return this.isLoading;
    }

    public void setIsLoading(boolean value) {
        this.isLoading.setValue(value);
    }

    public MutableLiveData<Boolean> getIsLastPage() {
        return this.isLastPage;
    }

    public void setIsLastPage(boolean value) {
        this.isLastPage.setValue(value);
    }

    public MutableLiveData<String> getLastId() {
        return this.lastId;
    }

    public void setLastId(String value) {
        this.lastId.setValue(value);
    }

    public void loadGroupIsMemberFrom(String userId) {
        if (userId == null) return;
        this.userGroupRepository.getAllFromMember(userId, newGroups -> {
            ArrayList<UserGroup> groups = this.groups.getValue();
            groups.addAll(newGroups);

            this.groups.postValue(groups);
        });
    }
}