package hasoffer.adp.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chevy on 16-11-19.
 */
public class TimeUtils {

    private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

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

}
