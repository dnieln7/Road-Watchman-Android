package com.daniel.reportes.ui.app.fragment.profile;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.Utils;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.user.PutUser;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileFragment extends Fragment {

    // Var

    // Objects
    private View root;
    private AppViewModel appViewModel;
    private AppSession appSession;

    // Widgets
    private EditText profileUsername;
    private TextInputEditText profileEmail;
    private TextInputEditText profilePassword;

    private ImageView editUsername;
    private ImageView editEmail;
    private ImageView editPassword;

    private Button save;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        initVar();
        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    private void initVar() {

    }

    private void initObjects() {
        appViewModel.getAppSession().observe(getActivity(), session -> appSession = session);
    }

    private void initWidgets() {
        profileUsername = root.findViewById(R.id.profileUsername);
        profileEmail = root.findViewById(R.id.profileEmail);
        profilePassword = root.findViewById(R.id.profilePassword);

        editUsername = root.findViewById(R.id.editUsername);
        editEmail = root.findViewById(R.id.editEmail);
        editPassword = root.findViewById(R.id.editPassword);

        save = root.findViewById(R.id.save);

        profileUsername.setText(appSession.getUser().getUsername());
        profileEmail.setText(appSession.getUser().getEmail());
        profilePassword.setText(appSession.getUser().getPassword());
    }

    private void initListeners() {
        save.setOnClickListener(v -> save());
        editUsername.setOnClickListener(v -> editUsername());
        editEmail.setOnClickListener(v -> editEmail());
        editPassword.setOnClickListener(v -> editPassword());
    }

    private void editUsername() {

        if (profileUsername.isEnabled()) {
            profileUsername.setEnabled(false);
        }
        else {
            profileUsername.setEnabled(true);
        }
    }

    private void editEmail() {

        if (profileEmail.isEnabled()) {
            profileEmail.setEnabled(false);
        }
        else {
            profileEmail.setEnabled(true);
        }
    }

    private void editPassword() {

        if (profilePassword.isEnabled()) {
            profilePassword.setEnabled(false);
            ((TextInputLayout) root.findViewById(R.id.layoutPassword)).setPasswordVisibilityToggleTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }
        else {
            profilePassword.setEnabled(true);
            ((TextInputLayout) root.findViewById(R.id.layoutPassword)).setPasswordVisibilityToggleTintList(ColorStateList.valueOf(Utils.GetPrimaryColor(getContext())));
        }
    }

    private void save() {
        User user = new User(
                profileUsername.getText().toString(),
                profileEmail.getText().toString(),
                profilePassword.getText().toString(),
                appSession.getUser().getGoogleId(),
                appSession.getUser().getRole()
        );

        try {
            user = new PutUser(String.valueOf(appSession.getUser().getId())).execute(user).get();

            if(user != null) {
                Snackbar.make(root, "Los cambios se han guardado!", Snackbar.LENGTH_SHORT).show();
                profileUsername.setEnabled(false);
                profileEmail.setEnabled(false);
                profilePassword.setEnabled(false);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}