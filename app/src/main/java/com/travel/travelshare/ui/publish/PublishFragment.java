package com.travel.travelshare.ui.publish;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.travel.travelshare.ChipFilterViewModel;
import com.travel.travelshare.databinding.FragmentPublishBinding;
import com.travel.travelshare.model.ImagePublication;
import com.travel.travelshare.storage.Storage;

import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PublishFragment extends Fragment {
    private Storage storage;

    private FragmentPublishBinding binding;

    private ImageView publishPhotoView;
    private MaterialButton takePhotoButton;
    private MaterialButton uploadButton;

    private TextInputEditText dateEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText instructionsEditText;
    private TextInputEditText locationEditText;

    private Button publishButton;
    private MaterialButton visibilityPublicToggleButton;
    private MaterialButton visibilityPrivateToggleButton;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest> pickMediaLauncher;
    private PublishViewModel mViewModel;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.storage = new Storage();

        this.mViewModel = new ViewModelProvider(this).get(PublishViewModel.class);

        this.takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) {
                // SUCCESS! The image is already saved at 'photoUri'
                // You don't get it from 'success', you use the variable you created earlier.

                Log.v("LOG", "CAMERA_SUCCESS");
                Log.v("LOG", this.mViewModel.getPhotoURI().getValue().toString());

                if (mViewModel.getPhotoURI().getValue() != null) {
                    // Remove tint
                    binding.publishImageCardview.setImageTintList(null);

                    this.imageUri = PublishFragment.this.mViewModel.getPhotoURI().getValue();

                    Glide.with(this)
                            .load(this.mViewModel.getPhotoURI().getValue()) // <--- Use the field here
                            .into(binding.publishImageCardview);
                }

            } else {
                Log.v("LOG", "CAMERA_FAILED");
                // User cancelled or camera failed
            }
        });


        this.pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.v("LOG", "GALLERY_SUCCESS");
                Log.v("LOG", this.mViewModel.getPhotoURI().getValue().toString());
                mViewModel.setPhotoURI(uri);
                // Remove tint
                binding.publishImageCardview.setImageTintList(null);

                this.imageUri = PublishFragment.this.mViewModel.getPhotoURI().getValue();

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

        this.publishPhotoView = binding.publishImageCardview;
        this.takePhotoButton = binding.publishTakePhotoButton;
        this.uploadButton = binding.publishUploadButton;

        this.dateEditText = binding.publishEditDate;
        this.descriptionEditText = binding.publishEditDescription;
        this.instructionsEditText = binding.publishEditInstructions;
        this.locationEditText = binding.publishEditLocation;

        this.publishButton = binding.publishButton;

        this.visibilityPublicToggleButton = binding.publicToggleButton;
        this.visibilityPrivateToggleButton = binding.privateToggleButton;

        this.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setPhotoURI(createPhotoUri());
                PublishFragment.this.pickMediaLauncher.launch(new androidx.activity.result.PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        this.takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setPhotoURI(createPhotoUri());
                PublishFragment.this.takePictureLauncher.launch(PublishFragment.this.mViewModel.getPhotoURI().getValue());
            }
        });

        this.visibilityPublicToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setVisibility(true); // set to PUBLIC
            }
        });

        this.visibilityPrivateToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.mViewModel.setVisibility(false); // set to PUBLIC
            }
        });

        this.publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.saveImagePublication();
            }
        });

        if (this.mViewModel.getPhotoURI().getValue() != null) {
            this.imageUri = this.mViewModel.getPhotoURI().getValue();

            // 1. REMOVE THE GRAY TINT
            binding.publishImageCardview.setImageTintList(null);

            // 2. Load the image
            Glide.with(this)
                    .load(mViewModel.getPhotoURI().getValue())
                    .centerCrop()
                    .into(binding.publishImageCardview);
        }

        if (this.mViewModel.getVisibility().getValue() != null) {
            boolean is_public = this.mViewModel.getVisibility().getValue();

            this.visibilityPublicToggleButton.setChecked(is_public);
            this.visibilityPrivateToggleButton.setChecked(!is_public);
        }

        if (this.mViewModel.getDate().getValue() != null) {
            String dateAsString = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.mViewModel.getDate().getValue());

            this.dateEditText.setText(dateAsString);
        }

        if (this.mViewModel.getDescription().getValue() != null) {
            this.descriptionEditText.setText(this.mViewModel.getDescription().getValue());
        }

        if (this.mViewModel.getLocation().getValue() != null) {
            this.locationEditText.setText(this.mViewModel.getLocation().getValue());
        }

        return root;
    }

    private Uri createPhotoUri() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = requireContext().getExternalCacheDir();

            File imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Convert File to Uri using FileProvider
            return FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    imageFile
            );

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveImagePublication() {
        Uri imageUri = this.imageUri;

        boolean visibility = this.visibilityPublicToggleButton.isChecked();
        LocalDateTime date;
        try {
            date = LocalDate.parse(
                    this.dateEditText.getText(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            ).atStartOfDay();
        }
        catch (Exception e) {
            // defaults to zero
            date = LocalDate.parse(
                    "01/01/1970",
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            ).atStartOfDay();
        }
        String description = this.descriptionEditText.getText().toString();
        String instructions = this.instructionsEditText.getText().toString();
        String location = this.locationEditText.getText().toString();

        this.storage.saveImagePublication(imageUri, visibility, date, description, instructions, location);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}