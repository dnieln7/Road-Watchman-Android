package com.dnieln7.roadwatchman.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

public class TextMonitor implements TextWatcher {

    private TextInputEditText container;
    private String textError;

    public TextMonitor(TextInputEditText container, String textError) {
        this.container = container;
        this.textError = textError;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not required
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Not required
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!validateEmail(s.toString())) {
            container.setError(textError);
        }
    }

    private boolean validateEmail(String email) {
        if (email.trim().isEmpty()) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
