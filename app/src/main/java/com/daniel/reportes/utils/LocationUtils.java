package com.daniel.reportes.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationUtils {

    public static final int LOCATION_PERMISSION_CODE = 101;
    private static android.location.LocationListener locationListener;
    private static Location knownLocation;

    @SuppressLint("MissingPermission")
    public static Location getGPS(Activity activity) {
        if (hasPermissions(activity)) {
            if (hasGPSEnabled(activity)) {
                LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener();

                if (knownLocation == null) {
                    manager.requestSingleUpdate(
                            LocationManager.GPS_PROVIDER,
                            locationListener,
                            Looper.myLooper()
                    );

                }
                else {
                    return knownLocation;
                }
            }
            else {
                activateLocation(activity);
            }
        }
        else {
            askPermissions(activity);
        }

        return null;
    }

    @SuppressLint("MissingPermission")
    public static Location getNetwork(Activity activity) {
        if (hasPermissions(activity)) {
            if (hasNetworkEnabled(activity)) {
                LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                knownLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationListener = new LocationListener();

                if (knownLocation == null) {
                    manager.requestSingleUpdate(
                            LocationManager.NETWORK_PROVIDER,
                            locationListener,
                            Looper.myLooper()
                    );
                }
                else {
                    return knownLocation;
                }
            }
            else {
                Toast.makeText(activity, "Conexión a internet no dispobible", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                activity.startActivity(intent);
            }
        }
        else {
            askPermissions(activity);
        }

        return null;
    }

    private static boolean hasPermissions(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private static void askPermissions(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                LOCATION_PERMISSION_CODE
        );
    }

    private static boolean hasNetworkEnabled(Activity activity) {
        return ((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean hasGPSEnabled(Activity activity) {
        return ((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void activateLocation(Activity activity) {
        Toast.makeText(activity, "Por favor activa la ubicación", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }

    private static void setLocation(Location location) {
        knownLocation = location;
        locationListener = null;
    }

    private static class LocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
