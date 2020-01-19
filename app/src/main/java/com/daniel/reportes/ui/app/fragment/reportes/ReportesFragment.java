package com.daniel.reportes.ui.app.fragment.reportes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReportesFragment extends Fragment {

    private boolean expanded;

    // Objects
    private View root;
    private AppViewModel appViewModel;
    private AppSession appSession;

    // Widgets
    private TextView withPictureLabel;
    private TextView withoutPictureLabel;

    private FloatingActionButton newReporte;
    private FloatingActionButton withPicture;
    private FloatingActionButton withoutPicture;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reportes, container, false);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        if(appViewModel.getAppSession() == null) {
            appViewModel.setAppSession(new MutableLiveData<>());
            appViewModel.setAppSessionValue((AppSession) getActivity().getIntent().getSerializableExtra("session"));
        }

        init();
        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void init() {
        expanded = false;
    }

    private void initObjects() {
        appViewModel.getAppSession().observe(this, session -> appSession = session);
    }

    private void initWidgets() {
        withPictureLabel = root.findViewById(R.id.withPictureLabel);
        withoutPictureLabel = root.findViewById(R.id.withoutPictureLabel);

        newReporte = root.findViewById(R.id.newReporte);
        withPicture = root.findViewById(R.id.withPicture);
        withoutPicture = root.findViewById(R.id.withoutPicture);
    }

    private void initListeners() {
        newReporte.setOnClickListener(v -> expandMenu());
    }

    public void expandMenu() {

        if (expanded) {
            newReporte.animate().rotationBy(-180);
            withPicture.animate().translationY(0);
            withoutPicture.animate().translationY(0);
            withPictureLabel.animate().translationY(0);
            withoutPictureLabel.animate().translationY(0);

            withPicture.hide();
            withoutPicture.hide();
            withPictureLabel.setVisibility(View.INVISIBLE);
            withoutPictureLabel.setVisibility(View.INVISIBLE);

            expanded = false;
        }
        else {
            newReporte.animate().rotationBy(180);
            withPicture.animate().translationY(-150);
            withoutPicture.animate().translationY(-300);
            withPictureLabel.animate().translationY(-150);
            withoutPictureLabel.animate().translationY(-300);

            withPicture.show();
            withoutPicture.show();
            withPictureLabel.setVisibility(View.VISIBLE);
            withoutPictureLabel.setVisibility(View.VISIBLE);

            expanded = true;
        }
    }
}