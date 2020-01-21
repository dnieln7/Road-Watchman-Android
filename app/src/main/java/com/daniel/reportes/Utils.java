package com.daniel.reportes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import java.util.Random;

public class Utils {

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
}
