/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.enums;

/**
 *
 * @author schlappajo
 */
public enum CustomerTyp {

    Hospital(0, "Krankenhaus"),
    Insurance(1, "Krankenkasse");

    private final int _id;
    private final String _name;

    CustomerTyp(int id, String textId) {
        _id = id;
        _name = textId;
    }

    public int id() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public static CustomerTyp valueById(int id) {
        for (CustomerTyp value : CustomerTyp.values()) {
            if (value.id() == id) {
                return value;
            }
        }
        return CustomerTyp.Hospital;
    }
}
