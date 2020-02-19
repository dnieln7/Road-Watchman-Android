package com.daniel.reportes.ui.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.email.ExistsEmail;
import com.daniel.reportes.task.email.SendEmail;
import com.daniel.reportes.task.user.PostUser;
import com.daniel.reportes.ui.other.IFragmentListener;
import com.daniel.reportes.utils.Printer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpForm extends Fragment {

    //Objects
    private boolean validEmail;
    private SignUpViewModel viewModel;
    private IFragmentListener listener;

    // Widgets
    private View root;

    private TextInputEditText signUsername;
    private TextInputEditText signEmail;
    private TextInputEditText signPassword;

    private MaterialButton signNext;

    public SignUpForm(IFragmentListener listener) {
        this.listener = listener;
        this.validEmail = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sign_up_form, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SignUpViewModel.class);

        initWidgets();
        initListeners();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getVerified().observe(this, aBoolean -> {
            if (aBoolean) {
                User user = new User(
                        signUsername.getText().toString(),
                        signEmail.getText().toString(),
                        signPassword.getText().toString(),
                        "",
                        "user"
                );

                try {
                    if (new PostUser(postListener).execute(user).get().success()) {
                        Toast.makeText(getContext(), "Registro completado!", Toast.LENGTH_SHORT).show();
                        listener.exit();
                    }
                }
                catch (ExecutionException | InterruptedException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        });
    }

    private void initWidgets() {
        signUsername = root.findViewById(R.id.signUsername);
        signEmail = root.findViewById(R.id.signEmail);
        signPassword = root.findViewById(R.id.signPassword);

        signNext = root.findViewById(R.id.signNext);
    }

    private void initListeners() {
        signNext.setOnClickListener(v -> next());
        signEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    signEmail.setError("Inválido!");
                }
                else if (!Utils.isEmailValid(s.toString())) {
                    signEmail.setError("Inválido!");
                    validEmail = false;
                }
                else {
                    validEmail = true;
                }
            }
        });
    }

    private void next() {
        Utils.hideKeyboard(getActivity());

        String email = signEmail.getText().toString();
        String code = Utils.generateCode();

        if (validEmail) {
            try {
                if (new ExistsEmail().execute(signEmail.getText().toString()).get()) {
                    Snackbar.make(root, "El usuario ya existe! olvido su contraseña?", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    new SendEmail().execute(email, code);

                    viewModel.setCode(code);
                    viewModel.setVerified(false);
                    listener.showFragment(2);
                }
            }
            catch (ExecutionException | InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    // Class
    private TaskListener<User> postListener = new TaskListener<User>() {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(SignUpForm.this.getContext(), this.exception.getMessage());
                return false;
            }
            return true;
        }
    };
}
