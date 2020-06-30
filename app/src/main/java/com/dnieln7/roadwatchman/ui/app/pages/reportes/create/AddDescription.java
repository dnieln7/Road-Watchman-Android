package com.dnieln7.roadwatchman.ui.app.pages.reportes.create;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.utils.WindowController;
import com.dnieln7.roadwatchman.work.report.ReportWorkManager;
import com.google.android.material.textfield.TextInputEditText;

public class AddDescription extends Fragment {

    // Objects
    private CreateViewModel createViewModel;

    // Widgets
    private View root;
    private TextInputEditText description;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_description, container, false);
        createViewModel = new ViewModelProvider(getActivity()).get(CreateViewModel.class);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        description = root.findViewById(R.id.upload_reporte_description);
    }

    void upload() {
        WindowController.hideKeyboard(getActivity());

        String description = this.description.getText() != null ? this.description.getText().toString().trim() : "";
        createViewModel.getReporte().observe(getActivity(), reporte -> reporte.setDescription(description));

        ReportWorkManager reportWorkManager = new ReportWorkManager();

        StringBuilder pictureName = new StringBuilder();
        StringBuilder pictureUri = new StringBuilder();

        createViewModel.getPictureName().observe(getActivity(), pictureName::append);
        createViewModel.getPictureUri().observe(getActivity(), pictureUri::append);

        createViewModel.getReporte().observe(getActivity(), reporte -> {
            reportWorkManager.prepareUploadWork(reporte, pictureUri.toString(), pictureName.toString());
            reportWorkManager.startWork(getContext());
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        });
    }
}
