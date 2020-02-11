package org.inek.dataportal.common.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.Date;

class DateUtilsTest {

    @Test
    void addDays() {
        Date start = DateUtils.createDate(2000, Month.JANUARY, 1);
        Date expected = DateUtils.createDate(2000, Month.JANUARY, 11);
        Assertions.assertThat(expected).isEqualTo(DateUtils.addDays(start, 10));
    }

    @Test
    void subtractDays() {
        Date start = DateUtils.createDate(2019, Month.APRIL, 1);
        Date expected = DateUtils.createDate(2019, Month.MARCH, 31);
        Assertions.assertThat(expected).isEqualTo(DateUtils.addDays(start, -1));
    }

    @Test
    void subtractDaysAtBegin() {
        Date start = DateUtils.createDate(2019, Month.JANUARY, 1);
        Date expected = DateUtils.createDate(2018, Month.DECEMBER, 31);
        Assertions.assertThat(expected).isEqualTo(DateUtils.addDays(start, -1));
    }
}
