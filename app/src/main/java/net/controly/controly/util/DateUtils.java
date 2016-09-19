package net.controly.controly.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is used to format date.
 */
public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_WITH_TIME_ZONE = "H:mm:sZ";

    public static Date parse(String date) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String FormatTime(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_WITH_TIME_ZONE, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
