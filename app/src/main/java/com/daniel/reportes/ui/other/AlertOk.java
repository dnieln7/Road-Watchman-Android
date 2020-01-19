package com.daniel.reportes.ui.other;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertOk extends DialogFragment {

    private String message;

    public AlertOk(String message) {
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setNeutralButton("OK", (dialog, which) -> {});
        return builder.create();
    }
}
