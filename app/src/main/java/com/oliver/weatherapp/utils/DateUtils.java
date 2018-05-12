package com.oliver.weatherapp.utils;

import android.content.Context;

import com.oliver.weatherapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final DateFormat fullDate = new SimpleDateFormat(", MMM dd, HH:mm", Locale.US);
    private static final DateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

    public static String getFriendlyDateString(Context context, long dateInMillis) {
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTime(new Date(dateInMillis));
        String dayName;
        if (date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            dayName = context.getString(R.string.today);
        } else if (date.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == 1) {
            dayName = context.getString(R.string.tomorrow);
        } else {
            dayName = dayFormat.format(date.getTime());
        }

       return dayName + fullDate.format(new Date(dateInMillis));
    }
}
