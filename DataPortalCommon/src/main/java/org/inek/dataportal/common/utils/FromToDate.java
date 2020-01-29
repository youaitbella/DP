package org.inek.dataportal.common.utils;

import java.util.Date;
import java.util.Objects;

public class FromToDate {
    private final Date from;
    private final Date to;

public FromToDate (Date from, Date to){
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
        FromToDate that = (FromToDate) o;
        return from.equals(that.from) &&
                to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
