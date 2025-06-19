package com.android.coffeeshop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date getStartOfWeek() {
        Calendar calendar = Calendar.getInstance();
        // Set to first day of week (Monday in most locales)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        // Set time to start of day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfWeek() {
        Calendar calendar = Calendar.getInstance();
        // Start with first day of week
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        // Add 6 days to get to Sunday
        calendar.add(Calendar.DATE, 6);
        // Set time to end of day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatDateForChart(String dbDateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            Date date = inputFormat.parse(dbDateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dbDateString;
        }
    }

    public static String getWeekRangeDisplay() {
        Date startDate = getStartOfWeek();
        Date endDate = getEndOfWeek();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(startDate) + " - " + sdf.format(endDate);
    }
}