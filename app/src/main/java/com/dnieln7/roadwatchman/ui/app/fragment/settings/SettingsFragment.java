package com.dnieln7.roadwatchman.ui.app.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.ui.app.fragment.settings.appearance.AppearanceActivity;
import com.dnieln7.roadwatchman.ui.app.fragment.settings.network.NetworkActivity;

public class SettingsFragment extends Fragment {

    private View root;

    private LinearLayout appearance;
    private LinearLayout network;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initWidgets();
        initListeners();

        return root;
    }

    private void initWidgets() {

        appearance = root.findViewById(R.id.settings_appearance);
        network = root.findViewById(R.id.settings_network);
    }

    private void initListeners() {
        appearance.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AppearanceActivity.class))
        );

        network.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), NetworkActivity.class))
        );
    }
}