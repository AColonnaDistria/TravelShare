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

import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class PublishFragment extends Fragment {
    private static final int CAMERA_REQUEST = 100;
    private static final int PICK_IMAGE_REQUEST = 101;

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

                if (mViewModel.getPhotoURI().getValue() != null) {
                    // Remove tint
                    binding.publishImageCardview.setImageTintList(null);

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

        if (mViewModel.getPhotoURI().getValue() != null) {

            // 1. REMOVE THE GRAY TINT
            binding.publishImageCardview.setImageTintList(null);

            // 2. Load the image
            Glide.with(this)
                    .load(mViewModel.getPhotoURI().getValue())
                    .centerCrop()
                    .into(binding.publishImageCardview);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}