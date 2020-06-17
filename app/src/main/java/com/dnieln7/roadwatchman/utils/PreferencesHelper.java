package com.dnieln7.roadwatchman.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.dnieln7.roadwatchman.data.AppSession;
import com.dnieln7.roadwatchman.data.User;

/**
 * Helper class to modify shared preferences.
 *
 * @author dnieln7
 */
public class PreferencesHelper {

    private static final String DARK_THEME = "DarkTheme";
    private static final String NAME = "com.daniel.reportes.settings";

    private static PreferencesHelper instance;
    private SharedPreferences sharedPreferences;

    private PreferencesHelper(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * Creates a new instance of {@link PreferencesHelper} if there is none.
     *
     * @param activity Activity to crate shared preferences file.
     * @return An instance of {@link PreferencesHelper}
     */
    public static PreferencesHelper getInstance(Activity activity) {
        if (instance == null) {
            return instance = new PreferencesHelper(activity);
        }

        return instance;
    }

    /**
     * Loads dark theme if the property "DarkTheme" is true; loads default theme otherwise.
     */
    public void loadTheme() {
        if (isDarkThemeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Saves the desired theme option.
     *
     * @param darkTheme Enable dark theme
     */
    public void saveTheme(boolean darkTheme) {
        sharedPreferences.edit().putBoolean("DarkTheme", darkTheme).apply();
    }

    /**
     * Checks if the dark theme is enabled
     *
     * @return true if the dark theme is enabled; false otherwise.
     */
    public boolean isDarkThemeEnabled() {
        return sharedPreferences.getBoolean(DARK_THEME, false);
    }

    /**
     * Saves the desired language code.
     *
     * @param languageCode Language code.
     */
    public void saveLanguage(String languageCode) {
        sharedPreferences.edit().putString("Language", languageCode).apply();
    }

    /**
     * Gets the current language code.
     *
     * @return A string with the language code; empty string otherwise.
     */
    public String getLanguage() {
        return sharedPreferences.getString("Language", "");
    }

    /**
     * Checks if there is a user logged in, to get it´s data.
     *
     * @return An {@link AppSession} instance containing the user data; null otherwise.
     */
    public AppSession isUserLoggedIn() {
        AppSession appSession = null;

        if (sharedPreferences.getBoolean("Logged", false)) {
            appSession = new AppSession(
                    "",
                    new User(
                            sharedPreferences.getInt("Id", 0),
                            sharedPreferences.getString("Username", ""),
                            sharedPreferences.getString("Email", ""),
                            sharedPreferences.getString("GoogleId", ""),
                            sharedPreferences.getString("Role", "")
                    )
            );
        }

        return appSession;
    }

    /**
     * Checks the current logged account type.
     *
     * @return True if there is an active google account.
     */
    public boolean isGoogleAccount() {
        return sharedPreferences.getBoolean("GoogleAccount", false);
    }

    /**
     * Checks the current logged account type.
     *
     * @return The email of current user; null if the current logged account is not an email account.
     */
    public String isEmailAccount() {
        String email = "";

        if (sharedPreferences.getBoolean("EmailAccount", false)) {
            email = sharedPreferences.getString("Email", "");
        }

        return email;
    }

    /**
     * Saves the desired user into shared preferences.
     *
     * @param user          The user to be saved.
     * @param googleAccount A boolean indicating if the user uses a google account.
     */
    public void putUser(User user, boolean googleAccount) {

        sharedPreferences.edit().putBoolean("Logged", true).apply();
        sharedPreferences.edit().putBoolean("GoogleAccount", googleAccount).apply();
        sharedPreferences.edit().putBoolean("EmailAccount", !googleAccount).apply();

        sharedPreferences.edit().putInt("Id", user.getId()).apply();
        sharedPreferences.edit().putString("Username", user.getUsername()).apply();
        sharedPreferences.edit().putString("Email", user.getEmail()).apply();
        sharedPreferences.edit().putString("GoogleId", user.getGoogleId()).apply();
        sharedPreferences.edit().putString("Role", user.getRole()).apply();
    }

    /**
     * Deletes all the user data of shared preferences and destroys the current {@link PreferencesHelper} instance.
     */
    public void destroy() {
        sharedPreferences.edit().putBoolean("Logged", false).apply();
        sharedPreferences.edit().putBoolean("GoogleAccount", false).apply();
        sharedPreferences.edit().putBoolean("EmailAccount", false).apply();

        sharedPreferences.edit().putInt("Id", 0).apply();
        sharedPreferences.edit().putString("Username", "").apply();
        sharedPreferences.edit().putString("Email", "").apply();
        sharedPreferences.edit().putString("GoogleId", "").apply();
        sharedPreferences.edit().putString("Role", "").apply();

        saveLanguage("");
        saveTheme(false);

        instance = null;
    }
}