/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.structures;

/**
 *
 * @author muellermi
 */
public class Pair<T1, T2> {

    private T1 _value1;
    private T2 _value2;

    public Pair() {
    }

    public Pair(final T1 value1, final T2 value2) {
        _value1 = value1;
        _value2 = value2;
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
}
