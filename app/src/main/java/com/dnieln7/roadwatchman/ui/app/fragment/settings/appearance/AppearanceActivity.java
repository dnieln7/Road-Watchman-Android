package com.dnieln7.roadwatchman.ui.app.fragment.settings.appearance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatSpinner;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.utils.LanguageHelper;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.ThemeHelper;

public class AppearanceActivity extends AppCompatActivity {

    private AppCompatSpinner theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        initWidgets();
    }

    private void initWidgets() {
        setSupportActionBar(findViewById(R.id.appearance_toolbar));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.themes, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theme = findViewById(R.id.appearance_theme);
        theme.setAdapter(adapter);
        theme.setSelection(ThemeHelper.getCurrentTheme());
        theme.setOnItemSelectedListener(new ThemeListener(this));
    }

    static class ThemeListener implements AdapterView.OnItemSelectedListener {
        final Activity activity;

        ThemeListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int mode;

            switch (position) {
                case 1:
                    mode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
                    break;
                case 2:
                    mode = AppCompatDelegate.MODE_NIGHT_YES;
                    break;
                case 3:
                    mode = AppCompatDelegate.MODE_NIGHT_NO;
                    break;
                default:
                    mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }

            PreferencesHelper.getInstance(activity).saveTheme(mode);
            ThemeHelper.setTheme(mode);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
