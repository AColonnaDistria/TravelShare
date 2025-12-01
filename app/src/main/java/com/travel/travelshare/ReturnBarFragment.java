package com.travel.travelshare;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travel.travelshare.databinding.FragmentReturnBarBinding;

public class ReturnBarFragment extends Fragment {

    private ReturnBarViewModel mViewModel;
    private FragmentReturnBarBinding binding;

    public interface OnCloseRequestedListener {
        void onRequestClose();
    }
    private OnCloseRequestedListener callback;

    public static ReturnBarFragment newInstance() {
        return new ReturnBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReturnBarBinding.inflate(inflater, container, false);

        binding.back.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestClose();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReturnBarViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCloseRequestedListener) {
            callback = (OnCloseRequestedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCloseRequestedListener");
        }
    }
}