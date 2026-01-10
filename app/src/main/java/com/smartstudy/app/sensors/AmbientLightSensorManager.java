package com.smartstudy.app.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AmbientLightSensorManager implements SensorEventListener {

    public interface Listener {
        void onLowLight();
        void onNormalLight();
    }

    private static final float LOW_LIGHT_THRESHOLD = 15f; // lux

    private final SensorManager sensorManager;
    private final Sensor lightSensor;
    private final Listener listener;

    public AmbientLightSensorManager(Context context, Listener listener) {
        this.listener = listener;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void start() {
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        if (lux < LOW_LIGHT_THRESHOLD) {
            listener.onLowLight();
        } else {
            listener.onNormalLight();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
