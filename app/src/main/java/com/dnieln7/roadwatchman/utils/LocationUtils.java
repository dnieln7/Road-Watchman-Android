package com.dnieln7.roadwatchman.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

public class LocationUtils {
    private static android.location.LocationListener locationListener;
    private static Location knownLocation;

    @SuppressLint("MissingPermission")
    public static Location getGPS(Activity activity) {
        if (LocationHelper.hasLocationPermissions(activity)) {
            if (LocationHelper.hasGPSEnabled(activity)) {
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
                LocationHelper.askActivateLocation(activity);
            }
        }
        else {
            LocationHelper.askLocationPermissions(activity);
        }

        return null;
    }

    @SuppressLint("MissingPermission")
    public static Location getNetwork(Activity activity) {
        if (LocationHelper.hasLocationPermissions(activity)) {
            if (LocationHelper.hasNetworkEnabled(activity)) {
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
            LocationHelper.askActivateLocation(activity);
        }

        return null;
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
