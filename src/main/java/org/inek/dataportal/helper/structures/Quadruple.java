/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.structures;

/**
 *
 * @author muellermi
 */
public class Quadruple<T1, T2, T3, T4> {

    private T1 _value1;
    private T2 _value2;
    private T3 _value3;
    private T4 _value4;

    public Quadruple() {
    }

    public Quadruple(final T1 value1, final T2 value2, final T3 value3, final T4 value4) {
        _value1 = value1;
        _value2 = value2;
        _value3 = value3;
        _value4 = value4;
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

    public T4 getValue4() {
        return _value4;
    }

    public void setValue4(T4 value4) {
        this._value4 = value4;
    }
}
