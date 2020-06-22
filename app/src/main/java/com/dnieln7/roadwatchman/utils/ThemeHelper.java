package com.dnieln7.roadwatchman.utils;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Helper class to manage app themes
 *
 * @author dnieln7
 */
public class ThemeHelper {

    /**
     * Applies the provided theme mode.
     *
     * @param mode Theme mode to be applied, must be a constant of {@link AppCompatDelegate}.
     */
    public static void setTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    /**
     * @return An integer describing the current theme mode code, see {@link AppCompatDelegate#getDefaultNightMode()}.
     */
    public static int getCurrentTheme() {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY:
                return 1;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return 2;
            case AppCompatDelegate.MODE_NIGHT_NO:
                return 3;
            default:
                return 0;
        }
    }
}
