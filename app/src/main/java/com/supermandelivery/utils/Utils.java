package com.supermandelivery.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Office on 3/16/2016.
 */
public class Utils {

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() > 9) {
            return true;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static String timeConversion(int totalSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = totalSeconds / SECONDS_IN_A_MINUTE;
        totalSeconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return hours + " hours " + minutes + " minutes " + totalSeconds + " seconds";
    }

    public static double secondsToMin(double totalSeconds) {
        final int SECONDS_IN_A_MINUTE = 60;
        double minutes = totalSeconds / SECONDS_IN_A_MINUTE;
        return minutes;
    }

    /**
     * The calculateKilometers method displays the kilometers that are equivalent to
     * a specified number of meters.
     *
     * @param meters
     * @return the number of kilometers
     */
    public static double calculateKilometers(double meters) {
        double kilometers = meters * 0.001;
        return kilometers;
    }

    //"expiryDate": "2016-03-22",
    public static String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return simpleDateFormat.format(currentDate.getTime());
    }

    public static String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        return simpleDateFormat.format(currentDate.getTime());
    }

    //we will use this for comparing current time with selected time
    public static String getCurrentCompareDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH", Locale.ENGLISH);
        return simpleDateFormat.format(currentDate.getTime());
    }

    public static String getCurrentHour() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.ENGLISH);
        return simpleDateFormat.format(currentDate.getTime());
    }

    public static String getEndHour(int hrVal) {
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.HOUR, hrVal);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh", Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String getCurrentAPPM(int hourOfDay, int minute) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        return am_pm;
    }

    public static String convert24To12(String _24HourTime) {

        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date _24HourDt;
        String convertDate = "";
        try {
            _24HourDt = _24HourSDF.parse(_24HourTime);
            convertDate = _12HourSDF.format(_24HourDt.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDate;
    }

    //end time is greater than the current time
    public static String getEndTime(int hourVal) {
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.HOUR, hourVal);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }

    //@returns true
    public static boolean compareDate(String selectedTime, String currentTime) {
        boolean compare = false;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date currentDate = formatter.parse(currentTime);
            Date selectedDate = formatter.parse(selectedTime);

            if (currentDate.before(selectedDate) || currentDate.equals(selectedDate)) {
                compare = true;
            } else {
                compare = false;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return compare;
    }

    //@returns true
    public static boolean compareDateTime(String currentTime, String selectedTime) {

        boolean compare = false;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date currentDate = formatter.parse(currentTime);
            Date selectedDate = formatter.parse(selectedTime);

            if (currentDate.before(selectedDate)) {
                compare = true;
            } else {
                compare = false;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return compare;
    }

    //1900-01-23T06:00:00.000Z
    // Convert raw date into desired date and retruns parsed string
    public static String convertDate(String inputDate) {

        String outputFormat = "dd MMMM, yyyy";//Wed, September 21,2011 02:17:48 pm
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());
        String outputDate = "";
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)).parse(inputDate.replaceAll("Z$", "+0000"));
            outputDate = df_output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static String convertToString(String inputDate) {

        String outputFormat = "EEE MMM yyyy";//Wed, September 21,2011 02:17:48 pm
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());
        String outputDate = "";
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)).parse(inputDate);
            outputDate = df_output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

//    private void convertToString(String convertDate) {
//        SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//        SimpleDateFormat date = new SimpleDateFormat("EEE MMM yyyy");
//        SimpleDateFormat time = new SimpleDateFormat("hh:mm aa");
//        String date_str = date.format(email_date);
//        String time_str = time.format(email_date);
//    }

    //Convert date into time and retruns parsed string
    public static String convertTime(String inputDate) {
        String outputFormat = "h:mm aa";//Wed, September 21,2011 02:17:48 pm
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());
        String outputDate = "";
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)).parse(inputDate.replaceAll("Z$", "+0000"));
            outputDate = df_output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
}
