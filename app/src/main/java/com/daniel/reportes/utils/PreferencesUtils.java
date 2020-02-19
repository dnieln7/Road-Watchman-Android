package com.daniel.reportes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.daniel.reportes.data.User;

public class PreferencesUtils {

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.daniel.reportes.settings", Context.MODE_PRIVATE);

        sharedPreferences.edit().putInt("id", user.getId()).apply();
        sharedPreferences.edit().putString("username", user.getUsername()).apply();
        sharedPreferences.edit().putString("email", user.getEmail()).apply();
        sharedPreferences.edit().putBoolean("google", user.getGoogleId().length() > 0).apply();
        sharedPreferences.edit().putString("role", user.getRole()).apply();
    }
}
