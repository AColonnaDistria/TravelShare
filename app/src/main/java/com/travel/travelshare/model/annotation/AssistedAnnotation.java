package com.travel.travelshare.model.annotation;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssistedAnnotation extends Annotation {
    // up to 16 tags
    private static final int TAGS_LIMIT = 16;
    private static final String TAGS_ID = "tags_id";

    private ArrayList<String> tagsId; // tag ids

    public AssistedAnnotation() {
        super(AnnotationType.ASSISTED);
    }

    public AssistedAnnotation(Timestamp createdAt) {
        super(AnnotationType.ASSISTED, createdAt);
    }

    public boolean addTag(String tagId) {
        if (this.tagsId.size() >= TAGS_LIMIT) return false;

        return this.tagsId.add(tagId);
    }

    public boolean setTagsId(List<String> tagsId) {
        if (tagsId.size() >= TAGS_LIMIT) return false;

        this.tagsId.clear();
        return this.tagsId.addAll(tagsId);
    }

    public List<String> getTagsId() {
        return Collections.unmodifiableList(this.tagsId);
    }
}
