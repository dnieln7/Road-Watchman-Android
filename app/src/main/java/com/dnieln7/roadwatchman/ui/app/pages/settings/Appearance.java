package com.dnieln7.roadwatchman.ui.app.pages.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.ThemeHelper;

/**
 * Controls app's appearance.
 *
 * @author dnieln7
 */
public class Appearance extends Fragment {

    private View root;

    private AppCompatSpinner theme;

    public Appearance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_appearance, container, false);

        initWidgets();

        return root;
    }

    private void initWidgets() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.themes, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theme = root.findViewById(R.id.appearance_theme);
        theme.setAdapter(adapter);
        theme.setSelection(ThemeHelper.getCurrentTheme());
        theme.setOnItemSelectedListener(new ThemeListener(requireActivity()));
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
            // Not required
        }
    }
}
