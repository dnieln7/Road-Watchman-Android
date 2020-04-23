package com.daniel.reportes.ui.app.fragment.settings.appearance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.app.fragment.reportes.background.ReportesService;
import com.daniel.reportes.utils.PreferencesHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class AppearanceActivity extends AppCompatActivity {

    private SwitchMaterial darkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        initWidgets();
        initListeners();
    }


    private void initWidgets() {
        setSupportActionBar(findViewById(R.id.appearance_toolbar));

        darkTheme = findViewById(R.id.appearance_dark_theme);
        darkTheme.setChecked(PreferencesHelper.getInstance(this).isDarkThemeEnabled());
    }

    private void initListeners() {
        darkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> showCloseWarning(isChecked));
    }

    private void showCloseWarning(boolean isChecked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Cambiar tema");
        builder.setMessage("La aplicación se cerrará para aplicar los cambios");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            PreferencesHelper.getInstance(this).saveTheme(isChecked);
            finishAffinity();
        });
        builder.show();
    }
}
