package com.travel.travelshare.ui.auth;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentLoginBinding;
import com.travel.travelshare.databinding.FragmentRegisterBinding;
import com.travel.travelshare.model.user.ConnectedUser;
import com.travel.travelshare.repositories.UserRepository;

import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private FirebaseAuth mAuth;
    private UserRepository userRepo;
    private FragmentRegisterBinding binding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = com.travel.travelshare.databinding.FragmentRegisterBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        userRepo = new UserRepository();

        binding.btnRegister.setOnClickListener(v -> registerUser());

        binding.txtLoginLink.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_register_to_login);
        });

        return binding.getRoot();
    }

    private void registerUser() {
        String email = binding.editRegisterEmail.getText().toString().trim();
        String password = binding.editRegisterPassword.getText().toString().trim();
        String username = binding.editUsername.getText().toString().trim();

        // Validation des champs
        if (username.isEmpty()) {
            binding.layoutUsername.setError("Username required");
            return;
        }
        if (email.isEmpty()) {
            binding.layoutRegisterEmail.setError("Email required");
            return;
        }
        if (password.isEmpty()) {
            binding.layoutRegisterPassword.setError("Password required");
            return;
        }

        // Création du compte dans Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        if (fbUser != null) {
                            saveUserToFirestore(fbUser, username, email);
                        }

                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(verifyTask -> {
                                    if (verifyTask.isSuccessful()) {
                                        Toast.makeText(getContext(), "Please verify your email to continue.", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Failed to send verification email: " + verifyTask.getException(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Signup error : " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser fbUser, String username, String email) {
        // Création de l'objet ConnectedUser selon votre modèle
        ConnectedUser newUser = new ConnectedUser(
                username,
                mAuth.getUid(),
                email,
                Timestamp.now()
        );

        // Enregistrement dans la collection travelshare_users
        userRepo.putItem(newUser);

        Toast.makeText(getContext(), "Compte créé avec succès !", Toast.LENGTH_SHORT).show();

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}