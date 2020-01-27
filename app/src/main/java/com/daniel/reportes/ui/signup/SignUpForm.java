package com.daniel.reportes.ui.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.email.ExistsEmail;
import com.daniel.reportes.task.email.SendEmail;
import com.daniel.reportes.task.user.PostUser;
import com.daniel.reportes.ui.other.IFragmentListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpForm extends Fragment {

    //Var
    boolean validEmail;

    //Objects
    private View root;
    private SignUpViewModel viewModel;
    private IFragmentListener listener;

    // Widgets
    private EditText newUsername;
    private EditText newEmail;
    private EditText newPassword;
    private Button next;

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
                Object object = new PostUser().execute(new User(
                        newUsername.getText().toString(),
                        newEmail.getText().toString(),
                        newPassword.getText().toString(),
                        "",
                        "user"
                ));

                if(object != null) {
                    Toast.makeText(getContext(), "Registro completado!", Toast.LENGTH_SHORT).show();
                    listener.exit();
                }
            }
        });
    }

    private void initWidgets() {
        newUsername = root.findViewById(R.id.newUsername);
        newEmail = root.findViewById(R.id.newEmail);
        newPassword = root.findViewById(R.id.newPassword);
        next = root.findViewById(R.id.next);
    }

    private void initListeners() {
        next.setOnClickListener(v -> next());
        newEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")) {
                    newEmail.setError("Inválido!");
                }
                else if(!Utils.isEmailValid(s.toString())) {
                    newEmail.setError("Inválido!");
                    validEmail = false;
                }
                else {
                    validEmail = true;
                }
            }
        });
    }

    private void next() {

        String email = newEmail.getText().toString();
        String code = Utils.generateCode();

        if (validEmail) {
            try {
                if(new ExistsEmail().execute(newEmail.getText().toString()).get()) {
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
}
