package com.daniel.reportes.ui.app.fragment.reportes.create;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.data.User;
import com.daniel.reportes.utils.LocationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDateTime;

public class ReporteCreator extends AppCompatActivity {

    public static final int REQUEST_CODE = 2;

    private int currentStep;
    private User user;

    private CreateViewModel createViewModel;
    private Location location;

    private CreateReporte createReporte;
    private UploadReporte uploadReporte;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reporte);

        createViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        location = LocationUtils.getGPS(this);
        user = (User) getIntent().getSerializableExtra("user");

        initFragments();
    }

    private void initFragments() {
        currentStep = 1;

        createReporte = new CreateReporte();
        uploadReporte = new UploadReporte();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, uploadReporte)
                .add(R.id.fragment, createReporte)
                .commit();
    }

    public void next(View view) {
        switch (currentStep) {
            case 1:
                createReporte(view);
                break;
            case 2:
                uploadReporte();
        }
    }

    private void createReporte(View view) {
        createViewModel.getPicture().observe(this, hasPicture -> {
            if (location != null) {
                createViewModel.setReporte(new Reporte(
                        new LocalDateTime().toString(),
                        new double[]{location.getLatitude(), location.getLongitude()},
                        user.getId()
                ));

                createViewModel.appendPictureName("_" + user.getId());

                getSupportFragmentManager().beginTransaction().hide(createReporte).show(uploadReporte).commit();
                ((MaterialButton) findViewById(R.id.create_reporte_next)).setText("Subir");
                currentStep = 2;
            }
            else {
                Snackbar.make(view, "Calculando ubicación...", Snackbar.LENGTH_SHORT).show();
                location = LocationUtils.getGPS(this);
            }
        });
    }

    public void uploadReporte() {
        uploadReporte.upload();
    }

    @Override
    public void onBackPressed() {
        if (currentStep == 1) {
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("El reporte no se enviará, ¿Continuar?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    }).setNegativeButton("No", (dialog, which) -> {
            }).show();
        }
        else if (currentStep == 2) {
            getSupportFragmentManager().beginTransaction().hide(uploadReporte).show(createReporte).commit();
            ((MaterialButton) findViewById(R.id.create_reporte_next)).setText("Siguiente");
            currentStep = 1;
        }
    }
}
