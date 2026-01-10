package com.smartstudy.app.core;

public class SessionController {

    private static SessionController instance;
    private boolean sessionActive = false;

    private SessionController() {}

    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    public void startSession() {
        sessionActive = true;
    }

    public void pauseSession() {
        sessionActive = false;
    }

    public void endSession() {
        sessionActive = false;
    }

    public boolean isSessionActive() {
        return sessionActive;
    }
}
