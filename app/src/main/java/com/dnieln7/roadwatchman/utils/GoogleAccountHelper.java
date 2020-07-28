package com.dnieln7.roadwatchman.utils;

import android.app.Activity;
import android.content.Intent;

import com.dnieln7.roadwatchman.data.model.AuthResponse;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.dnieln7.roadwatchman.task.user.LoginTask;
import com.dnieln7.roadwatchman.task.user.SignUpTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.concurrent.ExecutionException;

/**
 * Helper class to manage a google account.
 *
 * @author dnieln7
 */
public class GoogleAccountHelper {

    private GoogleAccountHelper() {
    }

    public static final int REQUEST_CODE = 1;

    /**
     * Checks if there is a google account.
     *
     * @param activity - The activity to get account account info.
     * @return An {@link GoogleSignInAccount} instance; null if there is no account.
     */
    public static GoogleSignInAccount isGoogleAccountActive(Activity activity) {
        return GoogleSignIn.getLastSignedInAccount(activity);
    }

    /**
     * Shows signIn window to user
     *
     * @param activity - The activity from witch the window is shown.
     */
    public static void showSignIn(Activity activity) {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        GoogleSignInClient signIn = GoogleSignIn.getClient(activity, options);
        Intent signInIntent = signIn.getSignInIntent();

        activity.startActivityForResult(signInIntent, REQUEST_CODE);
    }

    /**
     * Login with the provided google account.
     *
     * @param account  - A {@link GoogleSignInAccount} instance to log in.
     * @param listener - A {@link ITaskListener<AuthResponse>} instance to listen for the results.
     */
    public static void login(GoogleSignInAccount account, ITaskListener<AuthResponse> listener) {
        User user = new User(
                account.getEmail(),
                account.getId(),
                "user"
        );

        new LoginTask("google", listener).execute(user);
    }

    /**
     * Register with the provided google account.
     *
     * @param account  - A {@link GoogleSignInAccount} instance to register.
     * @param listener - A {@link ITaskListener<AuthResponse>} instance to listen for the results.
     */
    public static void register(GoogleSignInAccount account, ITaskListener<AuthResponse> listener) {
        User signUp = new User(
                account.getDisplayName(),
                "",
                account.getEmail(),
                account.getId(),
                "user"
        );

        try {
            AuthResponse response = new SignUpTask(null).execute(signUp).get();
            if (response.getCode() == 1 || response.getMessage().equals("The email is already registered")) {
                signUp.setPassword(signUp.getGoogleId());

                new LoginTask("google", listener).execute(signUp);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Printer.logError(GoogleSignInAccount.class.getName(), e);
        }
    }
}
