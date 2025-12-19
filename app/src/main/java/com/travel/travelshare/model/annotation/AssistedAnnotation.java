package com.travel.travelshare.model.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssistedAnnotation extends Annotation {
    // up to 16 tags
    private ArrayList<String> tags; // tag ids

    public AssistedAnnotation() {
        super(AnnotationType.ASSISTED);
    }

    public boolean addTag(String tagId) {
        if (this.tags.size() >= 8) return false;

        return this.tags.add(tagId);
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
}
