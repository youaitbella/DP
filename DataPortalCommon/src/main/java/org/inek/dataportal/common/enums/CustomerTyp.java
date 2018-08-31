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
    private final String _textId;

    CustomerTyp(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }
}
