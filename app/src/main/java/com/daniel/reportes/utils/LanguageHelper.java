package com.daniel.reportes.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AlertDialog;

import com.daniel.reportes.R;

import java.util.Locale;

/**
 * Helper class to manage languages.
 *
 * @author dnieln7
 */
public class LanguageHelper {
    /**
     * Gets the current language.
     *
     * @param activity Activity to obtain an {@link PreferencesHelper} instance.
     * @return The current language in a readable form; empty string otherwise.
     */
    public static String getCurrentLanguage(Activity activity) {
        String languageCode = PreferencesHelper.getInstance(activity).getLanguage();
        String language = "";

        switch (languageCode) {
            case "es":
                language = "Español";
                break;
            case "en":
                language = "English";
                break;
        }

        return language;
    }

    /**
     * Gets the current language.
     *
     * @param activity Activity to obtain an {@link PreferencesHelper} instance.
     * @return The current language array position matching array at resources; 0 otherwise.
     */
    public static int getCurrentLanguagePosition(Activity activity) {
        String languageCode = PreferencesHelper.getInstance(activity).getLanguage();
        int language = 0;

        switch (languageCode) {
            case "es":
                language = 1;
                break;
            case "en":
                language = 2;
                break;
        }

        return language;
    }

    /**
     * Loads the current  language setting.
     *
     * @param activity Activity to obtain an {@link PreferencesHelper} instance.
     */
    public static void loadLanguage(Activity activity) {
        String languageCode = PreferencesHelper.getInstance(activity).getLanguage();

        if (!languageCode.equals("")) {
            Locale locale = new Locale(languageCode);
            Resources resources = activity.getResources();
            Configuration configuration = resources.getConfiguration();

            Locale.setDefault(locale);
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }

    /**
     * Saves the current language as a code.
     *
     * @param activity Activity to obtain an {@link PreferencesHelper} instance.
     */
    public static void changeLanguage(Activity activity, String language) {
        String languageCode = "";

        switch (language) {
            case "Español":
                languageCode = "es";
                break;
            case "English":
                languageCode = "en";
                break;
            default:
                return;
        }

        Locale locale = new Locale(languageCode);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();

        Locale.setDefault(locale);
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        PreferencesHelper.getInstance(activity).saveLanguage(languageCode);

        new AlertDialog.Builder(activity).setTitle(R.string.settings_language)
                .setMessage(R.string.settings_close_warning)
                .setPositiveButton(R.string.settings_ok, (dialog, which) -> activity.finishAffinity())
                .show();
    }
}
