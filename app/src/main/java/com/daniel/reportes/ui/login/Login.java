package com.daniel.reportes.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.reportes.R;

public class Login extends AppCompatActivity {

    // Var

    // Objects

    // Widgets
    EditText usernameIn;
    EditText passwordIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void login(View view) {

    }

    public void loginFake(View view) {

    }
}
