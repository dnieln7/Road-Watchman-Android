package com.daniel.reportes.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.reportes.R;
import com.daniel.reportes.data.User;
import com.daniel.reportes.ui.reportes.Reportes;
import com.daniel.reportes.ui.signup.SignUp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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

                User user = new User(
                        account.getEmail(),
                        account.getId(),
                        "user"
                );

                login(user);
            }
            catch (ApiException e) {
                Log.w("Error", "signInResult:failed code = " + e.getStatusCode());
            }
        }
    }

    private void initWidgets() {
        emailIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
    }

    private void login(User userIn) {

        String token = "";
        User user = null;


        if (user != null && !token.equals("")) {
            Intent intent = new Intent(getBaseContext(), Reportes.class);

            intent.putExtra("user", user);
            intent.putExtra("token", token);

            startActivity(intent);
        }
    }

    public void loginWithEmail(View view) {

        User user = new User(
                emailIn.getText().toString(),
                passwordIn.getText().toString(),
                "user"
        );

        login(user);
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
            User user = new User(
                    account.getEmail(),
                    account.getId(),
                    "user"
            );

            login(user);
        }
    }

    public void forgotPassword(View view) {

        if (emailIn.getText().toString().trim().equals("")) {
            emailIn.setError("Requerido!");
        }
        else {
            Fragment mFragment = new ResetPassword();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.forgotPassword, mFragment).commit();
        }
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getBaseContext(), SignUp.class);

        startActivity(intent);
    }
}
