package com.unioulu.ontime.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {

    private static final String DATE_FORMAT = "HH:mm";
    private static DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        } else {
            return null;
        }
    }

    public static String fromDateToStringHoursMinutes(Date date) {
        int hours = date.getHours();
        int minutes = date.getMinutes();
        String time = "";

        if(hours < 10) {
            time += "0" + hours + ":";
        } else {
            time += hours + ":";
        }

        if(minutes < 10) {
            time += "0" + minutes;
        } else {
            time += minutes;
        }

        return time;
    }
}
