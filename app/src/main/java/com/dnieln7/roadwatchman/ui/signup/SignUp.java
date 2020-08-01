package com.dnieln7.roadwatchman.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.model.AuthResponse;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.data.model.VerificationResponse;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.dnieln7.roadwatchman.task.user.SignUpTask;
import com.dnieln7.roadwatchman.task.user.VerifyTask;
import com.dnieln7.roadwatchman.utils.Printer;
import com.dnieln7.roadwatchman.utils.TextMonitor;
import com.dnieln7.roadwatchman.utils.WindowController;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity {

    private int code;
    private AlertDialog progressDialog;

    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preInit();
        init();
        postInit();
    }

    private void preInit() {
        code = 0;
    }

    private void init() {
        name = findViewById(R.id.sign_up_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);

        progressDialog = Printer.progressDialog(
                this,
                getString(R.string.please_wait),
                getString(R.string.sign_up_loading_message)
        );
    }

    private void postInit() {
        email.addTextChangedListener(new TextMonitor(email, getString(R.string.login_wrong_email)));
    }

    private boolean verifyForm() {
        String email = this.email.getText().toString();
        String name = this.name.getText().toString();
        String password = this.password.getText().toString();

        return !name.equals("") && !email.equals("") && !password.equals("") && this.email.getError() == null;
    }

    public void next(View view) {
        WindowController.hideKeyboard(this);

        if (verifyForm()) {
            ITaskListener<VerificationResponse> vListener = new ITaskListener<VerificationResponse>() {
                @Override
                public void onSuccess(VerificationResponse object) {
                    progressDialog.dismiss();
                    code = object.getResult();
                    verifyDialog(view).show();
                }

                @Override
                public void onFail() {
                    progressDialog.dismiss();
                    Printer.toast(SignUp.this, "Error");
                }
            };
            new VerifyTask(vListener).execute(email.getText().toString());
            progressDialog.show();
        }
        else {
            Printer.snackBar(view, getString(R.string.sign_up_invalid_form));
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
                        name.getText().toString(),
                        password.getText().toString(),
                        email.getText().toString(),
                        "",
                        "user"
                );

                try {
                    AuthResponse response = new SignUpTask(null).execute(user).get();

                    if (response.getCode() == 1) {
                        Printer.toast(this, getString(R.string.sign_up_completed));
                        dialog.dismiss();
                        finish();
                    }
                }
                catch (ExecutionException | InterruptedException e) {
                    Printer.logError(SignUp.class.getName(), e);
                }
            }
        });

        return dialog;
    }
}
