package com.daniel.reportes.ui.app.fragment.reportes.create;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.permission.Permissions;
import com.daniel.reportes.utils.Utils;
import com.google.android.material.button.MaterialButton;
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

    private MaterialButton selectPicture;
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
            if (data != null) {
                pictureUri = data.getData();
                Picasso.with(getContext()).load(pictureUri).placeholder(R.drawable.reportes).into(selectedPicture);
                createViewModel.setPictureUri(pictureUri);
                createViewModel.setPictureName(pictureName);
            }
            else if (pictureUri != null) {
                Picasso.with(getContext()).load(pictureUri).placeholder(R.drawable.reportes).into(selectedPicture);
                createViewModel.setPictureUri(pictureUri);
                createViewModel.setPictureName(pictureName);
            }

            try {
                createViewModel.setPicture(MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), pictureUri) != null);
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
        selectPicture = root.findViewById(R.id.selectPicture);
        selectedPicture = root.findViewById(R.id.selectedPicture);
    }

    private void initListeners() {
        selectPicture.setOnClickListener(v -> selectPicture());
    }

    private void selectPicture() {
        if (Permissions.hasPermissions(getContext())) {

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
            Permissions.askPermissions(getActivity());
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
}
