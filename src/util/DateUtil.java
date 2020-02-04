package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date classes with helper methods.
 */
public class DateUtil {

    /**
     * Current time in milliseonds
     *
     * @return string formatted date.
     */
    public static String getCurrentTimeInMilliSecondsFormatted() {

        long milliSeconds = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(milliSeconds);
        return simpleDateFormat.format(date);
    }
}
