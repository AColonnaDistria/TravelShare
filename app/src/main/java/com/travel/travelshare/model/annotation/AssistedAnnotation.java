package com.travel.travelshare.model.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssistedAnnotation extends Annotation {
    private ArrayList<Tag> tags;

    public AssistedAnnotation() {

    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }
}
