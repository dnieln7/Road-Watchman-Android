package com.daniel.reportes.ui.app.fragment.reportes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.Reporte;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.io.File;

public class CreateReporteStep1 extends Fragment {

    // Objects
    private boolean hasPermissions;
    private ReporteViewModel reporteViewModel;
    private String pictureName;
    private Uri pictureUri;

    // Widgets
    private View root;
    private MaterialButton selectPicture;
    private MaterialButton toStep2;
    private ImageView selectedPicture;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_create_reporte_step_1, container, false);
        reporteViewModel = ViewModelProviders.of(getActivity()).get(ReporteViewModel.class);

        checkPermissions(getActivity());
        initWidgets();
        initListeners();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Utils.SELECT_PICTURE) {
            if (data != null) {
                pictureUri = data.getData();
                Picasso.with(getContext()).load(pictureUri).placeholder(R.drawable.reportes_logo).into(selectedPicture);
                reporteViewModel.setPictureUri(pictureUri);
                reporteViewModel.setPictureName(pictureName);
            }
            else if (pictureUri != null) {
                Picasso.with(getContext()).load(pictureUri).placeholder(R.drawable.reportes_logo).into(selectedPicture);
                reporteViewModel.setPictureUri(pictureUri);
                reporteViewModel.setPictureName(pictureName);
            }

            pictureUri = null;
            pictureName = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            checkPermissions(getActivity());
        }
    }

    private void initWidgets() {
        selectPicture = root.findViewById(R.id.selectPicture);
        toStep2 = root.findViewById(R.id.toStep2);
        selectedPicture = root.findViewById(R.id.selectedPicture);
    }

    private void initListeners() {
        selectPicture.setOnClickListener(v -> selectPicture());
        toStep2.setOnClickListener(v -> toStep2());
    }

    private void selectPicture() {
        if (hasPermissions) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getUri());

            Intent intentContent = new Intent(Intent.ACTION_GET_CONTENT);
            intentContent.setType("image/*");

            Intent intentPick = new Intent(Intent.ACTION_PICK);
            intentPick.setType("image/*");

            Intent intentChooser = Intent.createChooser(intentContent, "Selecciona una imagen");
            intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentPick, cameraIntent});

            startActivityForResult(intentChooser, Utils.SELECT_PICTURE);
        }
        else {
            askPermissions(getActivity());
        }
    }

    private void toStep2() {

        reporteViewModel.getPictureName().observe(getActivity(), s -> {
            if (s != null) {
                Location location = getLocation();

                if (location != null) {

                    Reporte reporte = new Reporte(
                            new LocalDateTime().toString(),
                            new double[]{location.getLatitude(), location.getLongitude()}
                    );

                    reporteViewModel.setReporte(reporte);

                    CreateReporteStep2 step2 = new CreateReporteStep2();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, step2, step2.getTag())
                            .commit();
                }
                else {
                    Snackbar.make(root, "Debe seleccionar una foto", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Uri getUri() {

        return pictureUri = Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                "Reportes"
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Reportes", "failed to create directory");
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + getPictureName());
    }

    private String getPictureName() {
        return pictureName = "reporte_" + new LocalDateTime() + ".jpg";
    }

    @SuppressLint("MissingPermission")
    private Location getLocation() {
        if (hasPermissions) {

            if (isLocationEnabled()) {

                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                Location gps = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location network = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (gps != null) {
                    return gps;
                }
                else if (network != null) {
                    return network;
                }
                else {
                    return null;
                }
            }
            else {
                Toast.makeText(getContext(), "Por favor activa la ubicación", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
        else {
            askPermissions(getActivity());
        }

        return null;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void checkPermissions(Activity activity) {
        hasPermissions = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermissions(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                101
        );
    }
}
