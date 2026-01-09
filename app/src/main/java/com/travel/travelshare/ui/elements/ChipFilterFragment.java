package com.travel.travelshare.ui.elements;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.travel.travelshare.R;
import com.travel.travelshare.databinding.FragmentChipFilterBinding;
import com.travel.travelshare.ui.map.MapViewModel;

import java.util.Arrays;
import java.util.List;

public class ChipFilterFragment extends Fragment {
    private ChipFilterViewModel mViewModel;

    private ChipGroup chipGroup;
    private FragmentChipFilterBinding binding;


    public static ChipFilterFragment newInstance() {
        return new ChipFilterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChipFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.mViewModel = new ViewModelProvider(this).get(ChipFilterViewModel.class);

        ChipGroup chipGroup = binding.chipGroupFilters;

        for (String label : this.mViewModel.getLabels()) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_filter_chip, chipGroup, false);

            chip.setText(label);

            /*
            // Gestion du clic pour Multi-sélection
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    activeFilters.add(category); // Ajouter à la liste
                } else {
                    activeFilters.remove(category); // Retirer de la liste
                }

                // Appeler votre méthode de filtrage avec la liste complète
                applyFilters(activeFilters);
            })
             */

            chipGroup.addView(chip);
        }

        return root;    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChipFilterViewModel.class);
        // TODO: Use the ViewModel
    }

}