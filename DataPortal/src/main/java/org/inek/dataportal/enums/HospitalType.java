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
public enum HospitalType {

    No(-1, "enmNo"),
    AnyHospital(0, "enmHospitalTypeAny"),
    ModelProjectHospital(1, "enmHospitalTypeModelProject"),
    SpecificHospital(2, "enmHospitalTypeSpecific"),
    OtherHospital(3, "enmHospitalTypeOther");

    private final int _id;
    private final String _textId;

    private HospitalType(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String type() {
        try {
            return Utils.getMessage(_textId);
        } catch (Exception ex) {
            return "unknown text id: " + _textId;
        }

    }
}
