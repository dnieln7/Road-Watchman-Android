package com.dnieln7.roadwatchman.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.data.model.AuthResponse;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.dnieln7.roadwatchman.ui.app.AppActivity;
import com.dnieln7.roadwatchman.ui.login.Login;
import com.dnieln7.roadwatchman.ui.permission.Permissions;
import com.dnieln7.roadwatchman.utils.GoogleAccountHelper;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashActivity extends AppCompatActivity implements ITaskListener<AuthResponse> {

    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Permissions.hasPermissions(this)) {
            Intent permissionsIntent = new Intent(this, Permissions.class);
            startActivityForResult(permissionsIntent, Permissions.REQUEST_CODE);
        }
        else {
            loadPreferences();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Permissions.REQUEST_CODE) {
            loadPreferences();
        }
        else if (requestCode == Login.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadPreferences();
            }
            else {
                finish();
            }
        }
    }

    private void openApp() {
        Intent appIntent = new Intent(this, AppActivity.class);
        appIntent.putExtra("session", user);
        startActivity(appIntent);
        finish();
    }

    private void loadPreferences() {
        PreferencesHelper helper = PreferencesHelper.getInstance(this);
        helper.loadTheme();

        user = helper.isUserLoggedIn();

        if (user == null) {
            if (helper.isGoogleAccount()) {
                loginWithGoogle();
            }
            else {
                Intent loginIntent = new Intent(this, Login.class);
                loginIntent.putExtra("email_address", helper.isEmailAccount());
                startActivityForResult(loginIntent, Login.REQUEST_CODE);
            }
        }
        else {
            openApp();
        }
    }

    private void loginWithGoogle() {
        GoogleSignInAccount account = GoogleAccountHelper.isGoogleAccountActive(this);
        if (account != null) {
            GoogleAccountHelper.login(account, this);
        }
    }

    @Override
    public void onSuccess(AuthResponse object) {
        PreferencesHelper.getInstance(this).putUser(object.getResult(), true);
        openApp();
    }
}
