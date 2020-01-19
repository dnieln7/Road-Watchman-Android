package com.daniel.reportes.ui.app.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.ui.app.fragment.AppViewModel;

public class ProfileFragment extends Fragment {

    // Var

    // Objects
    private View root;
    private AppViewModel appViewModel;
    private AppSession appSession;

    // Widgets

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        initVar();
        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void initVar() {

    }

    private void initObjects() {
        appViewModel.getAppSession().observe(this, session -> appSession = session);
    }

    private void initWidgets() {

    }

    private void initListeners() {

    }
}