package net.maku.framework.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 * 
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 日期解析
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回Date
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据 YearMonth 获取当月的开始时间和结束时间
     *
     * @param yearMonth YearMonth 对象
     * @return 包含开始时间和结束时间的 LocalDateTime 数组
     */
    public static LocalDateTime[] getMonthStartAndEndDateTime(YearMonth yearMonth) {
        // 获取当月的第一天
        LocalDate startDate = yearMonth.atDay(1);
        // 将 LocalDate 转换为 LocalDateTime，并设置时间为 00:00:00
        LocalDateTime startDateTime = startDate.atStartOfDay();

        // 获取当月的最后一天
        int lastDayOfMonth = yearMonth.atEndOfMonth().getDayOfMonth();
        LocalDate endDate = yearMonth.atDay(lastDayOfMonth);
        // 将 LocalDate 转换为 LocalDateTime，并设置时间为 23:59:59
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

    /**
     * 根据 Year 获取当年的开始时间和结束时间
     *
     * @param year Year 对象
     * @return 包含开始时间和结束时间的 LocalDateTime 数组
     */
    public static LocalDateTime[] getYearStartAndEndDateTime(Year year) {
        // 获取当年的第一天
        LocalDate startDate = LocalDate.of(year.getValue(), 1, 1);
        // 将 LocalDate 转换为 LocalDateTime，并设置时间为 00:00:00
        LocalDateTime startDateTime = startDate.atStartOfDay();

        // 获取当年的最后一天
        LocalDate endDate = LocalDate.of(year.getValue(), 12, 31);
        // 将 LocalDate 转换为 LocalDateTime，并设置时间为 23:59:59
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

    public static Date setEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 使用传入的日期初始化Calendar
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime(); // 返回设置后的时间
    }


}
