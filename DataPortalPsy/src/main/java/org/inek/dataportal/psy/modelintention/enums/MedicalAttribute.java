/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.modelintention.enums;

import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author schlappajo
 */
public enum MedicalAttribute {

    NoAttribute(-1, "enmNoAttribute"),
    MainDiagnosis(0, "enmMedAttrMainDia"),
    PracticeAreas(1, "enmMedAttrPracticeArea"),
    Misc(2, "enmMedAttrMisc");

    private final int _id;
    private final String _textId;

    MedicalAttribute(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String attribute() {
        try {
            return Utils.getMessage(_textId);
        } catch (Exception e) {
            return "unknown text id: " + _textId;
        }
    }
}
