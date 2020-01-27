package com.daniel.reportes.ui.permission;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.daniel.reportes.R;
import com.google.android.material.button.MaterialButton;

public class AskPermission extends Fragment {

    // Widgets
    private View root;
    private MaterialButton askPermission;
    private MaterialButton exit;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ask_permissions, container, false);

        initWidgets();
        initListeners();

        return root;
    }

    private void initWidgets() {
        askPermission = root.findViewById(R.id.askPermission);
        exit = root.findViewById(R.id.exit);
    }

    private void initListeners() {
        askPermission.setOnClickListener(v -> askPermissions());
        exit.setOnClickListener(v -> exit());
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                101
        );
    }

    private void exit() {
        getActivity().finish();
        System.exit(0);
    }
}
