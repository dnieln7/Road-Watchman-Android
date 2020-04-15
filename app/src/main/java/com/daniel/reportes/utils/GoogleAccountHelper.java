package com.daniel.reportes.utils;

import android.app.Activity;
import android.content.Intent;

import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.user.GetUser;
import com.daniel.reportes.task.user.LoginUser;
import com.daniel.reportes.task.user.PostUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to manage a google account.
 *
 * @author dnieln7
 */
public class GoogleAccountHelper {

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
     * @param account       - A {@link GoogleSignInAccount} instance to log in.
     * @param loginListener - A {@link TaskListener<AppSession>} instance to listen for the results.
     * @return An {@link AppSession} instance containing the user data.
     */
    public static AppSession login(GoogleSignInAccount account, TaskListener<AppSession> loginListener) {
        AppSession appSession = null;

        User user = new User(
                account.getEmail(),
                account.getId(),
                "user"
        );

        try {
            if (new LoginUser("google", loginListener).execute(user).get().success()) {
                appSession = loginListener.getResult();
                appSession.setUser(new GetUser().execute(String.valueOf(appSession.getUserId())).get());
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(GoogleSignInAccount.class.getName()).log(Level.SEVERE, null, e);
        }

        return appSession;
    }

    /**
     * Register with the provided google account.
     *
     * @param account       - A {@link GoogleSignInAccount} instance to register.
     * @param userListener  - A {@link TaskListener<User>} instance to listen for the results.
     * @param loginListener - A {@link TaskListener<AppSession>} instance to listen for the results.
     * @return An {@link AppSession} instance containing the user data.
     */
    public static AppSession register(GoogleSignInAccount account, TaskListener<User> userListener, TaskListener<AppSession> loginListener) {
        AppSession appSession = null;

        User user = new User(
                account.getDisplayName(),
                account.getEmail(),
                account.getId(),
                account.getId(),
                "user"
        );

        try {
            if (new PostUser(userListener).execute(user).get().success()) {
                if (new LoginUser("google", loginListener).execute(user).get().success()) {
                    appSession = loginListener.getResult();
                    appSession.setUser(new GetUser().execute(String.valueOf(appSession.getUserId())).get());
                }
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(GoogleSignInAccount.class.getName()).log(Level.SEVERE, null, e);
        }

        return appSession;
    }
}
