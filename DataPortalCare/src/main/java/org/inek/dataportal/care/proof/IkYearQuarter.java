package org.inek.dataportal.care.proof;

import java.util.Objects;

public class IkYearQuarter {
    private final int ik;
    private final int year;
    private final int quarter;

    public IkYearQuarter(int ik, int year, int quarter) {
        this.ik = ik;
        this.year = year;
        this.quarter = quarter;
    }

    public static IkYearQuarter nextQuarter(IkYearQuarter current) {
        int year = current.year + (current.quarter == 4 ? 1 : 0);
        int quarter = (current.quarter % 4) + 1;
        return new IkYearQuarter(current.ik, year, quarter);
    }

    public int getIk() {
        return ik;
    }

    public int getYear() {
        return year;
    }

    public int getQuarter() {
        return quarter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IkYearQuarter that = (IkYearQuarter) o;
        return ik == that.ik &&
                year == that.year &&
                quarter == that.quarter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ik, year, quarter);
    }
}
