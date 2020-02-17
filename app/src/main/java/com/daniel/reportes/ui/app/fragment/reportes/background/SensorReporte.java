package com.daniel.reportes.ui.app.fragment.reportes.background;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.daniel.reportes.R;

public class SensorReporte extends Activity implements SensorEventListener {

    // Objects
    private boolean on;
    private long lastTime;
    private SensorManager sensorManager;

    private int counter10;
    private int counter15;
    private int counter20;

    //Widgets
    private Switch control;
    private TextView message;

    private TextView message10;
    private TextView message15;
    private TextView message20;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sensor);

        /*getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );*/

        initObjects();
        initWidgets();
        initListeners();
    }

    public void initObjects() {
        on = false;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastTime = System.currentTimeMillis();

        counter10 = 0;
        counter15 = 0;
        counter20 = 0;
    }

    public void initWidgets() {
        control = findViewById(R.id.sensorControl);
        message = findViewById(R.id.sensorMessage);

        message10 = findViewById(R.id.sensor10);
        message15 = findViewById(R.id.sensor15);
        message20 = findViewById(R.id.sensor20);
    }

    public void initListeners() {
        control.setOnCheckedChangeListener((buttonView, isChecked) -> {
            on = isChecked;
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (on) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                getAccelerometer(event);
            }
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float acceleration = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();

        if (actualTime - lastTime > 1000) {
            message.setText("Aceleración -> " + acceleration);

            if (acceleration >= 5) {
                lastTime = actualTime;
                counter20++;
                message20.setText("Aceleración 5 -> " + acceleration + " Veces -> " + counter20);
            }

            else if (acceleration >= 4) {
                lastTime = actualTime;
                counter15++;
                message15.setText("Aceleración 4 -> " + acceleration + " Veces -> " + counter15);
            }

            else if (acceleration >= 2) {
                lastTime = actualTime;
                counter10++;
                message10.setText("Aceleración 2 -> " + acceleration + " Veces -> " + counter10);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
