package com.dnieln7.roadwatchman.ui.app.fragment.reportes.create;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.Reporte;
import com.dnieln7.roadwatchman.data.User;
import com.dnieln7.roadwatchman.utils.LocationHelper;
import com.dnieln7.roadwatchman.utils.LocationUtils;
import com.dnieln7.roadwatchman.utils.Printer;
import com.google.android.material.button.MaterialButton;

import org.joda.time.LocalDateTime;

public class CreateReport extends AppCompatActivity {

    public static final int REQUEST_CODE = 2;

    private int currentStep;
    private User user;

    private CreateViewModel createViewModel;
    private Location location;

    private SelectPicture selectPicture;
    private AddDescription addDescription;

    private ProgressBar progressBar;
    private TextView stepDescription;
    private MaterialButton nextStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        user = (User) getIntent().getSerializableExtra("user");
        createViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        progressBar = findViewById(R.id.create_progress);
        stepDescription = findViewById(R.id.create_step_description);
        nextStep = findViewById(R.id.create_next);

        if (LocationHelper.hasGPSEnabled(this)) {
            location = LocationUtils.getGPS(this);
        }
        else {
            LocationHelper.askActivateLocation(this);
            location = LocationUtils.getGPS(this);
        }

        initFragments();
        switchFragment(1);
    }

    private void initFragments() {
        currentStep = 1;

        selectPicture = new SelectPicture();
        addDescription = new AddDescription();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.create_fragment, addDescription)
                .add(R.id.create_fragment, selectPicture)
                .commit();
    }

    public void next(View view) {

        if (currentStep == 1) {
            if (location != null) {
                createViewModel.getPicture().observe(this, hasPicture -> {
                    if (hasPicture) {
                        createViewModel.setReporte(new Reporte(
                                new LocalDateTime().toString(),
                                new double[]{location.getLatitude(), location.getLongitude()},
                                user.getId()
                        ));
                        createViewModel.appendUserId(user.getId());
                        switchFragment(2);
                    }
                    else {
                        Printer.snackBar(view, getString(R.string.create_report_no_picture));
                    }
                });
            }
            else {
                Printer.snackBar(view, getString(R.string.create_report_location));
                location = LocationUtils.getGPS(this);
            }
        }
        else if (currentStep == 2) {
            addDescription.upload();
        }
    }

    private void switchFragment(int fragment) {
        if (fragment == 1) {
            getSupportFragmentManager().beginTransaction().hide(addDescription).show(selectPicture).commit();
            nextStep.setText(R.string.create_report_next);
            stepDescription.setText(R.string.create_report_photo);
            currentStep = 1;
            updateProgress(1);
        }
        if (fragment == 2) {
            getSupportFragmentManager().beginTransaction().hide(selectPicture).show(addDescription).commit();
            nextStep.setText(R.string.create_report_upload);
            stepDescription.setText(R.string.create_report_add_description);
            currentStep = 2;
            updateProgress(2);
        }
    }

    private void updateProgress(int progress) {
        switch (progress) {
            case 1:
                progressBar.setProgress(33);
                break;
            case 2:
                progressBar.setProgress(66);
                break;
            case 3:
                progressBar.setProgress(99);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentStep == 1) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit)
                    .setMessage(R.string.create_report_exit_warning)
                    .setPositiveButton(R.string.create_report_yes, (dialog, which) -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    }).setNegativeButton(R.string.create_report_no, (dialog, which) -> {
            }).show();
        }
        else if (currentStep == 2) {
            switchFragment(1);
        }
    }
}
