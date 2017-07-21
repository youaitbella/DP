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
    
    public static Date getNullAlias() {
        Calendar c = Calendar.getInstance();
        c.set(1900, 0, 1, 0, 0, 0);
        return c.getTime();
    }
    
    public static boolean isNullAlias(Date toCompare) {
        if(getNullAlias().getYear() == toCompare.getYear()) {
            return true;
        }
        return false;
    }
}
