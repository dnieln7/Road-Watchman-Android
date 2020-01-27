package com.daniel.reportes;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.util.Random;

public class Utils {

    public static final int GOOGLE_SIGN = 1;
    public static final int TAKE_PICTURE = 2;
    public static final int SELECT_PICTURE = 3;

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 102;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 103;

    public static String generateCode() {
        return String.valueOf(new Random().nextInt(1000));
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static int GetPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);

        typedArray.recycle();

        return color;
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(String folderName, String fileName, String mimeType) {

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                folderName
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(fileName, "failed to create directory");
                return null;
            }
        }

        LocalDateTime dateTime = new LocalDateTime();

        return new File(mediaStorageDir.getPath() + File.separator +
                fileName + dateTime + mimeType);
    }
}
