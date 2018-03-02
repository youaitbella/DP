/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.helper.structures;

import java.lang.reflect.Field;

/**
 *
 * @author muellermi
 */
public class FieldValues {

    private final Field _field;
    private final Object _value1;
    private final Object _value2;

    public FieldValues(Field field, Object value1, Object value2) {
        _field = field;
        _value1 = value1;
        _value2 = value2;

    }

    public Field getField() {
        return _field;
    }

    public Object getValue1() {
        return _value1;
    }

    public Object getValue2() {
        return _value2;
    }
}
