package com.dnieln7.roadwatchman.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Helper class to check network type and status.
 *
 * @author dnieln7
 */
public class NetworkMonitor extends ConnectivityManager.NetworkCallback {

    private static NetworkMonitor monitor;
    private ConnectivityManager manager;
    private boolean wifi;
    private boolean data;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private NetworkMonitor(Context context) {
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            manager.registerDefaultNetworkCallback(this);
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * For android N and up, Create new instance of NetworkMonitor.
     *
     * @param context The activity context.
     * @return An instance of NetworkMonitor registered at {@link ConnectivityManager}.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static NetworkMonitor getMonitor(Context context) {
        if (monitor == null) {
            return monitor = new NetworkMonitor(context);
        }
        return monitor;
    }

    /**
     * For android M and lower. Checks if the active network is wifi.
     *
     * @return true if wifi is enabled and connected, false otherwise.
     */
    public static boolean hasWifi(Context context) {
        if (context == null) {
            return false;
        }

        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo()
                .getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * For android M and lower. Checks if the active network is mobile data.
     *
     * @return true if mobile data is enabled and connected, false otherwise.
     */
    public static boolean hasData(Context context) {
        if (context == null) {
            return false;
        }

        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo()
                .getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * For android M and lower. Checks if the network is connected regardless of the type.
     *
     * @return true if there is an enabled network, false otherwise.
     */
    public static boolean hasNetwork(Context context) {
        return NetworkMonitor.hasWifi(context) || NetworkMonitor.hasData(context);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        if (manager.isActiveNetworkMetered()) {
            data = true;
            wifi = false;
        }
        else {
            wifi = true;
            data = false;
        }
    }

    @Override
    public void onLost(@NonNull Network network) {
        wifi = false;
        data = false;
    }

    /**
     * For android N and up. Checks if the active network is wifi.
     *
     * @return true if wifi is enabled and connected, false otherwise.
     */
    public boolean hasWifi() {
        return wifi;
    }

    /**
     * For android N and up. Checks if the active network is mobile data.
     *
     * @return true if mobile data is enabled and connected, false otherwise.
     */
    public boolean hasData() {
        return data;
    }

    /**
     * For android N and up. Checks if the network is connected regardless of the type.
     *
     * @return true if there is an enabled network, false otherwise.
     */
    public boolean hasNetwork() {
        return wifi || data;
    }

    /**
     * For android N and up. Unregisters the current instance of {@link ConnectivityManager}
     * and destroys the instance.
     */
    public void unregister() {
        manager.unregisterNetworkCallback(this);
        monitor = null;
    }
}
