package com.dnieln7.roadwatchman.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dnieln7.roadwatchman.R;

import java.util.Random;

public class Utils {

    public static final int SELECT_PICTURE = 3;

    public static String generateCode() {
        return String.valueOf(new Random().nextInt(1000));
    }

    public static int GetPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);

        typedArray.recycle();

        return color;
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
