package com.travel.travelshare;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.model.user.ConnectedUser;
import com.travel.travelshare.model.user.GuestUser;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.UserRepository;

import java.util.function.Consumer;

public class Auth {
    private final FirebaseAuth mAuth;
    private final UserRepository userRepository;
    private User activeUser;
    private FirebaseUser firebaseUser;

    public Auth() {
        this.mAuth = FirebaseAuth.getInstance();
        this.userRepository = new UserRepository();

        this.firebaseUser = this.mAuth.getCurrentUser();
        if (this.firebaseUser == null) {
            this.mAuth.signInAnonymously();
        }

        this.userRepository.getItemByFirebaseUid(this.mAuth.getUid(), user -> {
           this.activeUser = user;
        });
    }

    public interface AuthReloadCallback {
        void onReload(User activeUser);
    }

    public interface AuthLoginCallback {
        void onSuccess(User activeUser);
        void onFailure();
    }

    public interface AuthRegisterCallback {
        void onSuccess(User activeUser);
        void onFailure();
    }

    public void reload(AuthReloadCallback callback) {
        this.firebaseUser = this.mAuth.getCurrentUser();
        if (this.firebaseUser == null) {
            this.signInAnonymously(new AuthLoginCallback() {
                @Override
                public void onSuccess(User activeUser) {
                    callback.onReload(activeUser);
                }

                @Override
                public void onFailure() {}
            });
        }
        else {
            this.firebaseUser.reload().addOnCompleteListener(task -> {
                userRepository.getItemByFirebaseUid(this.firebaseUser.getUid(), user -> {
                    if (user != null) {
                        activeUser = user;
                    }
                    else {
                        activeUser = new GuestUser(firebaseUser.getUid(), Timestamp.now());
                        userRepository.putItem(activeUser);
                    }

                    callback.onReload(activeUser);
                });
            });
        }
    }

    public void logout() {
        this.mAuth.signOut();
    }

    public void signInAnonymously(AuthLoginCallback callback) {
        this.mAuth.signInAnonymously().addOnSuccessListener(task -> {
            this.reload(callback::onSuccess);
        }).addOnFailureListener(task -> {
            callback.onFailure();
        });
    }

    public void signInWithEmailAndPassword(String email, String password, AuthLoginCallback callback) {
        this.mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(task -> {
            this.reload(callback::onSuccess);
        }).addOnFailureListener(task -> {
            callback.onFailure();
        });
    }

    public void createConnectedUser(ConnectedUser user, String password, AuthRegisterCallback callback) {
        this.mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnSuccessListener(task -> {
            FirebaseUser fbUser = task.getUser();

            if (fbUser != null) {
                // Verification email
                fbUser.sendEmailVerification().addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful()) {
                        user.setFirebaseUid(fbUser.getUid());
                        this.userRepository.putItem(user);
                        this.reload(callback::onSuccess);
                    } else {
                        callback.onFailure();
                    }
                });
            }
        }).addOnFailureListener(task -> {
            callback.onFailure();
        });
    }

    public String getActiveUserId() {
        if (this.activeUser != null) {
            return this.activeUser.getId();
        }
        return null;
    }

    public String getActiveUserAuthId() {
        if (this.activeUser != null) {
            return this.activeUser.getFirebaseUid();
        }
        return null;
    }

    public boolean isActiveUserAnonymous() {
        if (this.firebaseUser != null) {
            return this.firebaseUser.isAnonymous();
        }
        return false;
    }

    public boolean isActiveUserEmailVerified() {
        if (this.firebaseUser != null) {
            return this.firebaseUser.isEmailVerified();
        }
        return false;
    }

    public void observeAuthState(Consumer<User> observer) {
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser fbUser = firebaseAuth.getCurrentUser();

            if (fbUser != null) {
                userRepository.getItemByFirebaseUid(
                        fbUser.getUid(),
                        observer::accept
                );
            }
        });
    }
}
