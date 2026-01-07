package com.travel.travelshare.ui.publish;

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.travel.travelshare.databinding.FragmentPublishBinding;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.post.PicturePost;
import com.travel.travelshare.repositories.PostRepository;

import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PublishFragment extends Fragment {
    private FragmentPublishBinding binding;

    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest> pickMediaLauncher;

    private PublishViewModel mViewModel;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mViewModel = new ViewModelProvider(this).get(PublishViewModel.class);

        this.takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) {
                // SUCCESS! The image is already saved at 'photoUri'
                // You don't get it from 'success', you use the variable you created earlier.

                Log.v("LOG", "CAMERA_SUCCESS");
                Log.v("LOG", this.mViewModel.getPhotoURI().getValue().toString());
            } else {
                Log.v("LOG", "CAMERA_FAILED");
                // User cancelled or camera failed
            }
        });

        this.pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                mViewModel.setPhotoURI(uri);
                // Remove tint
                binding.publishImageCardview.setImageTintList(null);
                Glide.with(this)
                        .load(this.mViewModel.getPhotoURI().getValue()) // <--- Use the field here
                        .into(binding.publishImageCardview);
            } else {
                Log.v("LOG", "GALLERY_FAILED");
                // User cancelled or gallery failed
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublishBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.publishEditDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                if (text.length() != 10) return; // dd/MM/yyyy = 10 chars

                try {
                    DateTimeFormatter DATE_FORMATTER;
                    LocalDate date = LocalDate.parse(text, PublishFragment.DATE_FORMATTER);
                    mViewModel.setDate(date.atStartOfDay());
                } catch (Exception e) {
                    // Invalid date -> ignore
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        binding.publishEditDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setDescription(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        binding.publishEditInstructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setInstructions(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        binding.publishEditLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setLocation(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        binding.publishUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setPhotoURI(PublishFragment.this.mViewModel.createPhotoUri(getContext()));
                PublishFragment.this.pickMediaLauncher.launch(new androidx.activity.result.PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        binding.publishTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setPhotoURI(PublishFragment.this.mViewModel.createPhotoUri(getContext()));
                PublishFragment.this.takePictureLauncher.launch(PublishFragment.this.mViewModel.getPhotoURI().getValue());
            }
        });

        binding.publicToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setVisibility(true); // set to PUBLIC
            }
        });

        binding.privateToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setVisibility(false); // set to PUBLIC
            }
        });

        binding.publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save image publication
                PublishFragment.this.mViewModel.saveImagePublication();
            }
        });

        this.mViewModel.getPhotoURI().observe(this.getViewLifecycleOwner(), uri -> {
            binding.publishImageCardview.setImageTintList(null);

            Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.publishImageCardview);
        });

        this.mViewModel.getVisibility().observe(this.getViewLifecycleOwner(), is_public -> {
            if (is_public != binding.publicToggleButton.isChecked()) {
                binding.publicToggleButton.setChecked(is_public);
            }
            if (!is_public != binding.privateToggleButton.isChecked()) {
                binding.privateToggleButton.setChecked(!is_public);
            }
        });

        /*
        this.mViewModel.getDate().observe(this.getViewLifecycleOwner(), date -> {
            String dateAsString = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);

            if (!dateAsString.equals(binding.publishEditDate.getText().toString())) {
                binding.publishEditDate.setText(dateAsString);
            }
        });

         */

        this.mViewModel.getDescription().observe(this.getViewLifecycleOwner(), description -> {
            if (!description.equals(binding.publishEditDescription.getText().toString())) {
                binding.publishEditDescription.setText(description);
            }
        });

        this.mViewModel.getLocation().observe(this.getViewLifecycleOwner(), location -> {
            if (!location.equals(binding.publishEditLocation.getText().toString())) {
                binding.publishEditLocation.setText(this.mViewModel.getLocation().getValue());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}