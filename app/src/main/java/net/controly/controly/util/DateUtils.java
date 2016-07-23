package net.controly.controly.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class is used to format date.
 */
public class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDefaultDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }

    public static Date parse(String date) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
