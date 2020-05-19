package com.dnieln7.roadwatchman.ui.app.fragment.settings.network;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.ui.app.fragment.reportes.background.ReportesService;
import com.dnieln7.roadwatchman.utils.LocationUtils;
import com.dnieln7.roadwatchman.utils.NetworkMonitor;
import com.dnieln7.roadwatchman.utils.Printer;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class NetworkActivity extends AppCompatActivity {

    private SwitchMaterial backgroundReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        initWidgets();
        initListeners();
    }

    private void initWidgets() {
        setSupportActionBar(findViewById(R.id.network_toolbar));

        backgroundReportes = findViewById(R.id.network_background_reportes);
        backgroundReportes.setChecked(ReportesService.instance != null);
    }

    private void initListeners() {
        backgroundReportes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showReporteServiceWarning();
            }
            else {
                stopReportesService();
            }
        });
    }

    private boolean checkLocation() {
        if (LocationUtils.hasGPSEnabled(Objects.requireNonNull(this))) {
            startReportesService();
            return true;
        }

        LocationUtils.activateLocation(this);
        return false;
    }

    private boolean checkConnection() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (NetworkMonitor.getMonitor(this).hasNetwork()) {
                return checkLocation();
            }
        }
        else {
            if (NetworkMonitor.hasNetwork(this)) {
                startReportesService();
                return checkLocation();
            }
        }

        Printer.okDialog(this, getString(R.string.warning), getString(R.string.settings_warning));
        return false;
    }

    private void showReporteServiceWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.settings_background);
        builder.setMessage(R.string.settings_background_warning);
        builder.setPositiveButton(R.string.settings_continue, (dialog, which) -> backgroundReportes.setChecked(checkConnection()));
        builder.setNegativeButton(R.string.settings_cancel, (dialog, which) -> backgroundReportes.setChecked(false));
        builder.show();
    }

    private void startReportesService() {
        Intent service = new Intent(this, ReportesService.class);
        startService(service);
    }

    private void stopReportesService() {
        Intent service = new Intent(this, ReportesService.class);
        stopService(service);
    }
}
