package com.daniel.reportes.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.daniel.reportes.R;

public class SignUp extends AppCompatActivity {

    // Var

    // Objects

    // Widgets
    EditText usernameIn;
    EditText passwordIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    private void initVar() {

    }

    private void initObjects() {

    }

    private void initWidgets() {
        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
    }

    private void initListeners() {

    }
}
