package com.travel.travelshare.model.annotation;

import com.google.firebase.Timestamp;

public class AudioAnnotation extends Annotation {
    private String audio_URI;

    public AudioAnnotation() {
        super(AnnotationType.AUDIO);
    }

    public AudioAnnotation(Timestamp createdAt) {
        super(AnnotationType.AUDIO, createdAt);
    }

    public AudioAnnotation(String audioUri, Timestamp createdAt) {
        super(AnnotationType.AUDIO, createdAt);
        this.setAudio_URI(audioUri);
    }

    public String getAudio_URI() {
        return audio_URI;
    }

    public void setAudio_URI(String audio_URI) {
        this.audio_URI = audio_URI;
    }
}
