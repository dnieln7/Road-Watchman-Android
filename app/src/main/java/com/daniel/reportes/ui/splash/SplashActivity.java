package com.daniel.reportes.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.ui.app.AppActivity;
import com.daniel.reportes.ui.login.Login;
import com.daniel.reportes.ui.permission.Permissions;
import com.daniel.reportes.utils.GoogleAccountHelper;
import com.daniel.reportes.utils.PreferencesHelper;
import com.daniel.reportes.utils.Printer;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashActivity extends AppCompatActivity {

    AppSession appSession;

    private TaskListener<AppSession> loginListener = new TaskListener<AppSession>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(SplashActivity.this, this.exception.getMessage());
                return false;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Permissions.hasPermissions(this)) {
            Intent permissionsIntent = new Intent(this, Permissions.class);
            startActivityForResult(permissionsIntent, Permissions.REQUEST_CODE);
        }
        else {
            loadPreferences();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Permissions.REQUEST_CODE) {
            loadPreferences();
            finish();
        }
    }

    private void loadPreferences() {
        PreferencesHelper helper = PreferencesHelper.getInstance(this);
        helper.loadTheme();

        appSession = helper.isUserLoggedIn();

        if (appSession == null) {
            if (helper.isGoogleAccount()) {
                loginWithGoogle();
            }
            else {
                Intent loginIntent = new Intent(this, Login.class);
                loginIntent.putExtra("email", helper.isEmailAccount());
                startActivity(loginIntent);
            }
        }
        else {
            Intent appIntent = new Intent(this, AppActivity.class);
            appIntent.putExtra("session", appSession);
            startActivity(appIntent);
        }
    }

    private void loginWithGoogle() {
        GoogleSignInAccount account = GoogleAccountHelper.isGoogleAccountActive(this);

        if (account != null) {
            appSession = GoogleAccountHelper.login(account, loginListener);
            PreferencesHelper.getInstance(this).putUser(appSession.getUser(), true);

            Intent appIntent = new Intent(this, AppActivity.class);
            appIntent.putExtra("session", appSession);
            startActivity(appIntent);
        }
    }
}
