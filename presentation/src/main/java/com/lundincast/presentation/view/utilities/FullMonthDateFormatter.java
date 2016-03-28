package com.lundincast.presentation.view.utilities;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for proper date formatting
 */
public class FullMonthDateFormatter {

    private static String[] monthsComplete = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private static String[] shortMonthName = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug",
            "Sep", "Oct", "Nov", "Dec"};
    private static String[] shortDaysOfWeekName = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    public FullMonthDateFormatter() {}

    public static String getCompleteMonthName(int index) {
        return monthsComplete[index];
    }

    public static String getShortMonthName(int index) {
        return shortMonthName[index];
    }

    public static String getShortFormattedDate(Calendar cal) {
        String formattedDate = null;
        if (dateIsToday(cal)) {
            formattedDate = "Today";
        } else if (dateIsYesterday(cal)) {
            formattedDate = "Yesterday";
        } else {
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int Year = cal.get(Calendar.YEAR);
            formattedDate = "" + String.valueOf(day) + " " + shortMonthName[month] + " " + String.valueOf(Year);
        }
        return formattedDate;
    }

    public static String getShortDayOfWeekName(Calendar cal) {

        return shortDaysOfWeekName[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    private static boolean dateIsToday(Calendar cal) {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        if (today.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)
                && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                && today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean dateIsYesterday(Calendar cal) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (yesterday.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                && yesterday.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }
}
