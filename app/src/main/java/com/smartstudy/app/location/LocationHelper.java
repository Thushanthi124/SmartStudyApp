package com.smartstudy.app.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    public static final int LOCATION_PERMISSION_REQUEST = 200;

    private final Activity activity;
    private final FusedLocationProviderClient locationClient;

    public interface LocationCallback {
        void onLocationReceived(Location location);
        void onLocationUnavailable();
    }

    public LocationHelper(Activity activity) {
        this.activity = activity;
        this.locationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void fetchLocation(LocationCallback callback) {
        if (!hasPermission()) {
            requestPermission();
            callback.onLocationUnavailable();
            return;
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.onLocationReceived(location);
                    } else {
                        callback.onLocationUnavailable();
                    }
                });
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST
        );
    }
}
