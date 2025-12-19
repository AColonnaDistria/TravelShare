package com.travel.travelshare.model.annotation;

public class TextAnnotation extends Annotation {
    private String textContent;

    public TextAnnotation(String textContent) {
        this.setTextContent(textContent);
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
