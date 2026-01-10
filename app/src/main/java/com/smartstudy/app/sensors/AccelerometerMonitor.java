package com.smartstudy.app.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerMonitor implements SensorEventListener {

    public interface Listener {
        void onUserInactive();
        void onMovementDetected();
    }

    private static final float MOVEMENT_THRESHOLD = 0.5f;
    private static final long INACTIVITY_TIME_MS = 5 * 60 * 1000; // 5 minutes

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Listener listener;

    private long lastMovementTime = System.currentTimeMillis();

    public AccelerometerMonitor(Context context, Listener listener) {
        this.listener = listener;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = Math.abs(event.values[0]);
        float y = Math.abs(event.values[1]);
        float z = Math.abs(event.values[2]);

        if (x > MOVEMENT_THRESHOLD || y > MOVEMENT_THRESHOLD || z > MOVEMENT_THRESHOLD) {
            lastMovementTime = System.currentTimeMillis();
            listener.onMovementDetected();
        }

        long now = System.currentTimeMillis();
        if (now - lastMovementTime > INACTIVITY_TIME_MS) {
            listener.onUserInactive();
            lastMovementTime = now; // prevent spam
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
