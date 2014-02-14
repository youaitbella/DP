package org.inek.dataportal.helper.structures;

/**
 *
 * @author muellermi
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
        this._value1 = value1;
    }

    public T2 getValue2() {
        return _value2;
    }

    public void setValue2(T2 value2) {
        this._value2 = value2;
    }

    public T3 getValue3() {
        return _value3;
    }

    public void setValue3(T3 value3) {
        this._value3 = value3;
    }
}
