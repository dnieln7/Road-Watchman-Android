package com.daniel.reportes.ui.app.fragment.reportes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.utils.LocationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateReporteStep1 extends Fragment {

    // Objects
    private boolean hasPermissions;
    private ReporteViewModel reporteViewModel;
    private String pictureName;
    private Uri pictureUri;
    private Bitmap pictureBitmap;

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

            try {
                pictureBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), pictureUri);
            }
            catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
        if (pictureBitmap != null) {
            Location location = LocationUtils.getGPS(getActivity());

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
                Snackbar.make(root, "Calculando ubicación...", Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            Snackbar.make(root, "Debe seleccionar una foto", Snackbar.LENGTH_SHORT).show();
        }
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
