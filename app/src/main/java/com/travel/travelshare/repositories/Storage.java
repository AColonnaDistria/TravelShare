package com.travel.travelshare.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.HashMap;
import java.util.Map;

import com.travel.travelshare.BuildConfig;

public class Storage {
    private static Map config;
    public static void init(Context context) {
        try {
            Storage.config = Map.of(
                    "cloud_name",BuildConfig.CLOUDINARY_CLOUD_NAME,
                    "api_key",BuildConfig.CLOUDINARY_API_KEY,
                    "api_secret",BuildConfig.CLOUDINARY_API_SECRET,
                    "secure", true
            );

            MediaManager.init(context, Storage.config);
        } catch (IllegalStateException e) {

        }
    }

    public interface OnUploadListener {
        void onSuccess(String imageUrl);

        void onFailure(String error);
    }

    public static void uploadImage(Uri filePath, OnUploadListener listener) {
        // "android_upload" is the preset you made in the dashboard
        MediaManager.get().upload(filePath)
                .unsigned("travelshare_upload")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Extract the secure HTTPS URL
                        String url = (String) resultData.get("secure_url");
                        Log.d("[STORAGE]", "Upload success: " + url);

                        // Tell the Activity!
                        if (listener != null) listener.onSuccess(url);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e("[STORAGE]", "Upload failed: " + error.getDescription());

                        // Tell the Activity!
                        if (listener != null) listener.onFailure(error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                })
                .dispatch();
    }
}