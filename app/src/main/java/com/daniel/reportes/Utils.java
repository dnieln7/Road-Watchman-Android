package com.daniel.reportes;

import java.util.Random;

public class Utils {

    public static String generateCode() {
        return String.valueOf(new Random().nextInt(1000));
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
