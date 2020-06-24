package com.dnieln7.roadwatchman.ui.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.utils.WindowController;
import com.dnieln7.roadwatchman.utils.Printer;

public class Permissions extends AppCompatActivity {

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    public static final int REQUEST_CODE = 100;

    public static boolean hasPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void askPermissions(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION
                },
                REQUEST_CODE
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowController.goFullScreen(this);
        setContentView(R.layout.activity_permissions);

        if (hasPermissions(this)) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (hasPermissions(this)) {
                finish();
            }
            else {
                Printer.okDialog(this, getString(R.string.permissions), getString(R.string.permissions_warning));
            }
        }
    }

    public void givePermissions(View view) {
        Permissions.askPermissions(this);
    }

    public void exit(View view) {
        finishAffinity();
    }
}
