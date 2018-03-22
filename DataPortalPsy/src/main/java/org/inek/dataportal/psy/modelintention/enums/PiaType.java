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
public enum PiaType {
    
    None(-1, "enmNo"),
    AnyPIA(0, "enmPiaTypeAny"),
    IntegratedPIA(1, "enmPiaTypeIntegrated"),
    ContractPIA(2, "enmPiaTypeContract"),
    SpecificPIA(3, "enmPiaTypeSpecific");

    private final int _id;
    private final String _textId;

    PiaType(int id, String textId) {
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
