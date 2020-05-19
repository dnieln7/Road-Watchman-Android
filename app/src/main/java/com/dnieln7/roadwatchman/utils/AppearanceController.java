package com.dnieln7.roadwatchman.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Helper class to control app´s appearance.
 */
public class AppearanceController {

    /**
     * Goes full screen by hiding the status bar. Needs to be called before Activity.setContentView
     *
     * @param activity - The app activity
     */
    public static void goFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }
}
