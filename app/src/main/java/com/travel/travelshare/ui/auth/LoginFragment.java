package com.travel.travelshare.ui.auth;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation; // Important pour la navigation

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentLoginBinding;
import com.travel.travelshare.model.user.UserType;
import com.travel.travelshare.repositories.UserRepository;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        userRepo = new UserRepository();

        // Gestion du bouton de connexion
        binding.btnLogin.setOnClickListener(v -> loginUser());

        // Navigation vers le fragment d'inscription
        binding.txtRegisterLink.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_login_to_register);
        });

        return binding.getRoot();
    }

    private void loginUser() {
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.layoutEmail.setError("Email required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.layoutPassword.setError("Password required");
            return;
        }

        // Utilisation de getActivity() pour le contexte du Toast
        // Utilisation de l'exécuteur par défaut ou de la signature simplifiée
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        if (fbUser != null) {
                            updateUserSession(fbUser);
                        }
                    } else {
                        Toast.makeText(getContext(), "Authentification error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateUserSession(FirebaseUser fbUser) {
        userRepo.getItemByFirebaseUid(fbUser.getUid(), user -> {
            if (user == null || user.getUserType() != UserType.CONNECTED) {
                Toast.makeText(getContext(), "Error: Profile not found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Successful connection", Toast.LENGTH_SHORT).show();

                if (getActivity() != null) {
                    if (getActivity().getClass() == MainActivity.class) {
                        ((MainActivity)(getActivity())).reloadUser();
                    }
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}