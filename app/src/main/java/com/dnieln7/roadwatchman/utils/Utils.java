package com.dnieln7.roadwatchman.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static void logError(Class clazz, Throwable error) {
        Logger.getLogger(clazz.getName()).log(
                Level.SEVERE,
                "-------------------------Something went wrong!-------------------------",
                error
        );
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        View currentFocusedView = activity.getCurrentFocus();

        if (currentFocusedView != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
