package com.travel.travelshare.model.annotation;

import com.google.firebase.Timestamp;

public class Annotation {
    private AnnotationType annotationType;
    private Timestamp createdAt;

    public Annotation() {}

    public Annotation(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public Annotation(AnnotationType annotationType, Timestamp createdAt) {
        this.annotationType = annotationType;
        this.createdAt = createdAt;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
