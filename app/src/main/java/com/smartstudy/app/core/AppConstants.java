package com.smartstudy.app.core;

public final class AppConstants {

    // Light sensor thresholds (lux)
    public static final float LOW_LIGHT_THRESHOLD = 10f;

    // Session timing
    public static final long MIN_READING_TIME_MS = 5 * 60 * 1000; // 5 minutes

    // Distraction detection
    public static final int MAX_MOVEMENT_EVENTS = 5;

    private AppConstants() {
        // Prevent object creation
    }
}
