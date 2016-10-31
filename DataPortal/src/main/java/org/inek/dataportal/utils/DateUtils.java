/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.utils;

import java.text.SimpleDateFormat;
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

    // returns the previous year until max month
    // after max, we assume to be in the previous year
    public static int getDataYear(int max) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (month < max){
            return year - 1;
        }
        return year;
    }

}
