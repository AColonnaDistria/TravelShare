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
import com.travel.travelshare.databinding.DialogInstructionsInputBinding;
import com.travel.travelshare.databinding.DialogLocationPickerBinding;
import com.travel.travelshare.model.location.Location;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class DialogInstructionsInput extends BottomSheetDialogFragment {
    private static final String ARG_DEFAULT_TEXT = "default_text";

    public static DialogInstructionsInput newInstance(String defaultText) {
        DialogInstructionsInput fragment = new DialogInstructionsInput();
        Bundle args = new Bundle();
        args.putString(ARG_DEFAULT_TEXT, defaultText);
        fragment.setArguments(args);
        return fragment;
    }

    private DialogInstructionsInputBinding binding;

    private InstructionsResultListener listener;

    public interface InstructionsResultListener {
        void onInstructionsEntered(String instructions);
    }

    public void setInstructionsResultListener(InstructionsResultListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInstructionsInputBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            String defaultText = getArguments().getString(ARG_DEFAULT_TEXT);
            if (defaultText != null) {
                binding.editInstructions.setText(defaultText);
                binding.editInstructions.setSelection(defaultText.length());
            }
        }

        binding.btnConfirmInstructions.setOnClickListener(v -> {
            if (listener != null) {
                String textInstructions = binding.editInstructions.getText().toString();

                listener.onInstructionsEntered(textInstructions);
            }
            dismiss();
        });

        return binding.getRoot();
    }
}
