package org.inek.dataportal.common.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.Date;

class DateUtilsTest {

    @Test
    void addDays() {
        Date start = DateUtils.createDate(2000, Month.JANUARY, 1);
        Date expected = DateUtils.createDate(2000, Month.JANUARY, 11);
        Assert.assertEquals(expected, DateUtils.addDays(start, 10));
    }
}