package com.daniel.reportes.ui.signup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.reportes.R;
import com.daniel.reportes.ui.other.IFragmentListener;

public class SignUp extends AppCompatActivity implements IFragmentListener {

    //Fragments
    Fragment signUp;
    Fragment verifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initFragments();
        showFragment(1);
    }

    @Override
    public void showFragment(int fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (fragment) {
            case 1:
                fragmentManager.beginTransaction().replace(R.id.forgotPassword, signUp).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.forgotPassword, verifyPassword).commit();
                break;
        }
    }

    @Override
    public void exit() {
        finish();
    }

    private void initFragments() {
        signUp = new SignUpForm(this);
        verifyPassword = new Verify("1", this);
    }
}
