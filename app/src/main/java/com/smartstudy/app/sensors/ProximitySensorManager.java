package com.smartstudy.app.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ProximitySensorManager implements SensorEventListener {

    public interface Listener {
        void onNear();
        void onFar();
    }

    private final SensorManager sensorManager;
    private final Sensor proximitySensor;
    private final Listener listener;

    public ProximitySensorManager(Context context, Listener listener) {
        this.listener = listener;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    public void start() {
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance < proximitySensor.getMaximumRange()) {
            listener.onNear();   // phone close to face
        } else {
            listener.onFar();    // phone away
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
