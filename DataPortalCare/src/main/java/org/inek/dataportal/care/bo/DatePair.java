package org.inek.dataportal.care.bo;

import java.util.Date;
import java.util.Objects;

public class DatePair {
    private Date _date1;
    private Date _date2;

    public Date getDate1() {
        return _date1;
    }

    public void setDate1(Date date1) {
        this._date1 = date1;
    }

    public Date getDate2() {
        return _date2;
    }

    public void setDate2(Date date2) {
        this._date2 = date2;
    }

    public DatePair(Date date1, Date date2) {
        this._date1 = date1;
        this._date2 = date2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatePair datePair = (DatePair) o;
        return Objects.equals(_date1, datePair._date1) &&
                Objects.equals(_date2, datePair._date2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_date1, _date2);
    }
}
