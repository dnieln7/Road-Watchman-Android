package com.daniel.reportes.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniel.reportes.R;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.user.GetUser;
import com.daniel.reportes.task.user.LoginUser;
import com.daniel.reportes.task.user.PostUser;
import com.daniel.reportes.ui.reportes.Reportes;
import com.daniel.reportes.ui.signup.SignUp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends AppCompatActivity {

    // Widgets
    EditText emailIn;
    EditText passwordIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidgets();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
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

    private void initWidgets() {
        emailIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
    }

    private void goToApp(AppSession session) {

        Intent intent = new Intent(getBaseContext(), Reportes.class);

        intent.putExtra("session", session);

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
                    emailIn.getText().toString(),
                    passwordIn.getText().toString(),
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

            startActivityForResult(signInIntent, 100);
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
