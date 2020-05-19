package com.dnieln7.roadwatchman.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.dnieln7.roadwatchman.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Helper class to print messages to the UI
 *
 * @author dnieln7
 */
public class Printer {

    /**
     * Displays a {@link Toast} for short period of time.
     *
     * @param context Activity context.
     * @param message Message to be displayed.
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a {@link Snackbar} for short period of time.
     *
     * @param view    The view in witch to show the message.
     * @param message Message to be displayed.
     */
    public static void snackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Displays a dialog with a ok button.
     *
     * @param context Activity context
     * @param title   Dialog header.
     * @param message Dialog body.
     */
    public static void okDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        });
        builder.show();
    }

    /**
     * Creates an {@link AlertDialog} with a circular progress indicator.
     *
     * @param activity The activity to inflate the layout.
     * @param title    Dialog header.
     * @param message  Dialog body.
     * @return An instance of {@link AlertDialog}
     */
    public static AlertDialog progressDialog(Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_progress, null);

        ((TextView) view.findViewById(R.id.dialog_header)).setText(title);
        ((TextView) view.findViewById(R.id.dialog_body)).setText(message);

        return builder.setCancelable(false).setView(view).create();
    }

    /**
     * Creates an {@link AlertDialog} with a horizontal progress indicator.
     *
     * @param activity   The activity to inflate the layout.
     * @param title      Dialog header.
     * @param message    Dialog body.
     * @param percentage Show percentage progress indicator
     * @param numeric    Show numeric progress
     * @return An instance of {@link AlertDialog}
     */
    public static AlertDialog progressDialog(Activity activity, String title, String message, boolean percentage, boolean numeric) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_progress_horizontal, null);

        ((TextView) view.findViewById(R.id.dialog_header)).setText(title);
        ((TextView) view.findViewById(R.id.dialog_body)).setText(message);
        ((TextView) view.findViewById(R.id.dialog_progress_percentage)).setVisibility(percentage ? View.VISIBLE : View.GONE);
        ((TextView) view.findViewById(R.id.dialog_progress_numeric)).setVisibility(numeric ? View.VISIBLE : View.GONE);


        return builder.setCancelable(false).setView(view).create();
    }
}
