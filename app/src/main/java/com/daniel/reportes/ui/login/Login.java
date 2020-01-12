package com.daniel.reportes.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.reportes.Reportes;

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

        initWidgets();
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
        Intent intent = new Intent(getBaseContext(), Reportes.class);

        startActivity(intent);
    }

    public void forgotPassword(View view) {

        if(usernameIn.getText().toString().trim().equals("")) {
            usernameIn.setError("Requerido!");
        }
        else {



            Fragment mFragment = new ResetPassword();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.forgotPassword, mFragment).commit();
        }
    }

    public void goToSignUp(View view) {

    }
}
