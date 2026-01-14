package com.travel.travelshare.ui.publish;

import java.text.SimpleDateFormat;

import java.time.Instant;
import java.util.TimeZone;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.travel.travelshare.LocationPickerDialog;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentPublishBinding;
import com.travel.travelshare.model.location.ApproximateLocation;
import com.travel.travelshare.model.location.ExactLocation;
import com.travel.travelshare.model.location.Location;
import com.travel.travelshare.model.location.LocationType;
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
                this.mViewModel.setPhotoURI(null);
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

    public String getFormattedDMS(double coordinate, boolean isLatitude) {
        double absolute = Math.abs(coordinate);
        int degrees = (int) absolute;

        double minutesNotTruncated = (absolute - degrees) * 60;
        int minutes = (int) minutesNotTruncated;

        double seconds = (minutesNotTruncated - minutes) * 60;

        String direction;
        if (isLatitude) {
            direction = coordinate >= 0 ? "N" : "S";
        } else {
            direction = coordinate >= 0 ? "E" : "W";
        }

        // Change %.6f to %.2f to round to two decimal places
        return String.format("%d°%d'%.2f''%s", degrees, minutes, seconds, direction);
    }

    public String formatToDMS(double latitude, double longitude) {
        return getFormattedDMS(latitude, true) + " " + getFormattedDMS(longitude, false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublishBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.publishEditDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                this.mViewModel.setDate(new Date(selection));
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
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

        binding.publishUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment.this.pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        binding.publishTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri cameraUri = PublishFragment.this.mViewModel.createTempUri(getContext());
                if (cameraUri != null) {
                    PublishFragment.this.mViewModel.setPhotoURI(cameraUri);
                    PublishFragment.this.takePictureLauncher.launch(cameraUri);
                }
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
                PublishFragment.this.mViewModel.setVisibility(false); // set to PRIVATE
            }
        });

        binding.publishEditLocation.setOnClickListener(v -> {
            LocationPickerDialog locationPicker = new LocationPickerDialog();
            locationPicker.show(getChildFragmentManager(), "LOCATION_PICKER");

            locationPicker.setLocationResultListener(location -> {
                if (location.getLocationType() == LocationType.EXACT) {
                    ExactLocation exact = (ExactLocation) location;
                    //binding.publishEditLocation.setText(formatToDMS(exact.getLatitude(), exact.getLongitude()));
                    this.mViewModel.setLocation(exact);
                } else if (location.getLocationType() == LocationType.APPROXIMATE) {
                    ApproximateLocation approx = (ApproximateLocation) location;
                    //binding.publishEditLocation.setText(approx.getName());
                    this.mViewModel.setLocation(approx);
                }
            });
        });

        binding.publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishViewModel.ValidationFormStatus status = mViewModel.validateForm();

                switch (status) {
                    case OK:
                        PublishFragment.this.mViewModel.saveImagePublication(getContext(), task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Your post has been uploaded!", Toast.LENGTH_LONG).show();
                                resetForm();
                            }
                            else {
                                Toast.makeText(getContext(), "Error: Your post could not be uploaded. Check internet connection.", Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case PICTURE_MISSING:
                        Toast.makeText(getContext(), "Picture is required", Toast.LENGTH_SHORT).show();
                        break;
                    case DATE_MISSING:
                        Toast.makeText(getContext(), "Date is required", Toast.LENGTH_SHORT).show();
                        break;
                    case DESCRIPTION_MISSING:
                        Toast.makeText(getContext(), "Description is required", Toast.LENGTH_SHORT).show();
                        break;
                    case INSTRUCTIONS_MISSING:
                        Toast.makeText(getContext(), "Instructions are required", Toast.LENGTH_SHORT).show();
                        break;
                    case LOCATION_MISSING:
                        Toast.makeText(getContext(), "Location is required", Toast.LENGTH_SHORT).show();
                        break;
                }
                // Save image publication
            }
        });

        return root;
    }

    private void resetForm() {
        this.mViewModel.reset();

    }

    private void checkRequiredFields() {
        binding.publishButton.setEnabled(
                mViewModel.getPhotoURI().getValue() != null
             && mViewModel.getDescription().getValue() != null
             && !mViewModel.getDescription().getValue().trim().isEmpty()
             && mViewModel.getInstructions().getValue() != null
             && !mViewModel.getInstructions().getValue().trim().isEmpty()
             && mViewModel.getLocation().getValue() != null);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mViewModel.getDate().observe(this.getViewLifecycleOwner(), date -> {
            checkRequiredFields();

            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                String formattedDate = sdf.format(date);
                binding.publishEditDate.setText(formattedDate);
            }
            else {
                binding.publishEditDate.setText("dd/mm/yyyy");
            }
        });

        this.mViewModel.getPhotoURI().observe(this.getViewLifecycleOwner(), uri -> {
            checkRequiredFields();

            if (uri != null) {
                binding.publishImageCardview.setImageTintList(null);

                Glide.with(this)
                        .load(uri)
                        .centerCrop()
                        .into(binding.publishImageCardview);
            }
            else {
                Glide.with(this).clear(binding.publishImageCardview);

                binding.publishImageCardview.setImageTintList(null);
                binding.publishImageCardview.setImageResource(R.drawable.image_48px);
                binding.publishImageCardview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        });

        this.mViewModel.getVisibility().observe(this.getViewLifecycleOwner(), is_public -> {
            checkRequiredFields();

            if (is_public != binding.publicToggleButton.isChecked()) {
                binding.publicToggleButton.setChecked(is_public);
            }
            if (!is_public != binding.privateToggleButton.isChecked()) {
                binding.privateToggleButton.setChecked(!is_public);
            }
        });

        this.mViewModel.getDescription().observe(this.getViewLifecycleOwner(), description -> {
            checkRequiredFields();

            if (description == null) {
                binding.publishEditDescription.setText("");
            }
            else if (!description.equals(binding.publishEditDescription.getText().toString())) {
                binding.publishEditDescription.setText(description);
            }
        });

        this.mViewModel.getInstructions().observe(this.getViewLifecycleOwner(), instructions -> {
            checkRequiredFields();

            if (instructions == null) {
                binding.publishEditInstructions.setText("");
            }
            else if (!instructions.equals(binding.publishEditInstructions.getText().toString())) {
                binding.publishEditInstructions.setText(instructions);
            }
        });

        this.mViewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            checkRequiredFields();

            if (location != null) {
                switch (location.getLocationType()) {
                    case EXACT:
                        binding.publishEditLocation.setText(formatToDMS(
                                location.getLatitude(),
                                location.getLongitude()
                        ));
                        break;
                    case APPROXIMATE:
                        binding.publishEditLocation.setText(String.format("%s, %s, %s, %s",
                                location.getName(),
                                location.getCity(),
                                location.getRegion(),
                                location.getCountry()
                        ));
                        break;
                }
            }
            else {
                binding.publishEditLocation.setText("");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}