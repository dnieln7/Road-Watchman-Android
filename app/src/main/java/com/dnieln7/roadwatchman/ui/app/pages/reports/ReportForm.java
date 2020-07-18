package com.dnieln7.roadwatchman.ui.app.pages.reports;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.data.model.User;
import com.dnieln7.roadwatchman.ui.permission.Permissions;
import com.dnieln7.roadwatchman.utils.LocationUtils;
import com.dnieln7.roadwatchman.utils.Printer;
import com.dnieln7.roadwatchman.utils.WindowController;
import com.dnieln7.roadwatchman.work.report.ReportWorkManager;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportForm extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private static final int SELECT_PICTURE = 3;

    private TextInputEditText description;
    private ImageView picture;

    private User user;
    private Reporte report;
    private Location location;

    private String pictureName;
    private Uri pictureUri;
    private boolean hasPicture = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);

        location = LocationUtils.getGPS(this);
        user = (User) getIntent().getSerializableExtra("user");
        description = findViewById(R.id.report_form_description);
        picture = findViewById(R.id.report_form_picture);
        picture.setOnClickListener(v -> selectPicture());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE) {

            if (data != null && data.getData() != null) {
                pictureUri = data.getData();
                Picasso.get().load(pictureUri).error(R.drawable.reportes).into(picture);
            }
            else {
                if (pictureUri != null) {
                    Picasso.get().load(pictureUri).error(R.drawable.reportes).into(picture);
                }
            }

            try {
                if (pictureUri != null) {
                    hasPicture = MediaStore.Images.Media.getBitmap(getContentResolver(), pictureUri) != null;
                    location = LocationUtils.getGPS(this);
                }
            }
            catch (IOException e) {
                Printer.logError(ReportForm.class.getName(), e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.REQUEST_CODE) {
            if (Permissions.hasPermissions(this)) {
                selectPicture();
            }
        }
    }

    private void selectPicture() {
        if (Permissions.hasPermissions(this)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createPictureUri());

            Intent intentContent = new Intent(Intent.ACTION_GET_CONTENT);
            intentContent.setType("image/*");

            Intent intentPick = new Intent(Intent.ACTION_PICK);
            intentPick.setType("image/*");

            Intent intentChooser = Intent.createChooser(intentContent, getString(R.string.create_report_select));
            intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentPick, cameraIntent});

            startActivityForResult(intentChooser, SELECT_PICTURE);
        }
        else {
            Permissions.askPermissions(this);
        }
    }

    private Uri createPictureUri() {
        pictureName = "report_" + new LocalDateTime() + ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();

            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pictureName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + getString(R.string.app_name) + "/");

            pictureUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        else {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM
                    ),
                    "Road Watchman reports"
            );

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Logger.getLogger(ReportForm.class.getName()).log(Level.SEVERE, "Failed to create directory");
                    pictureUri = null;
                }
            }

            pictureUri = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + pictureName));
        }

        return pictureUri;
    }

    public void appendUserId(int id) {
        if (pictureName != null) {
            String[] nameSplit = pictureName.split("\\.");
            String type = nameSplit[nameSplit.length - 1];
            pictureName = pictureName.replace("." + type, "_" + id + "." + type);
        }
    }

    public void upload(View view) {
        WindowController.hideKeyboard(this);

        if (location != null) {
            if (hasPicture && pictureUri != null) {
                report = new Reporte(
                        new LocalDateTime().toString(),
                        new double[]{location.getLatitude(), location.getLongitude()},
                        user.getId()
                );

                appendUserId(user.getId());

                report.setDescription(this.description.getText() != null ? this.description.getText().toString().trim() : "");

                ReportWorkManager reportWorkManager = new ReportWorkManager();
                reportWorkManager.prepareUploadWork(report, pictureUri.toString(), pictureName);
                reportWorkManager.startWork(this);

                setResult(Activity.RESULT_OK);
                finish();
            }
            else {
                Printer.snackBar(view, getString(R.string.create_report_no_picture));
            }
        }
        else {
            Printer.snackBar(view, getString(R.string.create_report_location));
            location = LocationUtils.getGPS(this);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit)
                .setMessage(R.string.create_report_exit_warning)
                .setPositiveButton(R.string.create_report_yes, (dialog, which) -> {
                    setResult(RESULT_CANCELED);
                    finish();
                }).setNegativeButton(R.string.create_report_no, (dialog, which) -> {
        }).show();
    }
}
