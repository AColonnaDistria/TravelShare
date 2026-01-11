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
import com.travel.travelshare.Auth;
import com.travel.travelshare.MainActivity;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentLoginBinding;
import com.travel.travelshare.databinding.FragmentRegisterBinding;
import com.travel.travelshare.model.user.ConnectedUser;
import com.travel.travelshare.model.user.User;
import com.travel.travelshare.repositories.UserRepository;

import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {
    private RegisterViewModel mViewModel;
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModelFactory factory = new RegisterViewModelFactory(new Auth());
        this.mViewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.btnRegister.setOnClickListener(v -> registerUser());

        binding.txtLoginLink.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_register_to_login);
        });

        return binding.getRoot();
    }

    private void registerUser() {
        String username = binding.editUsername.getText().toString().trim();
        String email = binding.editRegisterEmail.getText().toString().trim();
        String password = binding.editRegisterPassword.getText().toString().trim();

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

        ConnectedUser user = new ConnectedUser(username, null, email, Timestamp.now());
        this.mViewModel.registerUser(user, password, new Auth.AuthRegisterCallback() {
            @Override
            public void onSuccess(User activeUser) {
                Toast.makeText(getContext(), "Successful sign in. Please verify your email.", Toast.LENGTH_SHORT).show();

                if (getActivity() != null) {
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Sign in error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}