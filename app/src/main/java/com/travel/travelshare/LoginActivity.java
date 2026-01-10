package com.travel.travelshare;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travelshare.databinding.ActivityLoginBinding;
import com.travel.travelshare.model.user.UserType;
import com.travel.travelshare.repositories.UserRepository;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        userRepo = new UserRepository();
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnLogin.setOnClickListener(v -> loginUser());

        binding.txtRegisterLink.setOnClickListener(v -> {
            // Intent intent = new Intent(this, RegisterActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Redirection vers l'inscription...", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString().trim();

        // Validations minimales
        if (TextUtils.isEmpty(email)) {
            binding.layoutEmail.setError("L'email est requis");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.layoutPassword.setError("Le mot de passe est requis");
            return;
        }

        // Authentification Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        if (fbUser != null) {
                            updateUserSession(fbUser);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Erreur d'authentification",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateUserSession(FirebaseUser fbUser) {
        // On récupère les données pour vérifier si l'utilisateur doit être mis à jour
        userRepo.getItem(fbUser.getUid(), user -> {
            if (user == null || user.getUserType() != UserType.CONNECTED) {
                Toast.makeText(LoginActivity.this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                // On ferme l'activité pour revenir à la MainActivity qui se rafraîchira
                finish();
            }
        });
    }
}