package com.dnieln7.roadwatchman.ui.app.fragment.reportes.background;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.dnieln7.roadwatchman.App;
import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.Reporte;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.roadwatchman.task.reporte.PostReporte;
import com.dnieln7.roadwatchman.ui.login.Login;
import com.dnieln7.roadwatchman.utils.Printer;

import org.joda.time.LocalDateTime;

public class ReportesService extends Service implements SensorEventListener, LocationListener {

    public static ReportesService instance;
    private SensorManager sensorManager;
    private LocationManager locationManager;

    private long lastTime;
    private int UserId;
    private Location location;

    // Class
    private TaskListener<Reporte> reporteListener = new TaskListener<Reporte>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(getBaseContext(), this.exception.getMessage());
                return false;
            }
            return true;
        }
    };

    // Called when service is created
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5,
                5,
                this
        );
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        );

        lastTime = System.currentTimeMillis();
    }

    // Called when service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UserId = intent.getIntExtra("UserId", 1);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Intent app = new Intent(this, Login.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, app, 0);

            Notification notification = new Notification.Builder(this, App.ID)
                    .setContentTitle("Reportes")
                    .setContentText("Servicio en ejecución...")
                    .setSmallIcon(R.drawable.reportes)
                    .build();

            startForeground(1, notification);
        }

        return START_STICKY;
    }

    // Called when service is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float acceleration = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();

        if (actualTime - lastTime > 2000) {
            if (acceleration >= 4) {
                lastTime = actualTime;
                Reporte reporte = new Reporte(
                        new LocalDateTime().toString(),
                        new double[]{location.getLatitude(), location.getLongitude()},
                        UserId
                );

                reporte.setDescription("Auto-Generado");

                new PostReporte(reporteListener).execute(reporte);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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