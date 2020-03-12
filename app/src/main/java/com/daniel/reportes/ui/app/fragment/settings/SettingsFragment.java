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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.app.fragment.reportes.background.ReportesService;
import com.daniel.reportes.utils.LocationUtils;
import com.daniel.reportes.utils.NetworkMonitor;
import com.daniel.reportes.utils.Printer;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    // Objects
    private SharedPreferences sharedPreferences;

    // Widgets
    private View root;
    private SwitchMaterial darkTheme;
    private SwitchMaterial backgroundReportes;

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
        darkTheme = root.findViewById(R.id.settingsDarkTheme);
        backgroundReportes = root.findViewById(R.id.settingsReportesInBackground);

        darkTheme.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        backgroundReportes.setChecked(ReportesService.instance != null);
    }

    private void initListeners() {
        darkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> showCloseWarning());

        backgroundReportes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showReporteServiceWarning();
            }
            else {
                stopReportesService(getActivity());
            }
        });
    }

    private void showCloseWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Cambiar tema");
        builder.setMessage("La aplicación se cerrará para aplicar los cambios");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            sharedPreferences.edit().putBoolean("DarkTheme", AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES).apply();

            getActivity().finishAffinity();
        });
        builder.show();
    }

    private boolean checkLocation() {
        if (LocationUtils.hasGPSEnabled(Objects.requireNonNull(getActivity()))) {
            startReportesService(getActivity());
            return true;
        }

        LocationUtils.activateLocation(getActivity());
        return false;
    }

    private boolean checkConnection() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (NetworkMonitor.getMonitor(getContext()).hasNetwork()) {
                return checkLocation();
            }
        }
        else {
            if (NetworkMonitor.hasNetwork(getContext())) {
                startReportesService(getActivity());
                return checkLocation();
            }
        }

        Printer.snackBar(root, "Se necesita una conexión a internet activa");
        return false;
    }

    private void showReporteServiceWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Envío de reportes en segundo plano");
        builder.setMessage("Se enviarán reportes de manera automática cuando se detecten movimientos violentos");
        builder.setPositiveButton("Aceptar", (dialog, which) -> backgroundReportes.setChecked(checkConnection()));
        builder.setNegativeButton("Cancelar", (dialog, which) -> backgroundReportes.setChecked(false));
        builder.show();
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