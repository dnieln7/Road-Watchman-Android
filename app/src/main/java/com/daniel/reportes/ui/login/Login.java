package com.daniel.reportes.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.user.GetUser;
import com.daniel.reportes.task.user.LoginUser;
import com.daniel.reportes.task.user.PostUser;
import com.daniel.reportes.ui.app.AppActivity;
import com.daniel.reportes.ui.signup.SignUp;
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

    // Widgets
    TextInputEditText loginEmail;
    TextInputEditText loginPassword;

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

        if(sharedPreferences.getBoolean("google", false)) {
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
        sharedPreferences.edit().putBoolean("google", !session.getUser().getGoogleId().equals("")).apply();
        sharedPreferences.edit().putString("email", session.getUser().getEmail()).apply();

        startActivity(intent);
    }

    private void manageAccount(GoogleSignInAccount account, boolean newUser) throws ExecutionException, InterruptedException {

        AppSession session;
        User user;

        if (newUser) {
            user = new User(
                    account.getDisplayName(),
                    account.getEmail(),
                    account.getId(),
                    account.getId(),
                    "user"
            );

            new PostUser().execute(user).get();

            session = (AppSession) new LoginUser("google").execute(user).get();
            session.setUser(new GetUser().execute(String.valueOf(session.getUserId())).get());
        }
        else {
            user = new User(
                    account.getEmail(),
                    account.getId(),
                    "user"
            );

            session = (AppSession) new LoginUser("google").execute(user).get();
            session.setUser(new GetUser().execute(String.valueOf(session.getUserId())).get());
        }

        goToApp(session);
    }

    public void loginWithEmail(View view) {

        try {
            User user = new User(
                    loginEmail.getText().toString(),
                    loginPassword.getText().toString(),
                    "user"
            );

            Object result = new LoginUser("default").execute(user).get();

            if (result instanceof AppSession) {

                ((AppSession) result).setUser(new GetUser().execute(String.valueOf(((AppSession) result).getUserId())).get());

                goToApp((AppSession) result);
            }
            else {
                Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
        catch (InterruptedException | ExecutionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loginWithGoogle(View view) {

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
}
