package com.daniel.reportes.ui.app.fragment.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.daniel.reportes.R;

public class SettingsFragment extends Fragment {

    // Objects
    private SharedPreferences sharedPreferences;

    // Widgets
    private View root;
    private Switch darkTheme;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void initObjects() {
        sharedPreferences = getActivity().getSharedPreferences("com.daniel.reportes.settings", Context.MODE_PRIVATE);

    }

    private void initWidgets() {
        darkTheme = root.findViewById(R.id.darkTheme);
        darkTheme.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void initListeners() {
        darkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferences.edit().putBoolean("DarkTheme", true).apply();
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferences.edit().putBoolean("DarkTheme", false).apply();
            }
        });
    }
}