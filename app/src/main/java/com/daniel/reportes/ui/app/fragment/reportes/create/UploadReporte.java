package com.daniel.reportes.ui.app.fragment.reportes.create;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.reporte.PostReporte;
import com.daniel.reportes.utils.Printer;
import com.daniel.reportes.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadReporte extends Fragment {

    // Objects
    private CreateViewModel createViewModel;

    // Widgets
    private View root;

    private TextInputEditText description;
    private ProgressBar status;
    private TextView message;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_upload_reporte, container, false);
        createViewModel = new ViewModelProvider(getActivity()).get(CreateViewModel.class);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        description = root.findViewById(R.id.upload_reporte_description);
        status = root.findViewById(R.id.upload_reporte_status);
        message = root.findViewById(R.id.upload_reporte_message);
    }

    public void upload() {
        Utils.hideKeyboard(getActivity());

        if (this.description.getText() != null) {
            String description = this.description.getText().toString().trim();

            createViewModel.getReporte().observe(getActivity(), reporte -> reporte.setDescription(description));

            status.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);

            uploadPicture();
        }
    }

    private void uploadPicture() {
        status.setProgress(25);
        message.setText("Preparando foto...");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("Reportes/");

        StringBuilder pictureName = new StringBuilder();
        StringBuilder pictureUri = new StringBuilder();

        createViewModel.getPictureName().observe(getActivity(), pictureName::append);
        createViewModel.getPictureUri().observe(getActivity(), pictureUri::append);

        status.setProgress(50);
        message.setText("Subiendo foto...");

        reference.child(pictureName.toString())
                .putFile(Uri.parse(pictureUri.toString()))
                .addOnFailureListener(error -> Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, error))
                .addOnSuccessListener(snapshot -> reference.getDownloadUrl()
                        .addOnCompleteListener(uriTask -> uploadReporte(uriTask.getResult())));
    }

    private void uploadReporte(Uri uri) {
        status.setProgress(75);
        message.setText("Subiendo reporte...");

        Printer.okDialog(getContext(), "PUri", uri.toString());

        createViewModel.getReporte().observe(getActivity(), reporte -> {
            reporte.setPicture(uri == null ? "" : uri.toString());

            try {
                if (new PostReporte(reporteListener).execute(reporte).get().success()) {
                    status.setProgress(100);
                    message.setText("Completado");

                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
            catch (ExecutionException | InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    // Class

    private TaskListener<Reporte> reporteListener = new TaskListener<Reporte>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(UploadReporte.this.getContext(), this.exception.getMessage());
                return false;
            }
            return true;
        }
    };
}
