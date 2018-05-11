package com.oliver.weatherapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final DateFormat fullDate = new SimpleDateFormat("EEE, MMM dd, HH:mm", Locale.US);
    public static String getFriendlyDateString(long dateInMillis) {
       return fullDate.format(new Date(dateInMillis));
    }
}
