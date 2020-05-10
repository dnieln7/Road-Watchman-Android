package com.daniel.reportes.ui.app.fragment.settings.appearance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.daniel.reportes.R;
import com.daniel.reportes.utils.LanguageHelper;
import com.daniel.reportes.utils.PreferencesHelper;
import com.daniel.reportes.utils.Printer;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class AppearanceActivity extends AppCompatActivity {

    private SwitchMaterial darkTheme;
    private AppCompatSpinner language;

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
        language = findViewById(R.id.appearance_language);

        darkTheme.setChecked(PreferencesHelper.getInstance(this).isDarkThemeEnabled());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);
        language.setSelection(LanguageHelper.getCurrentLanguagePosition(this));
    }

    private void initListeners() {
        darkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> showCloseWarning(isChecked));
        language.setOnItemSelectedListener(new LanguageListener(this));
    }

    private void showCloseWarning(boolean isChecked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.settings_theme);
        builder.setMessage(R.string.settings_close_warning);
        builder.setPositiveButton(R.string.settings_ok, (dialog, which) -> {
            PreferencesHelper.getInstance(this).saveTheme(isChecked);
            finishAffinity();
        });
        builder.show();
    }

    static class LanguageListener implements AdapterView.OnItemSelectedListener {

        final Activity activity;

        LanguageListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String language = parent.getItemAtPosition(position).toString();
            if (!LanguageHelper.getCurrentLanguage(activity).equals(language)) {
                LanguageHelper.changeLanguage(activity, language);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
