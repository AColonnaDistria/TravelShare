package com.travel.travelshare.model.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssistedAnnotation extends Annotation {
    // up to 16 tags
    private ArrayList<Tag> tags;

    public AssistedAnnotation() {

    }

    public boolean addTag(Tag tag) {
        if (this.tags.size() >= 8) return false;

        return this.tags.add(tag);
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }
}
