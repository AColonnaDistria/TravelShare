package com.travel.travelshare.model.annotation;

public class AudioAnnotation extends Annotation {
    private String audio_URI;

    public AudioAnnotation() {
        super(AnnotationType.ASSISTED);
    }

    public AudioAnnotation(String audioUri) {
        super(AnnotationType.AUDIO);
        this.setAudio_URI(audioUri);
    }

    public String getAudio_URI() {
        return audio_URI;
    }

    public void setAudio_URI(String audio_URI) {
        this.audio_URI = audio_URI;
    }
}
