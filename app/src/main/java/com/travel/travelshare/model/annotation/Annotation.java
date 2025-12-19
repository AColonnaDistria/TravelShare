package com.travel.travelshare.model.annotation;

public class Annotation {
    private AnnotationType annotationType;

    public Annotation() {}

    public Annotation(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }
}
