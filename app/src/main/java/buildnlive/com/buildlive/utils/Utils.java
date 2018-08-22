package buildnlive.com.buildlive.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import buildnlive.com.buildlive.console;

public class Utils {

    public static String parseMonth(int month) {
        switch (month) {
            case 0:
                return "JAN";
            case 1:
                return "FEB";
            case 2:
                return "MAR";
            case 3:
                return "APR";
            case 4:
                return "MAY";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AUG";
            case 8:
                return "SEP";
            case 9:
                return "OCT";
            case 10:
                return "NOV";
            case 11:
                return "DEC";
        }
        return "MONTH";
    }

    public static int parseYear(int year) {
        return year + 1900;
    }

    public static Date fromISODateFormat(String dateStr) {
        java.util.TimeZone tz = java.util.TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(tz);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static int daysElapsed(long timestamp) {
        long current = System.currentTimeMillis();
        long diff = current - timestamp;
        if(diff < 0){
            diff = diff * -1;
        }
        return (int)diff / (24 * 60 * 60 * 1000);
    }


    public static final String fromTimeStampToDate(long time) {
        Date date = new Date(time);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static final String timeFromTimeStamp(long time) {
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(date);
    }

    public static final int MIN_IN_MS = 60000;

    public static boolean isBeforeTime(long time, int amount_in_min) {
        return (differenceInMin(time, System.currentTimeMillis()) - amount_in_min) > 0 ? true : false;
    }

    public static int differenceInMin(long one, long two) {
        long diff = two - one;
        int res = (int) diff / MIN_IN_MS;
        return res;
    }
}
