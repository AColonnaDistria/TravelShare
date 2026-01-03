package com.travel.travelshare.repositories;
import com.travel.travelshare.model.user.User;

public class UserRepository extends SimpleRepository<User> {
    public UserRepository() {
        super(User.class, "travelshare_users");
    }
}