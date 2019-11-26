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

    public static SimpleDateFormat ANSI_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat GERMAN_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    public static Date getDateWithDayOffset(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 24 * offset);
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

    public static Date getMaxDate() {
        return createDate(2079, Month.JANUARY, 1);
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
