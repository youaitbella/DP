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
public enum SelfHospitalisationType {
        
    Possible(0, "enmSelfHospitalisationTypePossible"),
    EmergencyPossible(1, "enmSelfHospitalisationTypeEmergency"),
    NotPossible(2, "enmSelfHospitalisationTypeImpossible");

    private final int _id;
    private final String _textId;

    SelfHospitalisationType(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String type() {
        return Utils.getMessage(_textId);
    }
}
