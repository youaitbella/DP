package org.inek.dataportal.care.proof;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IkYearQuarterTest {

    @Test
    void nextQuarterReturnsSameYearIf1() {
        IkYearQuarter start = new IkYearQuarter(0, 2000, 1);
        IkYearQuarter next = IkYearQuarter.nextQuarter(start);
        IkYearQuarter expected = new IkYearQuarter(0, 2000, 2);
        assertThat(next).isEqualTo(expected);
    }

    @Test
    void nextQuarterReturnsSameYearIf3() {
        IkYearQuarter start = new IkYearQuarter(0, 2000, 3);
        IkYearQuarter next = IkYearQuarter.nextQuarter(start);
        IkYearQuarter expected = new IkYearQuarter(0, 2000, 4);
        assertThat(next).isEqualTo(expected);
    }

    @Test
    void nextQuarterReturnsNextYearIf4() {
        IkYearQuarter start = new IkYearQuarter(0, 2000, 4);
        IkYearQuarter next = IkYearQuarter.nextQuarter(start);
        IkYearQuarter expected = new IkYearQuarter(0, 2001, 1);
        assertThat(next).isEqualTo(expected);
    }
}