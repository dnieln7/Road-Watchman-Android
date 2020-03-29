package com.daniel.reportes.data.dao;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static double[] fromLocation(String value) {
        String lat = value.split(",")[0];
        String lon = value.split(",")[1];

        return new double[]{Double.parseDouble(lat), Double.parseDouble(lon)};
    }

    @TypeConverter
    public static String toLocation(double[] location) {
        return location[0] + "," + location[1];
    }
}
