package com.daniel.reportes.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.user.GetUser;
import com.daniel.reportes.task.user.LoginUser;
import com.daniel.reportes.task.user.PostUser;
import com.daniel.reportes.ui.app.AppActivity;
import com.daniel.reportes.ui.signup.SignUp;
import com.daniel.reportes.utils.Printer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends AppCompatActivity {

    //Objects
    private SharedPreferences sharedPreferences;
    private AppSession appSession;

    // Widgets
    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidgets();
        loadPreferences();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                manageAccount(account, true);
            }
            catch (ApiException e) {
                Log.w("Error", "signInResult:failed code = " + e.getStatusCode());
            }
            catch (InterruptedException | ExecutionException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private void loadPreferences() {
        sharedPreferences = getSharedPreferences("com.daniel.reportes.settings", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("DarkTheme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (sharedPreferences.getInt("id", 0) != 0) {
            AppSession session = new AppSession(
                    "",
                    sharedPreferences.getInt("id", 0),
                    new User(
                            sharedPreferences.getInt("id", 0),
                            sharedPreferences.getString("username", ""),
                            sharedPreferences.getString("email", ""),
                            sharedPreferences.getString("googleId", ""),
                            sharedPreferences.getString("role", "")
                    )
            );

            goToApp(session);
        }
        else if (sharedPreferences.getBoolean("google", false)) {
            loginWithGoogle(null);
        }
        else {
            loginEmail.setText(sharedPreferences.getString("email", ""));
        }
    }

    private void initWidgets() {
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
    }

    private void goToApp(AppSession session) {
        Intent intent = new Intent(getBaseContext(), AppActivity.class);

        intent.putExtra("session", session);

        sharedPreferences.edit().putInt("id", session.getUser().getId()).apply();
        sharedPreferences.edit().putString("username", session.getUser().getUsername()).apply();
        sharedPreferences.edit().putString("email", session.getUser().getEmail()).apply();
        sharedPreferences.edit().putBoolean("google", session.getUser().getGoogleId().length() > 0).apply();
        sharedPreferences.edit().putString("role", session.getUser().getRole()).apply();

        startActivity(intent);
    }

    private void manageAccount(GoogleSignInAccount account, boolean newUser) throws ExecutionException, InterruptedException {
        User user;

        if (newUser) {
            user = new User(
                    account.getDisplayName(),
                    account.getEmail(),
                    account.getId(),
                    account.getId(),
                    "user"
            );

            if (new PostUser(postListener).execute(user).get().success()) {
                if (new LoginUser("google", loginListener).execute(user).get().success()) {
                    appSession = loginListener.getResult();
                    appSession.setUser(new GetUser().execute(String.valueOf(appSession.getUserId())).get());
                }
            }
        }
        else {
            user = new User(
                    account.getEmail(),
                    account.getId(),
                    "user"
            );

            if (new LoginUser("google", loginListener).execute(user).get().success()) {
                appSession = loginListener.getResult();
                appSession.setUser(new GetUser().execute(String.valueOf(appSession.getUserId())).get());
            }
        }

        goToApp(appSession);
    }

    public void loginWithEmail(View view) {
        Utils.hideKeyboard(this);

        User user = new User(
                loginEmail.getText().toString(),
                loginPassword.getText().toString(),
                "user"
        );

        try {
            if (new LoginUser("default", loginListener).execute(user).get().success()) {
                appSession = loginListener.getResult();
                appSession.setUser(new GetUser().execute(String.valueOf(appSession.getUserId())).get());
                goToApp(appSession);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loginWithGoogle(View view) {
        Utils.hideKeyboard(this);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestProfile()
                    .requestEmail()
                    .build();

            GoogleSignInClient signIn = GoogleSignIn.getClient(this, options);
            Intent signInIntent = signIn.getSignInIntent();

            startActivityForResult(signInIntent, Utils.GOOGLE_SIGN);
        }
        else {
            try {
                manageAccount(account, false);
            }
            catch (ExecutionException | InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getBaseContext(), SignUp.class);

        startActivity(intent);
    }

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

    private TaskListener<User> postListener = new TaskListener<User>() {

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
}
