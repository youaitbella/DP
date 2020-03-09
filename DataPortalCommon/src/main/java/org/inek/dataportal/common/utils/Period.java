package org.inek.dataportal.common.utils;

import java.util.Date;
import java.util.Objects;

public class Period {
    private final Date from;
    private final Date to;

    public Period(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date from() {
        return from;
    }

    public Date to() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period that = (Period) o;
        return from.equals(that.from) &&
                to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
