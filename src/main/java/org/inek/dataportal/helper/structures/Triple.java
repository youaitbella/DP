package org.inek.dataportal.helper.structures;

import java.util.Objects;

/**
 *
 * @author muellermi
 * @param <T1>
 * @param <T2>
 * @param <T3>
 */
public class Triple<T1, T2, T3> {

    private T1 _value1;
    private T2 _value2;
    private T3 _value3;

    public Triple() {
    }

    public Triple(final T1 value1, final T2 value2, final T3 value3) {
        _value1 = value1;
        _value2 = value2;
        _value3 = value3;
    }

    public T1 getValue1() {
        return _value1;
    }

    public void setValue1(T1 value1) {
        _value1 = value1;
    }

    public T2 getValue2() {
        return _value2;
    }

    public void setValue2(T2 value2) {
        _value2 = value2;
    }

    public T3 getValue3() {
        return _value3;
    }

    public void setValue3(T3 value3) {
        _value3 = value3;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(_value1);
        hash = 37 * hash + Objects.hashCode(_value2);
        hash = 37 * hash + Objects.hashCode(_value3);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
        if (!Objects.equals(_value1, other._value1)) {
            return false;
        }
        if (!Objects.equals(_value2, other._value2)) {
            return false;
        }
        if (!Objects.equals(_value3, other._value3)) {
            return false;
        }
        return true;
    }
    
}
