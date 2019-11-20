package org.inek.dataportal.care.entities;


import java.io.Serializable;
import java.util.Objects;

public class ExtensionId implements Serializable {

    private static final long serialVersionUID = 1L;
    private int _ik;
    private int _year;
    private int _quarter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionId that = (ExtensionId) o;
        return _ik == that._ik &&
                _year == that._year &&
                _quarter == that._quarter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ik, _year, _quarter);
    }
}
