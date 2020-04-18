package com.daniel.reportes.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.user.GetUser;
import com.daniel.reportes.task.user.LoginUser;
import com.daniel.reportes.ui.signup.SignUp;
import com.daniel.reportes.utils.GoogleAccountHelper;
import com.daniel.reportes.utils.PreferencesHelper;
import com.daniel.reportes.utils.Printer;
import com.daniel.reportes.utils.TextMonitor;
import com.daniel.reportes.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends AppCompatActivity {

    public static int REQUEST_CODE = 2;

    //Objects
    private AppSession appSession;

    // Widgets
    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    // Class
    private TaskListener<AppSession> loginListener = new TaskListener<AppSession>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(Login.this, this.exception.getMessage());
                return false;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidgets();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAccountHelper.REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                TaskListener<User> userListener = new TaskListener<User>() {
                    @Override
                    public boolean success() {
                        if (this.exception != null) {
                            if (this.exception.getCode() == 409) {
                                return true;
                            }
                            else {
                                Printer.toast(Login.this, this.exception.getMessage());
                                return false;
                            }
                        }
                        return true;
                    }
                };

                if (account != null) {
                    appSession = GoogleAccountHelper.register(account, userListener, loginListener);
                    finishLogin(true);
                }
            }
            catch (ApiException e) {
                Log.w("Error", "signInResult:failed code = " + e.getStatusCode());
            }
        }
    }

    private void initWidgets() {
        String email = getIntent().getStringExtra("email");

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        loginEmail.addTextChangedListener(new TextMonitor(loginEmail));
        loginEmail.setText(email == null ? "" : email);
    }

    private void finishLogin(boolean googleAccount) {
        PreferencesHelper.getInstance(this).putUser(appSession.getUser(), googleAccount);
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void loginWithEmail(View view) {
        Utils.hideKeyboard(this);

        if (loginEmail.getError() != null) {
            Printer.snackBar(view, "Verifique su email");
            return;
        }

        User user = new User(
                loginEmail.getText().toString(),
                loginPassword.getText().toString(),
                "user"
        );

        try {
            if (new LoginUser("default", loginListener).execute(user).get().success()) {
                appSession = loginListener.getResult();
                finishLogin(false);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loginWithGoogle(View view) {
        Utils.hideKeyboard(this);
        GoogleSignInAccount account = GoogleAccountHelper.isGoogleAccountActive(this);

        if (account != null) {
            appSession = GoogleAccountHelper.login(account, loginListener);
            finishLogin(true);
        }
        else {
            GoogleAccountHelper.showSignIn(this);
        }
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getBaseContext(), SignUp.class);

        startActivity(intent);
    }
}
