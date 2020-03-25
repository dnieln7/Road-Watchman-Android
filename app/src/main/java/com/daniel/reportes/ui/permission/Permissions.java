package com.daniel.reportes.ui.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.daniel.reportes.R;
import com.daniel.reportes.utils.AppearanceController;
import com.daniel.reportes.utils.Printer;

public class Permissions extends AppCompatActivity {

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    public static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppearanceController.goFullScreen(this);
        setContentView(R.layout.activity_permissions);

        if(hasPermissions(this)) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE) {
            if(hasPermissions(this)){
                finish();
            }
            else {
                Printer.okDialog(this, "Permisos", "Debe conceder todos los permisos necesarios");
            }
        }
    }

    public static boolean hasPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void askPermissions(View view) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION
                },
                REQUEST_CODE
        );
    }

    public void exit(View view) {
        finishAffinity();
    }
}