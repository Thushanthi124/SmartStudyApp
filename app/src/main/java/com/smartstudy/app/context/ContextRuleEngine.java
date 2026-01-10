package com.smartstudy.app.context;

import android.util.Log;

public class ContextRuleEngine {

    private static final String TAG = "ContextEngine";

    private boolean lowLight = false;
    private boolean focusMode = false;

    // =====================
    // Action callbacks
    // =====================
    public interface ActionListener {
        void enableNightMode();
        void disableNightMode();
        void triggerFocusMode();
        void exitFocusMode();
        void showStretchReminder();
    }

    private final ActionListener listener;

    public ContextRuleEngine(ActionListener listener) {
        this.listener = listener;
    }

    // =====================
    // Light-based rules
    // =====================
    public void onLowLightDetected() {
        Log.d(TAG, "Sensor event: Low ambient light");

        if (!lowLight) {
            lowLight = true;
            Log.d(TAG, "Rule applied: Enable night mode");
            listener.enableNightMode();
        }
    }

    public void onNormalLightDetected() {
        Log.d(TAG, "Sensor event: Normal ambient light");

        if (lowLight) {
            lowLight = false;
            Log.d(TAG, "Rule applied: Disable night mode");
            listener.disableNightMode();
        }
    }

    // =====================
    // Proximity-based rules
    // =====================
    public void onPhoneNearFace() {
        Log.d(TAG, "Sensor event: Phone near face");

        if (!focusMode) {
            focusMode = true;
            Log.d(TAG, "Rule applied: Enter focus mode");
            listener.triggerFocusMode();
        }
    }

    public void onPhoneAwayFromFace() {
        Log.d(TAG, "Sensor event: Phone away from face");

        if (focusMode) {
            focusMode = false;
            Log.d(TAG, "Rule applied: Exit focus mode");
            listener.exitFocusMode();
        }
    }

    // =====================
    // Activity-based rules
    // =====================
    public void onUserInactiveTooLong() {
        Log.d(TAG, "Rule applied: User inactive → stretch reminder");
        listener.showStretchReminder();
    }
}
