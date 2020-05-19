package com.dnieln7.roadwatchman;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String ID = "Reportes Channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID,
                    "Reportes",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
