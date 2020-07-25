package com.dnieln7.roadwatchman.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.model.AuthResponse;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.dnieln7.roadwatchman.task.user.LoginTask;
import com.dnieln7.roadwatchman.ui.signup.SignUp;
import com.dnieln7.roadwatchman.utils.GoogleAccountHelper;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.Printer;
import com.dnieln7.roadwatchman.utils.TextMonitor;
import com.dnieln7.roadwatchman.utils.WindowController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity implements ITaskListener<AuthResponse> {

    public static int REQUEST_CODE = 2;

    private User user;
    private AlertDialog progressDialog;

    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

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
                if (account != null) {
                    GoogleAccountHelper.register(account, this);
                }
            }
            catch (ApiException e) {
                Printer.logError(Login.class.getName(), e);
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
        WindowController.hideKeyboard(this);

        if (loginEmail.getError() != null) {
            Printer.snackBar(view, getString(R.string.login_wrong_email));
            return;
        }

        User login = new User(
                loginEmail.getText().toString(),
                loginPassword.getText().toString(),
                "user"
        );

        progressDialog.show();

        new LoginTask("email", this).execute(login);
    }

    public void loginWithGoogle(View view) {
        WindowController.hideKeyboard(this);
        progressDialog.show();

        GoogleSignInAccount account = GoogleAccountHelper.isGoogleAccountActive(this);

        if (account != null) {
            GoogleAccountHelper.login(account, this);
            finishLogin(true);
        }
        else {
            GoogleAccountHelper.showSignIn(this);
        }
    }

    private void finishLogin(boolean googleAccount) {
        PreferencesHelper.getInstance(this).putUser(user, googleAccount);
        progressDialog.dismiss();
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getBaseContext(), SignUp.class);

        startActivity(intent);
    }

    @Override
    public void onSuccess(AuthResponse object) {
        progressDialog.dismiss();

        if (object.getCode() == 1) {
            user = object.getResult();
            finishLogin(object.getResult().getGoogleId().length() > 1);
        }
        else {
            Printer.toast(this, object.getMessage());
        }
    }

    @Override
    public void onFail() {
        progressDialog.dismiss();
        Printer.toast(this, "Error");
    }
}
