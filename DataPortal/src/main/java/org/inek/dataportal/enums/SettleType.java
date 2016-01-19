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
public enum SettleType {
    
    No(-1, "enmNo"),
    ImpartialDepartment(0, "enmSettleTypeImpDep"),
    DepartmentDocs(1, "enmSettleTypeDepDocs"),
    MiscMedics(2, "enmSettleTypeMiscDocs");

    private final int _id;
    private final String _textId;

    private SettleType(int id, String textId) {
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
