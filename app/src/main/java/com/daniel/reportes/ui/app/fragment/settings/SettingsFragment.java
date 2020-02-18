package com.daniel.reportes.ui.app.fragment.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.ui.app.fragment.reportes.background.SensorReporte;
import com.daniel.reportes.ui.app.fragment.reportes.background.ReportesService;
import com.daniel.reportes.utils.NetworkMonitor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.internal.Util;

public class SettingsFragment extends Fragment {

    // Objects
    private SharedPreferences sharedPreferences;

    // Widgets
    private View root;
    private Switch darkTheme;
    private Switch backgroundReportes;
    private MaterialButton sensor;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void initObjects() {
        sharedPreferences = getActivity().getSharedPreferences("com.daniel.reportes.settings", Context.MODE_PRIVATE);

    }

    private void initWidgets() {
        darkTheme = root.findViewById(R.id.darkTheme);
        backgroundReportes = root.findViewById(R.id.settingsBackgroundReportes);
        sensor = root.findViewById(R.id.settingsSensor);

        darkTheme.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        backgroundReportes.setChecked(ReportesService.instance != null);
    }

    private void initListeners() {
        darkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferences.edit().putBoolean("DarkTheme", true).apply();
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferences.edit().putBoolean("DarkTheme", false).apply();
            }
        });

        backgroundReportes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (NetworkMonitor.getMonitor(getContext()).hasNetwork()) {
                        startReportesService(getActivity());
                    }
                    else {
                        Utils.toast(getContext(), "Se necesita una conexión activa");
                        backgroundReportes.setChecked(false);
                    }
                }
                else {
                    if (NetworkMonitor.hasNetwork(getContext())) {
                        startReportesService(getActivity());
                    }
                    else {
                        Utils.toast(getContext(), "Se necesita una conexión activa");
                        backgroundReportes.setChecked(false);
                    }
                }
            }
            else {
                stopReportesService(getActivity());
            }
        });

        sensor.setOnClickListener(v -> launchSensor());
    }

    private void launchSensor() {
        Intent intent = new Intent(getContext(), SensorReporte.class);

        startActivity(intent);
    }

    private void startReportesService(Activity activity) {
        Intent service = new Intent(activity, ReportesService.class);
        activity.startService(service);
    }

    private void stopReportesService(Activity activity) {
        Intent service = new Intent(activity, ReportesService.class);
        activity.stopService(service);
    }
}