package com.travel.travelshare.model.annotation;

import org.w3c.dom.Text;

public class TextAnnotation extends Annotation {
    private String textContent;

    public TextAnnotation() {
        super(AnnotationType.TEXT);
    }

    public TextAnnotation(String textContent) {
        super(AnnotationType.TEXT);
        this.setTextContent(textContent);
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
