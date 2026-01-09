package com.travel.travelshare.ui.elements;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.travel.travelshare.model.annotation.Tag;
import com.travel.travelshare.repositories.PostRepository;

import java.util.Arrays;
import java.util.List;

public class ChipFilterViewModel extends ViewModel {
    private MutableLiveData<List<String>> labels;

    public ChipFilterViewModel() {
        this.labels = new MutableLiveData<>();
        this.labels.setValue(Arrays.asList("Bearch", "Mountain", "Forest", "City Street", "Restaurant", "+ Add"));
    }

    public List<String> getLabels() {
        return labels.getValue();
    }

    public void clearLabels() {
        labels.getValue().clear();
    }

    public void addLabel(Tag tag) {
        labels.getValue().add(tag.getTagName());
    }
}