package com.travel.travelshare.ui.auth;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation; // Important pour la navigation
import androidx.navigation.internal.Log;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.Auth;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.MainViewModel;
import com.travel.travelshare.MainViewModelFactory;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentLoginBinding;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.model.user.UserType;
import com.travel.travelshare.repositories.UserRepository;

public class LoginFragment extends Fragment {
    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModelFactory factory = new LoginViewModelFactory(new Auth());
        this.mViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.btnLogin.setOnClickListener(v -> loginUser());
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

        this.mViewModel.loginUser(email, password, new Auth.AuthLoginCallback() {
            @Override
            public void onSuccess(User activeUser) {
                Toast.makeText(getContext(), "Successful connection", Toast.LENGTH_SHORT).show();

                if (getActivity() != null) {
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Authentification error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}