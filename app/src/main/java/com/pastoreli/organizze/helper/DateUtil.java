package com.pastoreli.organizze.helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String currentDate () {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public static String monthYearDateHash (String date) {
        String items[] = date.split("/");
        return items[1] + items[2];
    }

}
