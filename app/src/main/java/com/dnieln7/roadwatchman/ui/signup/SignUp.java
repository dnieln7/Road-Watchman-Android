package com.dnieln7.roadwatchman.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.User;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.roadwatchman.task.user.PostUser;
import com.dnieln7.roadwatchman.task.user.VerifyEmail;
import com.dnieln7.roadwatchman.utils.Printer;
import com.dnieln7.roadwatchman.utils.TextMonitor;
import com.dnieln7.roadwatchman.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUp extends AppCompatActivity {

    //Objects
    private int code;

    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;

    // Class
    private TaskListener<User> postListener = new TaskListener<User>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(getApplicationContext(), this.exception.getMessage());
                return false;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initWidgets();
        initListeners();
    }

    private void initWidgets() {
        username = findViewById(R.id.sign_up_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
    }

    private void initListeners() {
        email.addTextChangedListener(new TextMonitor(email, getString(R.string.login_wrong_email)));
    }

    public void next(View view) {
        Utils.hideKeyboard(this);

        String email = this.email.getText().toString();
        code = 20;

        if (!email.equals("")) {
            if (this.email.getError() != null) {
                Printer.snackBar(view, getString(R.string.login_wrong_email));
                return;
            }

            TaskListener codeListener = new TaskListener() {
            };

            try {
                if (new VerifyEmail(codeListener).execute(email).get().success()) {
                    Printer.toast(this, codeListener.getResult().toString());
                    verifyDialog(view).show();
                }
            }
            catch (ExecutionException | InterruptedException e) {
                Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, "There was an error", e);
            }
        }
        else {
            Printer.snackBar(view, getString(R.string.login_wrong_email));
        }
    }

    private AlertDialog verifyDialog(View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_verify, null);
        AlertDialog dialog = builder.setTitle(R.string.sign_up_verify).setCancelable(false).setView(view).create();

        view.findViewById(R.id.verify_cancel).setOnClickListener(v -> dialog.cancel());
        view.findViewById(R.id.verify_complete).setOnClickListener(v -> {

            TextInputEditText code = view.findViewById(R.id.verify_code);
            String codeIn = code.getText().toString();

            if (codeIn.equals("") || Integer.parseInt(codeIn) != this.code) {
                code.setError(getString(R.string.sign_up_wrong_code));
            }
            else {
                User user = new User(
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        "",
                        "user"
                );

                try {
                    if (new PostUser(postListener).execute(user).get().success()) {
                        Printer.snackBar(parent, getString(R.string.sign_up_completed));
                        dialog.dismiss();
                        finish();
                    }
                }
                catch (ExecutionException | InterruptedException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        });

        return dialog;
    }
}
