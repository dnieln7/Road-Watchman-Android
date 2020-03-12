package com.daniel.reportes.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TextMonitor implements TextWatcher {

    private TextInputEditText container;

    public TextMonitor(TextInputEditText container) {
        this.container = container;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!validateEmail(s.toString())) {
            container.setError("El email no es válido");
        }
        System.out.println(container.getError());
    }

    private boolean validateEmail(String email) {
        if (email.trim().isEmpty()) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
