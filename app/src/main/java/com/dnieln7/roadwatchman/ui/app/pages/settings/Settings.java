package com.dnieln7.roadwatchman.ui.app.pages.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.ui.app.AppActivity;

public class Settings extends Fragment {

    private View root;

    private LinearLayout appearance;
    private LinearLayout network;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        appearance = root.findViewById(R.id.settings_appearance);
        network = root.findViewById(R.id.settings_network);

        appearance.setOnClickListener(v ->
                AppActivity.navTo(v, SettingsDirections.actionSettingsToAppearance())
        );
        network.setOnClickListener(v ->
                AppActivity.navTo(v, SettingsDirections.actionSettingsToNetwork())
        );
    }
}