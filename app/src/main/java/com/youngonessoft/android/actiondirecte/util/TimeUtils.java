package com.youngonessoft.android.actiondirecte.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.youngonessoft.android.actiondirecte.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bobek on 26/02/2018.
 */

public class TimeUtils {
    public static final Calendar FIRST_DAY_OF_TIME;
    public static final Calendar LAST_DAY_OF_TIME;
    public static final int DAYS_OF_TIME;
    public static final int MONTHS_OF_TIME;

    //public static final int TEST;

    static {
        FIRST_DAY_OF_TIME = Calendar.getInstance();
        FIRST_DAY_OF_TIME.set(Calendar.MILLISECOND, 1);
        FIRST_DAY_OF_TIME.set(1950, Calendar.JANUARY, 1, 0, 0, 0);
        LAST_DAY_OF_TIME = Calendar.getInstance();
        LAST_DAY_OF_TIME.set(Calendar.MILLISECOND, 1);
        LAST_DAY_OF_TIME.set(2150, Calendar.DECEMBER, 31, 0, 0, 0);
        DAYS_OF_TIME = (int) ((LAST_DAY_OF_TIME.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis()) / (24 * 60 * 60 * 1000)); //73413
        MONTHS_OF_TIME = 12 * (LAST_DAY_OF_TIME.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR));

        Log.i("TimeUtils", "DAYS_OF_TIME: " + DAYS_OF_TIME);
    }

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param day
     * @return the position or 0 if day is null
     */
    public static int getPositionForDay(Calendar day) {
        if (day != null) {
            day.set(Calendar.HOUR_OF_DAY, 0);
            day.set(Calendar.MINUTE, 0);
            day.set(Calendar.SECOND, 0);
            day.set(Calendar.MILLISECOND, 1);

            int position = (int) ((day.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis()) / 86400000);
            Log.i("getPositionForDay", "P: " + position + ", D: " + convertDate(day.getTimeInMillis(), "yyyy-MM-dd"));

            return (position);
        }
        return 0;
    }

    /**
     * Get the position in the ViewPager for a given month
     *
     * @param day
     * @return the position or 0 if month is null
     */
    public static int getPositionForMonth(Calendar day) {
        if (day != null) {
            return day.get(Calendar.MONTH) + 12 * (day.get(Calendar.YEAR) - FIRST_DAY_OF_TIME.get(Calendar.YEAR));
        }
        return 0;
    }

    /**
     * Get the day for a given position in the ViewPager
     *
     * @param position
     * @return the day
     * @throws IllegalArgumentException if position is negative
     */
    public static Calendar getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, position);
        cal.add(Calendar.MILLISECOND, (int) millisToStartOfDay());

        Log.i("getDayForPosition", "P: " + position + ", D: " + convertDate(cal.getTimeInMillis(), "yyyy-MM-dd"));

        return cal;
    }

    public static long millisToStartOfDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long millis = (System.currentTimeMillis() - c.getTimeInMillis());
        return millis;
    }

    public static String convertDate(long dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }

    public static String getFormattedDate(Context context, long date) {
        final String defaultPattern = "yyyy-MM-dd";

        String pattern = null;
        if (context != null) {
            pattern = context.getString(R.string.date_format);
        }
        if (pattern == null) {
            pattern = defaultPattern;
        }
        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat(pattern);
        } catch (IllegalArgumentException e) {
            simpleDateFormat = new SimpleDateFormat(defaultPattern);
        }

        return simpleDateFormat.format(new Date(date));
    }

    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        setMidnight(calendar);

        return calendar;
    }

    public static void setMidnight(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
    }


}

