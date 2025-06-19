package com.android.coffeeshop.utils;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {

    

    // Chuyển Date thành String (định dạng "yyyy-MM-dd HH:mm:ss")
    @TypeConverter
    public static String fromDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // Định dạng bao gồm ngày và giờ
        return dateFormat.format(date);
    }

    // Chuyển String thành Date (định dạng "yyyy-MM-dd HH:mm:ss")
    @TypeConverter
    public static Date fromStringToDate(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
