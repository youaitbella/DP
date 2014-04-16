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
public enum TreatmentType {
            
    No(0, "enmTreatmentTypeNo"),
    Generally(1, "enmTreatmentTypeGeneral"),
    SpecialSetting(2, "enmTreatmentTypeSpecial");

    private int _id;
    private String _textId;

    private TreatmentType(int id, String textId) {
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
