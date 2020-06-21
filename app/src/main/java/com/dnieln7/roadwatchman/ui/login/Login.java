package com.dnieln7.roadwatchman.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.AppSession;
import com.dnieln7.roadwatchman.data.User;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.roadwatchman.task.user.LoginUser;
import com.dnieln7.roadwatchman.ui.signup.SignUp;
import com.dnieln7.roadwatchman.utils.GoogleAccountHelper;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.Printer;
import com.dnieln7.roadwatchman.utils.TextMonitor;
import com.dnieln7.roadwatchman.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    public static int REQUEST_CODE = 2;

    //Objects
    private AppSession appSession;
    private AlertDialog progressDialog;

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
                Utils.logError(Login.class, e);
                progressDialog.dismiss();
            }
        }
    }

    private void initWidgets() {
        String email = getIntent().getStringExtra("email_address");

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        loginEmail.addTextChangedListener(new TextMonitor(loginEmail, getString(R.string.login_wrong_email)));
        loginEmail.setText(email == null ? "" : email);

        progressDialog = Printer.progressDialog(
                this,
                getString(R.string.please_wait),
                getString(R.string.login_loading_message)
        );
    }

    public void loginWithEmail(View view) {
        Utils.hideKeyboard(this);

        if (loginEmail.getError() != null) {
            Printer.snackBar(view, getString(R.string.login_wrong_email));
            return;
        }

        User user = new User(
                loginEmail.getText().toString(),
                loginPassword.getText().toString(),
                "user"
        );

        progressDialog.show();

        try {
            if (new LoginUser("default", loginListener).execute(user).get().success()) {
                appSession = loginListener.getResult();
                finishLogin(false);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Utils.logError(Login.class, e);
            progressDialog.dismiss();
        }
    }

    public void loginWithGoogle(View view) {
        Utils.hideKeyboard(this);
        progressDialog.show();

        GoogleSignInAccount account = GoogleAccountHelper.isGoogleAccountActive(this);

        if (account != null) {
            appSession = GoogleAccountHelper.login(account, loginListener);
            finishLogin(true);
        }
        else {
            GoogleAccountHelper.showSignIn(this);
        }
    }

    private void finishLogin(boolean googleAccount) {
        PreferencesHelper.getInstance(this).putUser(appSession.getUser(), googleAccount);
        progressDialog.dismiss();
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getBaseContext(), SignUp.class);

        startActivity(intent);
    }
}
