package com.daniel.reportes.ui.app.fragment.reportes;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.reporte.PostReporte;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.dnieln7.httprequest.exception.ResponseException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateReporteStep2 extends Fragment {

    // Objects
    private ReporteViewModel reporteViewModel;
    private AppViewModel appViewModel;

    private AppSession appSession;

    private Reporte reporte;
    private Uri pictureUri;
    private String pictureName;

    // Widgets
    private View root;
    private TextInputEditText description;
    private MaterialButton upload;
    private ProgressBar uploadStatus;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_create_reporte_step_2, container, false);
        reporteViewModel = ViewModelProviders.of(getActivity()).get(ReporteViewModel.class);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void initObjects() {
        appViewModel.getAppSession().observe(getActivity(), appSession -> this.appSession = appSession);

        reporteViewModel.getReporte().observe(getActivity(), reporte -> this.reporte = reporte);
        reporteViewModel.getPictureUri().observe(getActivity(), pictureUri -> this.pictureUri = pictureUri);
        reporteViewModel.getPictureName().observe(getActivity(), pictureName -> this.pictureName = pictureName);
    }

    private void initWidgets() {
        description = root.findViewById(R.id.description);
        upload = root.findViewById(R.id.upload);
        uploadStatus = root.findViewById(R.id.uploadStatus);
    }

    private void initListeners() {
        upload.setOnClickListener(v -> upload());
    }

    private void upload() {
        if (!description.getText().toString().trim().equals("")) {
            reporte.setDescription(description.getText().toString());
        }

        reporte.setUserId(appSession.getUser().getId());
        uploadStatus.setVisibility(View.VISIBLE);

        Utils.hideKeyboard(getActivity());
        postReporte();
    }

    private void postReporte() {
        uploadStatus.setProgress(25);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage
                .getReference("Reportes/")
                .child(appSession.getUser().getUsername() + "_" + pictureName);

        uploadStatus.setProgress(50);

        reference.putFile(pictureUri)
                .addOnFailureListener(error -> Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, error))
                .addOnSuccessListener(snapshot -> reference.getDownloadUrl()
                        .addOnCompleteListener(uriTask -> postReport(uriTask.getResult()))
                );
    }

    private void postReport(Uri uri) {
        uploadStatus.setProgress(75);

        String pictureUrl = uri == null ? "" : uri.toString();

        try {
            if (new PostReporte(pictureUrl, reporteListener).execute(reporte).get().success()) {
                uploadStatus.setProgress(100);
                uploadStatus.setVisibility(View.GONE);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    // Class

    private TaskListener<Reporte> reporteListener = new TaskListener<Reporte>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Utils.toast(CreateReporteStep2.this.getContext(), this.exception.getMessage());
                return false;
            }
            return true;
        }
    };
}
