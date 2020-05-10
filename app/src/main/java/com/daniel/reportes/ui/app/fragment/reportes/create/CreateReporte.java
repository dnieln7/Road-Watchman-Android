package com.daniel.reportes.ui.app.fragment.reportes.create;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.permission.Permissions;
import com.daniel.reportes.utils.Utils;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateReporte extends Fragment {

    // Objects
    private CreateViewModel createViewModel;
    private String pictureName;
    private Uri pictureUri;

    // Widgets
    private View root;
    private ImageView selectedPicture;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_create_reporte, container, false);
        createViewModel = new ViewModelProvider(getActivity()).get(CreateViewModel.class);

        initWidgets();
        initListeners();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Utils.SELECT_PICTURE) {

            if (data != null && data.getData() != null) {
                pictureUri = data.getData();
                Picasso.get().load(pictureUri).error(R.drawable.reportes).into(selectedPicture);
                createViewModel.setPictureUri(pictureUri);
                createViewModel.setPictureName(pictureName);
            }
            else {
                if (pictureUri != null) {
                    Picasso.get().load(pictureUri).error(R.drawable.reportes).into(selectedPicture);
                    createViewModel.setPictureUri(pictureUri);
                    createViewModel.setPictureName(pictureName);
                }
            }

            try {
                if (pictureUri != null) {
                    createViewModel.setPicture(MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), pictureUri) != null);
                }
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
        if (requestCode == Permissions.REQUEST_CODE) {
            if (Permissions.hasPermissions(getContext())) {
                selectPicture();
            }
        }
    }

    private void initWidgets() {
        selectedPicture = root.findViewById(R.id.selectedPicture);
    }

    private void initListeners() {
        selectedPicture.setOnClickListener(v -> selectPicture());
    }

    private void selectPicture() {
        if (Permissions.hasPermissions(getContext())) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createPictureUri());

            Intent intentContent = new Intent(Intent.ACTION_GET_CONTENT);
            intentContent.setType("image/*");

            Intent intentPick = new Intent(Intent.ACTION_PICK);
            intentPick.setType("image/*");

            Intent intentChooser = Intent.createChooser(intentContent, getString(R.string.create_report_select));
            intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentPick, cameraIntent});

            startActivityForResult(intentChooser, Utils.SELECT_PICTURE);
        }
        else {
            Permissions.askPermissions(getActivity());
        }
    }

    private Uri createPictureUri() {
        pictureName = "reporte_" + new LocalDateTime() + ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentResolver resolver = getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();

            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pictureName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + getString(R.string.app_name) + "/");

            pictureUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        else {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM
                    ),
                    "Reportes"
            );

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Logger.getLogger(CreateReporte.class.getName()).log(Level.SEVERE, "Failed to create directory");
                    pictureUri = null;
                }
            }

            pictureUri = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + pictureName));
        }

        return pictureUri;
    }
}
