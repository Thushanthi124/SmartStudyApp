package com.smartstudy.app.core;

/**
 * All module-to-module communication
 * MUST go through these interfaces.
 */
public interface ModuleContracts {

    /**
     * Sensor & context events are reported here
     */
    interface ContextListener {
        void onLowLightDetected();
        void onGoodLightDetected();
        void onPossibleDistraction();
        void onStretchReminder();
    }

    /**
     * Reading session lifecycle
     */
    interface ReadingSessionListener {
        void onSessionStarted();
        void onSessionPaused();
        void onSessionEnded();
    }
}
