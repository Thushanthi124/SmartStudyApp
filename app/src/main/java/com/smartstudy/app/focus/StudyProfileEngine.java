package com.smartstudy.app.focus;

import android.location.Location;
import android.util.Log;

public class StudyProfileEngine {

    private static final String TAG = "StudyProfileEngine";

    // Example: University library coordinates (replace later if needed)
    private static final double LIBRARY_LAT = 6.9746;
    private static final double LIBRARY_LNG = 79.9156;
    private static final float LIBRARY_RADIUS_METERS = 150f;

    public interface ProfileListener {
        void onDeepFocus();
        void onQuickRead();
        void onNormalMode();
    }

    private final ProfileListener listener;

    public StudyProfileEngine(ProfileListener listener) {
        this.listener = listener;
    }

    public void evaluate(Location location) {
        if (location == null) {
            Log.d(TAG, "Location unavailable → Normal mode");
            listener.onNormalMode();
            return;
        }

        float[] distance = new float[1];
        Location.distanceBetween(
                location.getLatitude(),
                location.getLongitude(),
                LIBRARY_LAT,
                LIBRARY_LNG,
                distance
        );

        if (distance[0] <= LIBRARY_RADIUS_METERS) {
            Log.d(TAG, "Library detected → Deep focus mode");
            listener.onDeepFocus();
        } else {
            Log.d(TAG, "Non-library environment → Quick read");
            listener.onQuickRead();
        }
    }
}
