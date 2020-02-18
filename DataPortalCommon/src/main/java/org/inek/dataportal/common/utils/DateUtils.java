package org.inek.dataportal.common.utils;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import static org.inek.dataportal.api.helper.PortalConstants.MILLISECONDS_PER_DAY;
import static org.inek.dataportal.api.helper.PortalConstants.MILLISECONDS_PER_HOUR;

public class DateUtils {

    public static final Date MIN_DATE = createDate(1900, Month.JANUARY, 1);
    public static final Date MAX_DATE = createDate(2079, Month.JANUARY, 1);

    private static final SimpleDateFormat ANSI_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat GERMAN_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    public static Date getDateWithDayOffset(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);
        return calendar.getTime();
    }

    public static Date getDateWithMinuteOffset(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, offset);
        return calendar.getTime();
    }

    public static String toAnsi(Date date) {
        return ANSI_FORMATTER.format(date);
    }

    public static String todayAnsi() {
        return ANSI_FORMATTER.format(new Date());
    }

    public static String toGerman(Date date) {
        return GERMAN_FORMATTER.format(date);
    }

    public static String todayGerman() {
        return GERMAN_FORMATTER.format(new Date());
    }

    public static Date createDate(int year, Month month, int day) {
        return createDate(year, month.getValue(), day, 0, 0, 0);
    }

    public static Date createDate(int year, int month, int day) {
        return createDate(year, month, day, 0, 0, 0);
    }

    private static Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int currentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int currentMonth() {
        return 1 + Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int diffDays(Date from, Date to) {
        long diff = to.getTime() - from.getTime() + MILLISECONDS_PER_HOUR; // add one hour due to summertime switch
        return (int) (diff / MILLISECONDS_PER_DAY);

    }

    public static int duration(Date from, Date to) {
        return 1 + diffDays(from, to);
    }

    public static Period firstAndLastDayOfMonth(int year, int month) {
        Date fromDate = createDate(year, month, 1);
        Date toDate = addDays(createDate(year + month / 12, (month + 1) % 12, 1), -1);
        return new Period(fromDate, toDate);
    }
}
