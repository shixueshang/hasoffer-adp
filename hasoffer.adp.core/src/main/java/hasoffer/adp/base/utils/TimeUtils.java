package hasoffer.adp.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chevy on 16-11-19.
 */
public class TimeUtils {

    public static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

    public static String hourDatePattern = "yyyyMMddHH";

    public static Date now(){
        return new Date();
    }

    /**
     * 获得当前时间前一个小时
     *
     * @return
     */
    public static Date getBeforeHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat(defaultDatePattern);
        return calendar.getTime();
    }


    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static final Date parseDate(String strDate, String datePattern) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(datePattern);
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
        }
        return date;
    }

    public static final Date parseUSDate(String strDate, String datePattern) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(datePattern, Locale.US);
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
        }
        return date;
    }

    /**
     * 获得指定日期的起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得指定日期的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndTimeOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getFirstDayOfMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

}
