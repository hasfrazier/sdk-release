package com.tune.ma.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by gowie on 1/26/16.
 */
public class TuneDateUtils {

    public static SimpleDateFormat getTuneDateFormatter() {
        SimpleDateFormat tuneDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        tuneDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return tuneDateFormatter;
    }

    /**
     * Reverses the {@link Date#toString()} method
     * @param dateString String created from {@link Date#toString()}
     * @return Date for the given String
     */
    public static Date getDateFromString(String dateString) {
        Date parsedDate = null;
        SimpleDateFormat dateStringJsonFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            parsedDate = dateStringJsonFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    public static Date getNowUTC() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return cal.getTime();
    }

    public static boolean doesNowFallBetweenDates(Date startDate, Date endDate) {
        Date now = getNowUTC();
        return now.equals(startDate) || now.equals(endDate) || (now.after(startDate) && now.before(endDate));
    }

    public static boolean doesNowFallBeforeDate(Date date) {
        Date now = getNowUTC();
        return now.equals(date) || now.before(date);
    }

    public static boolean doesNowFallAfterDate(Date date) {
        Date now = getNowUTC();
        return now.equals(date) || now.after(date);
    }

    public static int daysSinceDate(Date pastDate) {
        // Remove the time component of the dates
        Date nowWithoutTime = removeTime(getNowUTC());
        Date pastDateWithoutTime = removeTime(pastDate);

        long diff = nowWithoutTime.getTime() - pastDateWithoutTime.getTime();
        return (int)(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    public static int secondsBetweenDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        long diff = Math.abs(firstDate.getTime() - secondDate.getTime());
        return (int)(TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Transform ISO 8601 string to Date.
     * @param iso8601String ISO 860 formatted string.
     * @return A <code>Date</code> parsed from the string.
     */
    public static Date parseIso8601(final String iso8601String) {
        if (iso8601String == null) {
            return null;
        }

        String utcString = iso8601String.replace("Z", "+00:00");

        try {
            utcString = utcString.substring(0, 22) + utcString.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            TuneDebugLog.e("TuneDateUtils", "Error building Date String: " + iso8601String);
            return null;
        }

        try {
            return getTuneDateFormatter().parse(utcString);
        } catch (ParseException e) {
            TuneDebugLog.e("TuneDateUtils", "Error parsing Date: " + iso8601String);
            return null;
        }
    }
}
