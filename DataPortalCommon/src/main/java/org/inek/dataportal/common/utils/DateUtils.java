/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.utils;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author muellermi
 */
public class DateUtils {

    public static Date getDateWithDayOffset(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
        return calendar.getTime();
    }

    public static Date getDateWithMinuteOffset(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, offset);
        return calendar.getTime();
    }

    public static String todayAnsi() {
        return today("yyyy-MM-dd");
    }

    public static String todayGerman() {
        return today("dd.MM.yyyy");
    }

    public static String today(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static Date getMaxDate() {
        return createDate(2050, Month.DECEMBER, 31);
    }

    public static Date createDate(int year, Month month, int day) {
        return createDate(year, month, day, 0, 0, 0);

    }

    private static Date createDate(int year, Month month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month.getValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


}
