package com.dnieln7.roadwatchman.ui.app.pages.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.task.ITaskListener;
import com.dnieln7.roadwatchman.task.user.UpdateUserTask;
import com.dnieln7.roadwatchman.ui.app.pages.AppViewModel;
import com.dnieln7.roadwatchman.ui.app.pages.reports.ReportDataService;
import com.dnieln7.roadwatchman.utils.PreferencesHelper;
import com.dnieln7.roadwatchman.utils.Printer;
import com.google.android.material.textfield.TextInputEditText;

public class Profile extends Fragment implements ITaskListener<User> {

    // Objects
    private AppViewModel appViewModel;

    // Widgets
    private View root;

    private TextView username;
    private TextView email;
    private TextView role;
    private TextView google;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        initWidgets();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_sign_out:
                PreferencesHelper.getInstance(getActivity()).destroy();
                ReportDataService.deleteAll(getContext());
                requireActivity().finish();
                return true;
            case R.id.profile_edit:
                if (PreferencesHelper.getInstance(getActivity()).isGoogleAccount()) {
                    Printer.okDialog(getContext(), getString(R.string.profile_warning), getString(R.string.profile_warning_message));
                }
                else {
                    showEditModal().show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {
        username = root.findViewById(R.id.profile_username);
        email = root.findViewById(R.id.profile_email);
        role = root.findViewById(R.id.profile_role);
        google = root.findViewById(R.id.profile_google);


        appViewModel.getUser().observe(requireActivity(), user -> {
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            role.setText(user.getRole().equals("user") ? R.string.profile_logged_user : R.string.profile_logged_admin);

            if (user.getGoogleId().length() > 1) {
                google.setText(R.string.profile_logged_google);
                root.findViewById(R.id.profile_google_logo).setVisibility(View.VISIBLE);
                root.findViewById(R.id.profile_email_logo).setVisibility(View.GONE);
            }
            else {
                google.setText(R.string.profile_logged_email);
                root.findViewById(R.id.profile_google_logo).setVisibility(View.GONE);
                root.findViewById(R.id.profile_email_logo).setVisibility(View.VISIBLE);
            }
        });
    }

    private AlertDialog showEditModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profile_edit, null);
        AlertDialog dialog = builder.setCancelable(false).setView(view).create();

        view.findViewById(R.id.profile_cancel).setOnClickListener(v -> dialog.cancel());
        view.findViewById(R.id.profile_save).setOnClickListener(v -> {

            TextInputEditText usernameIn = view.findViewById(R.id.profile_username_in);
            TextInputEditText emailIn = view.findViewById(R.id.profile_email_in);
            TextInputEditText passwordIn = view.findViewById(R.id.profile_password_in);

            save(usernameIn.getText().toString().trim(), emailIn.getText().toString().trim(), passwordIn.getText().toString().trim());

            dialog.cancel();
        });

        return dialog;
    }

    private void save(String username, String email, String password) {
        final User user = new User();

        appViewModel.getUser().observe(requireActivity(), user1 -> {
            user.setId(user1.getId());
            user.setUsername(username.equals("") ? user1.getUsername() : username);
            user.setEmail(email.equals("") ? user1.getEmail() : email);
            user.setPassword(password.equals("") ? "" : password);
            user.setGoogleId(user1.getGoogleId());
            user.setRole(user1.getRole());
        });

        new UpdateUserTask(String.valueOf(user.getId()), this).execute(user);
    }

    @Override
    public void onSuccess(User object) {
        Printer.snackBar(root, getString(R.string.profile_save_message));
        PreferencesHelper.getInstance(getActivity()).putUser(object, false);
        appViewModel.setUser(object);
    }

    @Override
    public void onFail() {
        Printer.toast(getContext(), "Error");
    }
}