package com.daniel.reportes.ui.app.fragment.profile;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daniel.reportes.R;
import com.daniel.reportes.utils.Utils;
import com.daniel.reportes.data.AppSession;
import com.daniel.reportes.data.User;
import com.daniel.reportes.task.TaskListener;
import com.daniel.reportes.task.user.PutUser;
import com.daniel.reportes.ui.app.fragment.AppViewModel;
import com.daniel.reportes.utils.PreferencesUtils;
import com.daniel.reportes.utils.Printer;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileFragment extends Fragment {

    // Objects
    private AppViewModel appViewModel;
    private AppSession appSession;

    // Widgets
    private View root;

    private TextInputEditText profileUsername;
    private TextInputEditText profileEmail;
    private TextInputEditText profilePassword;

    private Button save;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
        setHasOptionsMenu(true);

        initObjects();
        initWidgets();
        initListeners();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileSignOut:
                PreferencesUtils.saveUser(getContext(), new User(0, "", "", "", ""));
                appSession = null;
                appViewModel.setAppSession(null);
                getActivity().finish();
                return true;
            case R.id.profileEdit:
                edit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initObjects() {
        appViewModel.getAppSession().observe(getActivity(), session -> appSession = session);
    }

    private void initWidgets() {
        profileUsername = root.findViewById(R.id.profileUsername);
        profileEmail = root.findViewById(R.id.profileEmail);
        profilePassword = root.findViewById(R.id.profilePassword);

        save = root.findViewById(R.id.save);

        profileUsername.setText(appSession.getUser().getUsername());
        profileEmail.setText(appSession.getUser().getEmail());
        profilePassword.setText(appSession.getUser().getPassword());
    }

    private void initListeners() {
        save.setOnClickListener(v -> save());
    }

    private void edit() {
        profileUsername.setEnabled(!profileUsername.isEnabled());
        profileEmail.setEnabled(!profileEmail.isEnabled());
        profilePassword.setEnabled(!profilePassword.isEnabled());

        if (profilePassword.isEnabled()) {
            ((TextInputLayout) root.findViewById(R.id.layoutPassword))
                    .setEndIconTintList(ColorStateList.valueOf(Utils.GetPrimaryColor(getContext())));
        }
        else {
            ((TextInputLayout) root.findViewById(R.id.layoutPassword))
                    .setEndIconTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }

        if(save.getVisibility() == View.INVISIBLE) {
            save.setVisibility(View.VISIBLE);
        }
        else {
            save.setVisibility(View.INVISIBLE);
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
            PutListener listener = new PutListener();

            if (new PutUser(String.valueOf(appSession.getUser().getId()), listener).execute(user).get().success()) {
                Snackbar.make(root, "Los cambios se han guardado!", Snackbar.LENGTH_SHORT).show();

                user = listener.getResult();

                profileUsername.setEnabled(false);
                profileEmail.setEnabled(false);
                profilePassword.setEnabled(false);
                appSession.setUser(user);
                appViewModel.setAppSession(appSession);

                profileUsername.setText(appSession.getUser().getUsername());
                profileEmail.setText(appSession.getUser().getEmail());
            }
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    // Class

    private class PutListener extends TaskListener<User> {

        @Override
        public boolean success() {
            if (this.exception != null) {
                Printer.toast(ProfileFragment.this.getContext(), this.exception.getMessage());
                return false;
            }
            return true;
        }
    }
}