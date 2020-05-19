package com.dnieln7.roadwatchman.ui.app.fragment.reportes.create;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.Reporte;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.roadwatchman.task.reporte.PostReporte;
import com.dnieln7.roadwatchman.utils.Printer;
import com.dnieln7.roadwatchman.utils.Utils;
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
    private AlertDialog progressDialog;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_upload_reporte, container, false);
        createViewModel = new ViewModelProvider(getActivity()).get(CreateViewModel.class);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        description = root.findViewById(R.id.upload_reporte_description);
        progressDialog = Printer.progressDialog(getActivity(), getString(R.string.upload_report_info), getString(R.string.upload_report_message));
    }

    void upload() {
        Utils.hideKeyboard(getActivity());

        progressDialog.show();
        String description = this.description.getText() != null ? this.description.getText().toString().trim() : "";
        createViewModel.getReporte().observe(getActivity(), reporte -> reporte.setDescription(description));

        uploadPicture();
    }

    private void uploadPicture() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("Reports/");

        StringBuilder pictureName = new StringBuilder();
        StringBuilder pictureUri = new StringBuilder();

        createViewModel.getPictureName().observe(getActivity(), pictureName::append);
        createViewModel.getPictureUri().observe(getActivity(), pictureUri::append);

        reference.child(pictureName.toString())
                .putFile(Uri.parse(pictureUri.toString()))
                .addOnFailureListener(error -> Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, error))
                .addOnSuccessListener(snapshot -> reference.child(pictureName.toString()).getDownloadUrl()
                        .addOnCompleteListener(task -> uploadReporte(task.getResult()))
                );
    }

    private void uploadReporte(Uri uri) {
        createViewModel.getReporte().observe(getActivity(), reporte -> {
            reporte.setPicture(uri == null ? "" : uri.toString());

            try {
                if (new PostReporte(reporteListener).execute(reporte).get().success()) {
                    progressDialog.dismiss();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
            catch (ExecutionException | InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        });

        progressDialog.dismiss();
    }
}
