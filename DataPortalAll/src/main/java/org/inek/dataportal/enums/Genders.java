/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

import org.inek.dataportal.helper.Utils;

/**
 *
 * @author schlappajo
 */
public enum Genders {

    NoAttribute(0, "enmNoAttribute"),
    Male(2, "enmGenderMale"),
    Female(1, "enmGenderFemale");

    private final int _id;
    private final String _textId;

    Genders(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String gender() {
        return Utils.getMessage(_textId);
    }
}
