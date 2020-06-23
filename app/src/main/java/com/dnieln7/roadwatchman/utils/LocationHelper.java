package com.dnieln7.roadwatchman.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dnieln7.roadwatchman.R;

/**
 * Helper class to get user location
 *
 * @author dnieln7
 */
public class LocationHelper implements LocationListener {
    public static final int LOCATION_PERMISSION_CODE = 101;

    private static LocationHelper instance = null;

    private LocationManager locationManager;
    private Location location;

    @SuppressLint("MissingPermission")
    private LocationHelper(Activity activity) {
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        this.locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                this,
                Looper.myLooper()
        );
    }

    /**
     * @param activity The current activity to get a {@link LocationManager} instance.
     * @return An instance of {@link LocationHelper};
     * null if the required location permissions are not granted or if the location access on the device is off.
     */
    public static LocationHelper getInstance(Activity activity) {
        if (LocationHelper.hasLocationPermissions(activity)) {
            if (LocationHelper.hasGPSEnabled(activity) || LocationHelper.hasNetworkEnabled(activity)) {
                if (instance == null) {
                    instance = new LocationHelper(activity);
                }

                return instance;
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

    /**
     * Uses the device GPS to request it's current location.
     *
     * @return The current location of the device.
     */
    @SuppressLint("MissingPermission")
    public Location getCurrentLocation() {
        if (location == null) {
            locationManager.requestSingleUpdate(
                    LocationManager.GPS_PROVIDER,
                    this,
                    Looper.myLooper()
            );
        }

        return location;
    }

    /**
     * Checks if the device has the GPS enabled.
     *
     * @param activity The activity to call {@link LocationManager}.
     * @return True if the GPS is enabled; false otherwise
     */
    public static boolean hasGPSEnabled(Activity activity) {
        return ((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Starts an activity requesting the user to activate the device GPS.
     *
     * @param activity The activity to create the location settings intent.
     */
    public static void askActivateLocation(Activity activity) {
        Toast.makeText(activity, activity.getString(R.string.upload_report_location), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * Checks if the device network location service is enabled.
     *
     * @param activity The activity to call {@link LocationManager}.
     * @return True if the Network location service is enabled; false otherwise
     */
    public static boolean hasNetworkEnabled(Activity activity) {
        return ((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Checks if the required location permissions are enabled.
     *
     * @param activity The activity to call {@link ContextCompat}.
     * @return True if the permissions are enabled.
     */
    public static boolean hasLocationPermissions(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Starts an activity requesting the user to grant the required location permissions.
     *
     * @param activity The activity to ask for the location permissions.
     */
    public static void askLocationPermissions(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                LOCATION_PERMISSION_CODE
        );
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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
