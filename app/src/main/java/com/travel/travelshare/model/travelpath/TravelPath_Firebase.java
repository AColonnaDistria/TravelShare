package com.travel.travelshare.model.travelpath;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travel.travelshare.BuildConfig;

public class TravelPath_Firebase {
    private static TravelPath_Firebase instance;
    private final FirebaseFirestore firestore;
    private final FirebaseApp app;

    private TravelPath_Firebase(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(BuildConfig.TRAVELPATH_API_KEY)
                .setApplicationId(BuildConfig.TRAVELPATH_APP_ID)
                .setProjectId(BuildConfig.TRAVELPATH_PROJECT_ID)
                .build();

        app = FirebaseApp.initializeApp(context.getApplicationContext(), options, "travelpath");
        this.firestore = FirebaseFirestore.getInstance(app);
    }

    public FirebaseFirestore getDatabase() {
        return this.firestore;
    }

    public static synchronized TravelPath_Firebase getInstance(Context context) {
        if (instance == null) {
            instance = new TravelPath_Firebase(context);
        }
        return instance;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }
}