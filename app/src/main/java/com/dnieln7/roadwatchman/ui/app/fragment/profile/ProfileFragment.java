package com.dnieln7.roadwatchman.ui.app.fragment.profile;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.User;
import com.dnieln7.roadwatchman.task.TaskListener;
import com.dnieln7.roadwatchman.task.user.PutUser;
import com.dnieln7.roadwatchman.ui.app.fragment.AppViewModel;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.Printer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileFragment extends Fragment {

    // Objects
    private AppViewModel appViewModel;

    // Widgets
    private View root;

    private TextInputEditText profileUsername;
    private TextInputEditText profileEmail;
    private TextInputEditText profilePassword;

    private Button save;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

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
                PreferencesHelper.getInstance(getActivity()).destroy();
                getActivity().finish();
                return true;
            case R.id.profileEdit:
                if (PreferencesHelper.getInstance(getActivity()).isGoogleAccount()) {
                    Printer.okDialog(getContext(), getString(R.string.profile_warning), getString(R.string.profile_warning_message));
                }
                else {
                    edit();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {
        profileUsername = root.findViewById(R.id.profileUsername);
        profileEmail = root.findViewById(R.id.profileEmail);
        profilePassword = root.findViewById(R.id.profilePassword);

        save = root.findViewById(R.id.save);

        appViewModel.getAppSession().observe(getActivity(), appSession -> {
            profileUsername.setText(appSession.getUser().getUsername());
            profileEmail.setText(appSession.getUser().getEmail());
            profilePassword.setText(appSession.getUser().getPassword());
        });
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
                    .setEndIconTintList(ColorStateList.valueOf(getContext().getColor(R.color.primary)));
        }
        else {
            ((TextInputLayout) root.findViewById(R.id.layoutPassword))
                    .setEndIconTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }

        save.setVisibility(save.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }

    private void save() {
        final User user = new User();

        appViewModel.getAppSession().observe(getActivity(), appSession -> {
            user.setId(appSession.getUser().getId());
            user.setUsername(profileUsername.getText().toString());
            user.setEmail(profileEmail.getText().toString());
            user.setPassword(profilePassword.getText().toString());
            user.setGoogleId(appSession.getUser().getGoogleId());
            user.setRole(appSession.getUser().getRole());
        });

        try {
            PutListener listener = new PutListener();

            if (new PutUser(String.valueOf(user.getId()), listener).execute(user).get().success()) {
                Printer.snackBar(root, getString(R.string.profile_save_message));
                PreferencesHelper.getInstance(getActivity()).putUser(user, false);
                edit();
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