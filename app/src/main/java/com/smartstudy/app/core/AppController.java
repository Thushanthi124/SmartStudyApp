package com.smartstudy.app.core;

import android.util.Log;

public class AppController implements ModuleContracts.ContextListener {

    private static AppController instance;
    private static final String TAG = "SmartStudyCore";

    private AppController() {}

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    @Override
    public void onLowLightDetected() {
        Log.d(TAG, "Low light detected");
    }

    @Override
    public void onGoodLightDetected() {
        Log.d(TAG, "Good lighting detected");
    }

    @Override
    public void onPossibleDistraction() {
        Log.d(TAG, "Possible distraction detected");
    }

    @Override
    public void onStretchReminder() {
        Log.d(TAG, "Stretch reminder triggered");
    }
}
