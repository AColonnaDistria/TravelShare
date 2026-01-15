package com.travel.travelshare.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.travel.travelshare.LocationUtils;
import com.travel.travelshare.databinding.DialogDescriptionInputBinding;
import com.travel.travelshare.databinding.DialogLocationPickerBinding;
import com.travel.travelshare.model.location.Location;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class DialogDescriptionInput extends BottomSheetDialogFragment {
    private static final String ARG_DEFAULT_TEXT = "default_text";

    public static DialogDescriptionInput newInstance(String defaultText) {
        DialogDescriptionInput fragment = new DialogDescriptionInput();
        Bundle args = new Bundle();
        args.putString(ARG_DEFAULT_TEXT, defaultText);
        fragment.setArguments(args);
        return fragment;
    }

    private DialogDescriptionInputBinding binding;

    private DescriptionResultListener listener;

    public interface DescriptionResultListener {
        void onDescriptionEntered(String description);
    }

    public void setDescriptionResultListener(DescriptionResultListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDescriptionInputBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            String defaultText = getArguments().getString(ARG_DEFAULT_TEXT);
            if (defaultText != null) {
                binding.editDescription.setText(defaultText);
                binding.editDescription.setSelection(defaultText.length());
            }
        }

        binding.btnConfirmDescription.setOnClickListener(v -> {
            if (listener != null) {
                String textDescription = binding.editDescription.getText().toString();

                listener.onDescriptionEntered(textDescription);
            }
            dismiss();
        });

        return binding.getRoot();
    }
}
